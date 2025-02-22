package com.qualityplus.dragon.base.game.structure;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.util.block.BlockUtils;
import com.qualityplus.dragon.api.game.structure.type.DragonAltar;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

@Getter
@AllArgsConstructor
public final class DragonAltarImpl extends OkaeriConfig implements DragonAltar {
    private final Location location;
    private boolean enderKey;

    @Override
    public void removeStructure() {
        getLocation().getBlock().setType(Material.AIR);
    }

    @Override
    public World getWorld() {
        return location.getWorld();
    }

    @Override
    public void setEnderKey(boolean enderKey) {
        this.enderKey = enderKey;

        Block block = location.getBlock();

        if (BlockUtils.isNull(block)) return;

        TheAssistantPlugin.getAPI().getNms().setEnderEye(block, enderKey);
    }
}
