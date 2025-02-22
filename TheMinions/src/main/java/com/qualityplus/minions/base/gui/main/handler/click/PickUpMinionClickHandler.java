package com.qualityplus.minions.base.gui.main.handler.click;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.main.handler.ClickHandler;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionAutoShipping;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionFuelUpgrade;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionUpgrade;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.UserData;
import com.qualityplus.minions.persistance.data.upgrade.AutomatedShippingEntity;
import com.qualityplus.minions.persistance.data.upgrade.FuelEntity;
import com.qualityplus.minions.persistance.data.upgrade.UpgradeEntity;
import com.qualityplus.minions.util.MinionEggUtil;
import com.qualityplus.minions.util.MinionPlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class PickUpMinionClickHandler implements ClickHandler {
    @Override
    public void handle(InventoryClickEvent event, MinionEntity minionEntity, Box box) {
        Player player = (Player) event.getWhoClicked();

        player.closeInventory();

        deSpawn(player, minionEntity, box);

        dropMinionItems(player, minionEntity, box);

        minionEntity.unloadMinion(MinionEntity.DeSpawnReason.PLAYER_DE_SPAWN_MINION, true);

        MinionEggUtil
                .createFromExistent(box.files().config().minionEggItem, minionEntity.getMinionUniqueId())
                .ifPresent(player.getInventory()::addItem);

    }

    private void deSpawn(Player player, MinionEntity entity, Box box) {

        Optional<UserData> data = TheMinions.getApi().getUserService().getData(player.getUniqueId());

        int minionsAmount = MinionPlayerUtil.getMinionsAmount(player);
        int placedAmount = data.map(UserData::getMinionsToPlace).orElse(0);

        List<IPlaceholder> placeholders = PlaceholderBuilder.create(
                new Placeholder("minions_max_amount_to_place", minionsAmount),
                new Placeholder("minions_placed_amount", Math.max(placedAmount - 1, 0))).get();

        data.ifPresent(userData -> userData.removeMinion(entity.getMinionUniqueId()));

        player.sendMessage(StringUtils.processMulti(box.files().messages().minionMessages.pickUpMinion, placeholders));
    }

    private void dropMinionItems(Player player, MinionEntity minionEntity, Box box) {
        minionEntity.pickUpAllItems()
                .stream()
                .filter(Objects::nonNull)
                .forEach(item -> player.getWorld().dropItem(player.getLocation(), item));

        final Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());

        final AutomatedShippingEntity autoSellEntity = minionData
                .map(MinionData::getAutoSell)
                .orElse(null);

        final UpgradeEntity upgrade = minionData
                .map(MinionData::getFirstUpgrade)
                .orElse(null);

        final UpgradeEntity secondUpgrade = minionData
                .map(MinionData::getSecondUpgrade)
                .orElse(null);

        final FuelEntity fuel = minionData
                .map(MinionData::getFuel)
                .orElse(null);

        if (autoSellEntity != null) {
            final MinionAutoShipping automatedShipping = box.files().getAutoSell().automatedShippingUpgrades.getOrDefault(autoSellEntity.getId(), null);

            if (automatedShipping != null) {
                player.getWorld().dropItem(player.getLocation(), automatedShipping.getItemStack(autoSellEntity.getSoldItems(), autoSellEntity.getHeldCoins()));
            }
            minionData.get().setAutoSell(null);
        }

        if (upgrade != null) {
            final MinionUpgrade minionUpgrade = box.files().upgrades().getById(upgrade.getId());

            if (minionUpgrade != null) {
                player.getWorld().dropItem(player.getLocation(), minionUpgrade.getItemStack());
            }
            minionData.get().setFirstUpgrade(null);
        }

        if (secondUpgrade != null) {
            final MinionUpgrade minionUpgrade = box.files().upgrades().getById(secondUpgrade.getId());

            if (minionUpgrade != null) {
                player.getWorld().dropItem(player.getLocation(), minionUpgrade.getItemStack());
            }
            minionData.get().setSecondUpgrade(null);
        }

        if (fuel != null) {
            final MinionFuelUpgrade minionUpgrade = box.files().fuelUpgrades().fuelUpgrades.getOrDefault(fuel.getId(), null);

            if (minionUpgrade != null) {
                player.getWorld().dropItem(player.getLocation(), minionUpgrade.getItemStack(fuel.getMarkable().getDelay(), fuel.getMarkable().getLastMarked()));
            }
            minionData.get().setFuel(null);
        }

    }
}
