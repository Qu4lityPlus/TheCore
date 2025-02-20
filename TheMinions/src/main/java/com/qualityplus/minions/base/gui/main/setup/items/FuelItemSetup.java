package com.qualityplus.minions.base.gui.main.setup.items;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.time.RemainingTime;
import com.qualityplus.assistant.util.time.TimeUtils;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.config.Messages;
import com.qualityplus.minions.base.gui.main.MainMinionGUIConfig;
import com.qualityplus.minions.base.gui.main.setup.ItemSetup;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionFuelUpgrade;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.FuelEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public final class FuelItemSetup implements ItemSetup {
    @Override
    public void setItem(Inventory inventory, Box box, MainMinionGUIConfig config, MinionEntity minionEntity) {
        Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());

        FuelEntity fuelEntity = minionData
                .map(MinionData::getFuel)
                .orElse(null);

        if (fuelEntity == null)
            inventory.setItem(config.getMinionFuelItem().getSlot(), ItemStackUtils.makeItem(config.getMinionFuelItem()));
        else {
            MinionFuelUpgrade fuel = box.files().fuelUpgrades().fuelUpgrades.getOrDefault(fuelEntity.getId(), null);

            if (fuel == null) return;

            ItemStack itemStack = fuel.getItemStack(fuelEntity.getMarkable().getDelay(), fuelEntity.getMarkable().getLastMarked());

            String displayName = BukkitItemUtil.getName(itemStack);
            List<String> lore = BukkitItemUtil.getItemLore(itemStack);

            inventory.setItem(config.getFuelPlacedItem().getSlot(), ItemStackUtils.makeItem(
                    config.getFuelPlacedItem(),
                    PlaceholderBuilder.create(
                            new Placeholder("minion_upgrade_item_display_name", displayName),
                            new Placeholder("minion_upgrade_item_lore", lore),
                            new Placeholder("minion_upgrade_remaining_time", getRemainingTime(fuelEntity))
                    ).get(),
                    itemStack
            ));
        }
    }

    private String getRemainingTime(FuelEntity fuelEntity) {
        Messages messages = TheMinions.getApi().getConfigFiles().messages();

        RemainingTime time = fuelEntity.getMarkable().getRemainingTime();

        return TimeUtils.getParsedTime(
                time,
                messages.minionMessages.fuelEndsInFormat,
                messages.minionMessages.days,
                messages.minionMessages.hours,
                messages.minionMessages.minutes,
                messages.minionMessages.seconds,
                messages.minionMessages.noTimeFormat,
                messages.minionMessages.showNoTimeSymbol
        );
    }
}
