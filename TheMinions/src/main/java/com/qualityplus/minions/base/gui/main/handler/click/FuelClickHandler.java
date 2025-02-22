package com.qualityplus.minions.base.gui.main.handler.click;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.main.handler.ClickHandler;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionFuelUpgrade;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.FuelEntity;
import com.qualityplus.minions.util.MinionUpgradeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class FuelClickHandler implements ClickHandler {
    @Override
    public void handle(InventoryClickEvent event, MinionEntity minionEntity, Box box) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (event.isShiftClick()) return;

        Optional<MinionData> data = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());
        FuelEntity entity = data.map(MinionData::getFuel).orElse(null);

        ItemStack cursor = BukkitItemUtil.cloneOrNull(event.getCursor());
        ItemStack oldToGive = getTaken(box, entity, minionEntity);
        boolean cursorIsEntity = MinionUpgradeUtil.isFuel(cursor);

        if (cursorIsEntity) {
            FuelEntity newFuel = getEntityFromItem(box, entity, data, cursor);

            data.ifPresent(d -> d.setFuel(newFuel));

            player.setItemOnCursor(oldToGive);

        } else {
            if (entity == null) return;

            if (BukkitItemUtil.isNull(cursor)) {
                Optional.ofNullable(oldToGive).ifPresent(player::setItemOnCursor);
            } else
                Optional.ofNullable(oldToGive).ifPresent(item -> BukkitItemUtil.dropItem(player, item));

            data.ifPresent(MinionData::removeFuel);
        }
    }

    @SuppressWarnings("all")
    private FuelEntity getEntityFromItem(Box box, FuelEntity fuelEntity, Optional<MinionData> minionData, @Nullable ItemStack itemStack) {
        FuelEntity modified = MinionUpgradeUtil.getFuelFromItem(itemStack.clone());

        MinionFuelUpgrade fuel = box.files().fuelUpgrades().fuelUpgrades.getOrDefault(modified.getId(), null);

        Markable current = modified.getMarkable();

        if (current.getDelay() == -1 & current.getLastMarked() == -1)
            modified.setMarkable(new Markable(System.currentTimeMillis(), fuel.getTimer().getEffectiveTime()));
        else
            modified.setMarkable(new Markable(current.getDelay(), current.getLastMarked()));

        minionData.ifPresent(data -> data.setFuel(modified));

        return modified;
    }

    @SuppressWarnings("all")
    private ItemStack getTaken(Box box, FuelEntity fuelEntity, MinionEntity entity) {
        if (fuelEntity == null) return null;

        MinionFuelUpgrade fuel = box.files().fuelUpgrades().fuelUpgrades.getOrDefault(fuelEntity.getId(), null);

        if (fuel == null) return null;

        Markable markable = fuelEntity.getMarkable();

        return fuel.getItemStack(markable.getDelay(), markable.getLastMarked());
    }

}
