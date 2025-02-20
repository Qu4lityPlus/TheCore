package com.qualityplus.dragon.base.game.dragon;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;

@Getter
public final class TheMythicDragonEntity implements TheDragonEntity {
    private final String id;
    private final double chance;
    private final double xp;
    private final int level;
    private EnderDragon enderDragon;

    public TheMythicDragonEntity(String id, double chance, double xp, int level) {
        this.id = id;
        this.chance = chance;
        this.xp = xp;
        this.level = level;
    }

    @Override
    public EnderDragon spawn(Location location) {
        enderDragon = (EnderDragon) TheAssistantPlugin.getAPI().getAddons().getMythicMobs().spawn(id, location, level);
        return enderDragon;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public double getMaxHealth() {
        try {
            if (enderDragon == null) return 0;
            return enderDragon.getMaxHealth();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public double getHealth() {
        try {
            if (enderDragon == null) return 0;
            return enderDragon.getHealth();
        } catch (Exception e) {
            return 0;
        }
    }
}
