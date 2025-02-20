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
import com.qualityplus.minions.base.minions.minion.upgrade.MinionUpgrade;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.UpgradeEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public final class FirstUpgradeItemSetup implements ItemSetup {
    @Override
    public void setItem(Inventory inventory, Box box, MainMinionGUIConfig config, MinionEntity minionEntity) {
        Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());

        UpgradeEntity upgradeEntity = minionData
                .map(MinionData::getFirstUpgrade)
                .orElse(null);

        if (upgradeEntity == null)
            inventory.setItem(config.getFirstUpgradeEmptyItem().getSlot(), ItemStackUtils.makeItem(config.getFirstUpgradeEmptyItem()));
        else {
            MinionUpgrade fuel = box.files().upgrades().normalUpgrades.getOrDefault(upgradeEntity.getId(), null);

            if (fuel == null) return;

            ItemStack itemStack = fuel.getItemStack();

            String displayName = BukkitItemUtil.getName(itemStack);
            List<String> lore = BukkitItemUtil.getItemLore(itemStack);

            inventory.setItem(config.getFirstUpgradePlacedItem().getSlot(), ItemStackUtils.makeItem(
                    config.getFirstUpgradePlacedItem(),
                    PlaceholderBuilder.create(
                            new Placeholder("minion_upgrade_item_display_name", displayName),
                            new Placeholder("minion_upgrade_item_lore", lore)
                    ).get(),
                    itemStack
            ));
        }
    }
}
