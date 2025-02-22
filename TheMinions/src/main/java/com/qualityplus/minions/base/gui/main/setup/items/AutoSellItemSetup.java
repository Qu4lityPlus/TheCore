package com.qualityplus.minions.base.gui.main.setup.items;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.main.MainMinionGUIConfig;
import com.qualityplus.minions.base.gui.main.setup.ItemSetup;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionAutoShipping;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.AutomatedShippingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public final class AutoSellItemSetup implements ItemSetup {
    @Override
    public void setItem(Inventory inventory, Box box, MainMinionGUIConfig config, MinionEntity minionEntity) {
        Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());

        AutomatedShippingEntity autoSellEntity = minionData
                .map(MinionData::getAutoSell)
                .orElse(null);

        if (autoSellEntity == null)
            inventory.setItem(config.getMinionAutomatedShipping().getSlot(), ItemStackUtils.makeItem(config.getMinionAutomatedShipping()));
        else {
            MinionAutoShipping autoSell = box.files().getAutoSell().automatedShippingUpgrades.getOrDefault(autoSellEntity.getId(), null);

            if (autoSell == null) return;

            ItemStack itemStack = autoSell.getItemStack(autoSellEntity.getSoldItems(), autoSellEntity.getHeldCoins());

            String displayName = BukkitItemUtil.getName(itemStack);
            List<String> lore = BukkitItemUtil.getItemLore(itemStack);

            inventory.setItem(config.getAutomatedShippingPlacedItem().getSlot(), ItemStackUtils.makeItem(
                    config.getAutomatedShippingPlacedItem(),
                    PlaceholderBuilder.create(
                            new Placeholder("minion_upgrade_item_display_name", displayName),
                            new Placeholder("minion_upgrade_item_lore", lore),
                            new Placeholder("minion_upgrade_sold_items", autoSellEntity.getSoldItems()),
                            new Placeholder("minion_upgrade_held_coins", autoSellEntity.getHeldCoins())
                    ).get(),
                    itemStack
            ));
        }
    }
}
