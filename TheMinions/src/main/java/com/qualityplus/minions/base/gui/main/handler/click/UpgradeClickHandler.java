package com.qualityplus.minions.base.gui.main.handler.click;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.main.MainMinionGUIConfig;
import com.qualityplus.minions.base.gui.main.handler.ClickHandler;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionUpgrade;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.UpgradeEntity;
import com.qualityplus.minions.util.MinionUpgradeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class UpgradeClickHandler implements ClickHandler {

    @Override
    public void handle(InventoryClickEvent event, MinionEntity minionEntity, Box box) {
        UpgradeSlot slot = getSlot(event, box);

        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (event.isShiftClick()) return;

        Optional<MinionData> data = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());
        UpgradeEntity entity = slot.equals(UpgradeSlot.FIRST_SLOT) ? data
                .map(MinionData::getFirstUpgrade)
                .orElse(null) : data
                .map(MinionData::getSecondUpgrade)
                .orElse(null);

        ItemStack cursor = BukkitItemUtil.cloneOrNull(event.getCursor());
        ItemStack oldToGive = getTaken(box, entity, minionEntity);
        boolean cursorIsEntity = MinionUpgradeUtil.isUpgrade(cursor);

        if (cursorIsEntity) {
            UpgradeEntity newUpgrade = getEntityFromItem(box, entity, data, cursor, slot);
            data.ifPresent(d -> setUpgrade(d, slot, newUpgrade));

            player.setItemOnCursor(oldToGive);

        } else {
            if (entity == null) return;

            if (BukkitItemUtil.isNull(cursor)) {
                Optional.ofNullable(oldToGive).ifPresent(player::setItemOnCursor);
            } else
                Optional.ofNullable(oldToGive).ifPresent(item -> BukkitItemUtil.dropItem(player, item));

            data.ifPresent(d -> d.removeUpgrade(slot));
        }
    }

    @SuppressWarnings("all")
    private UpgradeEntity getEntityFromItem(Box box, UpgradeEntity upgradeEntity, Optional<MinionData> minionData, @Nullable ItemStack itemStack, UpgradeSlot slot) {
        UpgradeEntity modified = MinionUpgradeUtil.getUpgradeFromItem(itemStack.clone());

        MinionUpgrade upgrade = box.files().upgrades().normalUpgrades.getOrDefault(modified.getId(), null);

        minionData.ifPresent(data -> setUpgrade(data, slot, modified));

        return modified;
    }

    private void setUpgrade(MinionData data, UpgradeSlot slot, UpgradeEntity modified) {
        if (slot == UpgradeSlot.FIRST_SLOT)
            data.setFirstUpgrade(modified);
        else
            data.setSecondUpgrade(modified);
    }


    @SuppressWarnings("all")
    private ItemStack getTaken(Box box, UpgradeEntity upgradeEntity, MinionEntity entity) {
        if (upgradeEntity == null) return null;

        MinionUpgrade upgrade = box.files().upgrades().normalUpgrades.getOrDefault(upgradeEntity.getId(), null);

        if (upgrade == null) return null;


        return upgrade.getItemStack();
    }

    private UpgradeSlot getSlot(InventoryClickEvent event, Box box) {
        MainMinionGUIConfig config = box.files().inventories().getMinionGUIConfig();

        int slot = event.getSlot();

        return slot == config.getSecondUpgradeEmptyItem().getSlot() ? UpgradeSlot.SECOND_SLOT : UpgradeSlot.FIRST_SLOT;
    }

}
