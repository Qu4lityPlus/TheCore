package com.qualityplus.dragon.base.game.guardian;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.dragon.api.game.guardian.Guardian;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public final class DragonGuardian extends OkaeriConfig implements Guardian {
    private final String id;
    private String displayName;
    private double health;

    private double damage;
    private final GuardianArmor guardianArmor;
    private String entity;

    public DragonGuardian(String id, GuardianArmor guardianArmor) {
        this.id = id;
        this.guardianArmor = guardianArmor;
    }

    @Override
    public Entity spawn(Location location) {
        if (location == null || health <= 1 || entity == null) return null;

        LivingEntity guardian = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.valueOf(entity));

        Optional.ofNullable(displayName).ifPresent(name -> {
            guardian.setCustomName(StringUtils.color(name));
            guardian.setCustomNameVisible(true);
        });

        guardian.setMaxHealth(health);
        guardian.setHealth(health);

        EntityEquipment equipment = guardian.getEquipment();

        if (equipment != null) {
            if (BukkitItemUtil.isNotNull(guardianArmor.getWeapon())) equipment.setItemInMainHand(guardianArmor.getWeapon());
            if (BukkitItemUtil.isNotNull(guardianArmor.getHelmet())) equipment.setHelmet(guardianArmor.getHelmet());
            if (BukkitItemUtil.isNotNull(guardianArmor.getChestplate())) equipment.setChestplate(guardianArmor.getChestplate());
            if (BukkitItemUtil.isNotNull(guardianArmor.getLeggings())) equipment.setLeggings(guardianArmor.getLeggings());
            if (BukkitItemUtil.isNotNull(guardianArmor.getBoots())) equipment.setBoots(guardianArmor.getBoots());
        }

        return guardian;
    }

    @Override
    public String getID() {
        return id;
    }
}
