package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.base.perk.perks.common.AbstractRandomBlockDropPerk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
public final class LeavesMasterPerk extends AbstractRandomBlockDropPerk {
    @Builder
    public LeavesMasterPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel, Map<XMaterial, Double> itemAndChances) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel, itemAndChances);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getClickedBlock() == null) return;

        ItemStack inHand = e.getPlayer().getItemInHand();

        if (BukkitItemUtil.isNull(inHand)) return;

        if (!inHand.getType().equals(XMaterial.SHEARS.parseMaterial())) return;

        Player p = e.getPlayer();

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(p))
            return;

        ItemStack toGive = getItem();

        if (BukkitItemUtil.isNull(toGive)) return;

        Block block = e.getClickedBlock();

        block.getWorld().dropItem(block.getLocation(), toGive);
    }
}
