package com.qualityplus.minions.base.gui.main.handler.click;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.main.handler.ClickHandler;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionAutoShipping;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.AutomatedShippingEntity;
import com.qualityplus.minions.util.MinionUpgradeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class AutoShipClickHandler implements ClickHandler {
    @Override
    public void handle(InventoryClickEvent event, MinionEntity minionEntity, Box box) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (event.isShiftClick()) return;

        Optional<MinionData> data = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());
        AutomatedShippingEntity entity = data.map(MinionData::getAutoSell).orElse(null);

        ItemStack cursor = BukkitItemUtil.cloneOrNull(event.getCursor());
        ItemStack oldToGive = getTaken(box, entity, minionEntity);
        boolean cursorIsEntity = MinionUpgradeUtil.isAutoShip(cursor);

        if (cursorIsEntity) {
            AutomatedShippingEntity newAuto = getEntityFromItem(box, entity, data, cursor);

            data.ifPresent(d -> d.setAutoSell(newAuto));

            player.setItemOnCursor(oldToGive);

        } else {
            if (entity == null) return;

            if (BukkitItemUtil.isNull(cursor)) {
                Optional.ofNullable(oldToGive).ifPresent(player::setItemOnCursor);
            } else
                Optional.ofNullable(oldToGive).ifPresent(item -> BukkitItemUtil.dropItem(player, item));

            data.ifPresent(MinionData::removeAutoShip);
        }
    }

    @SuppressWarnings("all")
    private AutomatedShippingEntity getEntityFromItem(Box box, AutomatedShippingEntity entity, Optional<MinionData> minionData, @Nullable ItemStack itemStack) {
        AutomatedShippingEntity modified = MinionUpgradeUtil.getAutoShipFromItem(itemStack.clone());

        minionData.ifPresent(data -> data.setAutoSell(modified));

        return modified;
    }

    @SuppressWarnings("all")
    private ItemStack getTaken(Box box, AutomatedShippingEntity shippingEntity, MinionEntity entity) {
        if (shippingEntity == null) return null;

        MinionAutoShipping automatedShipping = box.files().getAutoSell().automatedShippingUpgrades.getOrDefault(shippingEntity.getId(), null);

        if (automatedShipping == null) return null;

        return automatedShipping.getItemStack(shippingEntity.getSoldItems(), shippingEntity.getHeldCoins());
    }
}
