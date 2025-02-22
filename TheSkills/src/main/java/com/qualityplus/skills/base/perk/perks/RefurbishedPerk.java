package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.nms.NMS;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class RefurbishedPerk extends Perk {
    private List<XMaterial> toolList;

    @Builder
    public RefurbishedPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double baseAmount, double chancePerLevel, List<XMaterial> toolList) {
        super(id, enabled, displayName, description, skillGUIOptions, baseAmount, chancePerLevel);

        this.toolList = toolList;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;

        ItemStack inHand = p.getItemInHand();

        if (BukkitItemUtil.isNull(inHand)) return;

        if (!toolList.contains(XMaterial.matchXMaterial(inHand.getType()))) return;

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(p))
            return;

        NMS nms = TheAssistantPlugin.getAPI().getNms();

        short durability = inHand.getType().getMaxDurability();

        Bukkit.getScheduler().runTask(TheSkills.getApi().getPlugin(), () -> p.setItemInHand(nms.setDurability(inHand, durability)));

    }
}
