package com.qualityplus.dragon.base.game.structure;

import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Exclude;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.game.structure.type.DragonCrystal;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Optional;

@RequiredArgsConstructor
public final class DragonCrystalImpl extends OkaeriConfig implements DragonCrystal {
    @Exclude
    private EnderCrystal enderCrystal;
    @Getter
    private final Location location;

    @Override
    public void removeStructure() {
        Optional.ofNullable(enderCrystal).ifPresent(EnderCrystal::remove);
    }

    @Override
    public World getWorld() {
        return location.getWorld();
    }

    @Override
    public void spawn() {
        if (location == null) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&c[TheDragon] Failed to add dragon crystals location is null!"));
            return;
        }

        World world1 = location.getWorld();

        if (world1 == null) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&c[TheDragon] Failed to add dragon crystals world is null!"));
            return;
        }

        Bukkit.getScheduler().runTask(TheDragon.getApi().getPlugin(), () -> {
            try {
                enderCrystal = location.getWorld().spawn(location.clone().add(0.0D, 1.0D, 0.0D), EnderCrystal.class);

                enderCrystal.setMetadata("theDragonCrystal", new FixedMetadataValue(TheDragon.getApi().getPlugin(), "theDragonCrystal"));
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(StringUtils.color("&c[TheDragon] Failed to add dragon crystals TASK EXCEPTION!"));
            }
        });
    }
}
