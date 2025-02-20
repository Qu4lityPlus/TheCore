package com.qualityplus.minions.persistance.data;

import com.qualityplus.assistant.api.util.MathUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.base.gui.main.handler.click.UpgradeSlot;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionFuelUpgrade;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionUpgrade;
import com.qualityplus.minions.persistance.data.upgrade.AutomatedShippingEntity;
import com.qualityplus.minions.persistance.data.upgrade.FuelEntity;
import com.qualityplus.minions.persistance.data.upgrade.SkinEntity;
import com.qualityplus.minions.persistance.data.upgrade.UpgradeEntity;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class MinionData extends Document {
    private UUID uuid;
    private UUID owner;
    private int level;
    private String minionId;
    private int resourcesGenerated;
    private Map<Integer, ItemStack> itemStackList;
    private FuelEntity fuel = null;
    private SkinEntity skinEntity = null;
    private UpgradeEntity firstUpgrade = null;
    private UpgradeEntity secondUpgrade = null;
    private AutomatedShippingEntity autoSell = null;
    private Location location;

    public void addLevel(int i) {
        this.level += i;
    }

    public void addOneLevel() {
        addLevel(1);
    }

    public void removeFuel() {
        this.fuel = null;
    }

    public void removeUpgrade(UpgradeSlot slot) {
        if (slot == UpgradeSlot.FIRST_SLOT)
            firstUpgrade = null;
        else
            secondUpgrade = null;
    }

    public void removeAutoShip() {
        this.autoSell = null;
    }

    public void removeSkin() {
        this.skinEntity = null;
    }

    public double getFuelReductionSeconds(double time) {
        if (fuel == null) return 0D;

        MinionFuelUpgrade fuelConfig = TheMinions.getApi()
                .getConfigFiles()
                .fuelUpgrades()
                .fuelUpgrades.getOrDefault(fuel.getId(), null);

        return Optional.ofNullable(fuelConfig)
                .map(f -> MathUtil.getPercentage(time, f.getPercentageOfSecondsToRemove()))
                .orElse(0D);
    }

    public long getFuelReductionMillis(long time) {
        if (fuel == null) return 0L;

        MinionFuelUpgrade fuelConfig = TheMinions.getApi()
                .getConfigFiles()
                .fuelUpgrades()
                .fuelUpgrades.getOrDefault(fuel.getId(), null);

        return Optional.ofNullable(fuelConfig)
                .map(f -> (long)MathUtil.getPercentage(time, f.getPercentageOfSecondsToRemove()))
                .orElse(0L);
    }

    //---------------------------------------------------
    /**
     *
     * @param time Current Time
     * @return Remaining seconds of both upgrades
     */
    public double getUpgradesReductionSeconds(double time) {
        return getUpgradeReductionSeconds(time, UpgradeSlot.FIRST_SLOT) + getUpgradeReductionSeconds(time, UpgradeSlot.SECOND_SLOT);
    }

    /**
     *
     * @param time Current Time
     * @return Remaining millis of both upgrades
     */
    public long getUpgradesReductionMillis(long time) {
        return getUpgradeReductionMillis(time, UpgradeSlot.FIRST_SLOT) + getUpgradeReductionMillis(time, UpgradeSlot.SECOND_SLOT);
    }

    /**
     *
     * @param time Current Time
     * @param slot Upgrade Slot
     * @return Remaining seconds of specific upgrade
     */
    public double getUpgradeReductionSeconds(double time, UpgradeSlot slot) {
        UpgradeEntity entity = slot.equals(UpgradeSlot.FIRST_SLOT) ? firstUpgrade : secondUpgrade;
        return getUpgradeSecondsReductionTime(time, entity);
    }

    /**
     *
     * @param time Current Time
     * @param slot Upgrade Slot
     * @return Remaining millis of specific upgrade
     */
    public long getUpgradeReductionMillis(long time, UpgradeSlot slot) {
        UpgradeEntity entity = slot.equals(UpgradeSlot.FIRST_SLOT) ? firstUpgrade : secondUpgrade;
        return getUpgradeMillisReductionTime(time, entity);
    }

    //---------------------------------------------------
    private long getUpgradeMillisReductionTime(long time, UpgradeEntity entity) {
        if (entity == null) return 0L;

        MinionUpgrade minionUpgrade = TheMinions.getApi()
                .getConfigFiles()
                .upgrades()
                .normalUpgrades.getOrDefault(entity.getId(), null);

        return Optional.ofNullable(minionUpgrade)
                .map(f -> (long)MathUtil.getPercentage(time, f.getPercentageOfSecondsToRemove()))
                .orElse(0L);
    }

    private double getUpgradeSecondsReductionTime(double time, UpgradeEntity entity) {
        if (entity == null) return 0D;

        MinionUpgrade fuelConfig = TheMinions.getApi()
                .getConfigFiles()
                .upgrades()
                .normalUpgrades.getOrDefault(entity.getId(), null);

        return Optional.ofNullable(fuelConfig)
                .map(f -> MathUtil.getPercentage(time, f.getPercentageOfSecondsToRemove()))
                .orElse(0D);
    }
}
