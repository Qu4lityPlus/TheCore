package com.qualityplus.dragon.api.game.structure;

import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

@AllArgsConstructor
public abstract class AbstractStructure extends OkaeriConfig {
    protected @Getter final Location location;

    public World getWorld() {
        return location.getWorld();
    }
}
