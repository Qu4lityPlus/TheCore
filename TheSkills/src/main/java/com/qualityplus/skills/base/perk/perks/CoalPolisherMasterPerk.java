package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class CoalPolisherMasterPerk extends Perk {
    @Builder
    public CoalPolisherMasterPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);
    }

    @EventHandler(ignoreCancelled = true)
    public void handlePerk(BlockBreakEvent e) {
        Block block = e.getBlock();

        if (!block.getType().equals(XMaterial.COAL_ORE.parseMaterial())) return;

        Player p = e.getPlayer();

        if (RandomUtil.randomBetween(0.0, 100.0) >= chancePerLevel * getStat(p))
            return;

        e.setDropItems(false);

        block.getWorld().dropItem(block.getLocation(), XMaterial.DIAMOND.parseItem());
    }
}
