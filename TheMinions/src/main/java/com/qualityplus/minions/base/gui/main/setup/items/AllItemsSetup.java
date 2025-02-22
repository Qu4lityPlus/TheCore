package com.qualityplus.minions.base.gui.main.setup.items;

import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.main.MainMinionGUIConfig;
import com.qualityplus.minions.base.gui.main.setup.ItemSetup;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.persistance.data.MinionData;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class AllItemsSetup implements ItemSetup {
    private final ItemSetup secondUpgradeSetup;
    private final ItemSetup firstUpgradeSetup;
    private final ItemSetup autoSellItemSetup;
    private final ItemSetup minionItemSetup;
    private final ItemSetup fuelItemSetup;
    private final ItemSetup skinItemSetup;

    public AllItemsSetup() {
        this.secondUpgradeSetup = new SecondUpgradeItemSetup();
        this.firstUpgradeSetup = new FirstUpgradeItemSetup();
        this.autoSellItemSetup = new AutoSellItemSetup();
        this.minionItemSetup = new MinionEggSetup();
        this.fuelItemSetup = new FuelItemSetup();
        this.skinItemSetup = new SkinItemSetup();

    }

    @Override
    public void setItem(Inventory inventory, Box box, MainMinionGUIConfig config, MinionEntity minionEntity) {
        secondUpgradeSetup.setItem(inventory, box, config, minionEntity);
        autoSellItemSetup.setItem(inventory, box, config, minionEntity);
        firstUpgradeSetup.setItem(inventory, box, config, minionEntity);
        minionItemSetup.setItem(inventory, box, config, minionEntity);
        fuelItemSetup.setItem(inventory, box, config, minionEntity);
        skinItemSetup.setItem(inventory, box, config, minionEntity);

        Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());
        Optional<Minion> minion = minionData.map(MinionData::getMinionId).filter(Objects::nonNull).map(Minions::getByID);

        int level = minionData.map(MinionData::getLevel).orElse(1);

        int count = 64;
        //64
        int maxStorage = minion.map(minion1 -> minion1.getMaxStorageForInv(level)).orElse(0);

        int slotCount = 0;

        Map<Integer, ItemStack> itemStackMap = minionEntity.getState().getFakeInventory().getItems();

        for (Integer slot : config.getStorageSlots()) {
            //64
            if (maxStorage >= count) {

                try {
                    ItemStack itemStack = itemStackMap.getOrDefault(slotCount, null);

                    inventory.setItem(slot, itemStack);
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("Error: " + slotCount);
                }
                count+=64;
                slotCount++;
                continue;
            }

            inventory.setItem(slot, ItemStackUtils.makeItem(config.getStorageSlotLocked()));
            count+=64;
        }
    }
}
