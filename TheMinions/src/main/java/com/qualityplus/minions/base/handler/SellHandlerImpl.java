package com.qualityplus.minions.base.handler;

import com.qualityplus.assistant.api.gui.FakeInventory;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.MathUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.handler.SellHandler;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.entity.MinionStorageState;
import com.qualityplus.minions.base.minions.entity.getter.LevelGetter;
import com.qualityplus.minions.base.minions.entity.state.MinionState;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionAutoShipping;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.AutomatedShippingEntity;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public final class SellHandlerImpl implements SellHandler, LevelGetter {
    private final MinionEntity minionEntity;
    private final Minion minion;


    @Override
    public void sellIfItsPossible() {
        FakeInventory fakeInventory = minionEntity.getState().getFakeInventory();

        UUID petUniqueId = minionEntity.getMinionUniqueId();

        MinionState state = minionEntity.getState();

        MinionStorageState storageState = state.getStorageState();

        if (storageState == null || !storageState.isHasFullStorage()) return;

        if (state.isSelling()) return;

        Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(petUniqueId);

        AutomatedShippingEntity automatedShipping = minionData.map(MinionData::getAutoSell).orElse(null);

        if (automatedShipping == null) return;

        ItemStack item = fakeInventory.removeOneFromLastItem();

        if (BukkitItemUtil.isNull(item)) return;

        state.setSelling(true);

        double sellPrice = minion.getMinionUpdateSettings().getNormalSellPrice();

        double percentage = Optional.ofNullable(TheMinions.getApi().getConfigFiles().getAutoSell().automatedShippingUpgrades.getOrDefault(automatedShipping.getId(), null))
                .map(MinionAutoShipping::getPercentageOfPriceToSell)
                .orElse(100D);

        automatedShipping.addHeldCoins(MathUtil.getPercentage(sellPrice, percentage));
        automatedShipping.addSoldItems(item.getAmount());

        minionData.ifPresent(minionData1 -> minionData1.setItemStackList(fakeInventory.getItems()));

        long eachTimer = minion.getTimer(getLevel()).getSeconds() * 20;

        Bukkit.getScheduler().runTaskLater(TheMinions.getInstance(), () -> state.setSelling(false), eachTimer);
    }

    @Override
    public UUID getMinionUniqueId() {
        return minionEntity.getMinionUniqueId();
    }
}
