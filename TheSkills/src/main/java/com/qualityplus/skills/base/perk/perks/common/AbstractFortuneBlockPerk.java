package com.qualityplus.skills.base.perk.perks.common;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.block.BlockUtils;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.base.event.ItemFortunePerkEvent;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.util.SkillsPlayerUtil;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public abstract class AbstractFortuneBlockPerk extends AbstractFortunePerk {
    public AbstractFortuneBlockPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double baseAmount, double chancePerLevel, List<XMaterial> allowedMaterials) {
        super(id, enabled, displayName, description, skillGUIOptions, baseAmount, chancePerLevel, allowedMaterials);
    }

    protected boolean isAllowedMaterial(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if (!SkillsPlayerUtil.isInSurvival(player)) return false;

        XMaterial material = XMaterial.matchXMaterial(e.getBlock().getType());

        return getAllowedMaterials().contains(material);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void  onBreak(BlockBreakEvent event) {
        if (!isAllowedMaterial(event)) return;

        Player player = event.getPlayer();

        Block block = event.getBlock();

        if (BlockUtils.isPlacedByPlayer(block))
            return;

        double level = getStat(player);

        double chance = getChance(level);

        int additional = getMultiplier(level) - 2;

        if (additional <= 0)
            return;

        if (RandomUtil.randomBetween(0.0, 100.0) < chance)
            additional++;

        ItemFortunePerkEvent fortunePerkEvent = new ItemFortunePerkEvent(player, this, new ArrayList<>(event.getBlock().getDrops()), event.getBlock().getLocation());

        Bukkit.getPluginManager().callEvent(fortunePerkEvent);

        if (fortunePerkEvent.getToDropItems().isEmpty() || fortunePerkEvent.isCancelled())
            return;

        int finalAdditional = additional;

        fortunePerkEvent.getToDropItems().forEach(item -> block.getWorld().dropItem(block.getLocation(), getModified(item, finalAdditional)));
    }
}
