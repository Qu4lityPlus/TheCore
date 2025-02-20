package com.qualityplus.minions.base.minions.entity;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.gui.FakeInventory;
import com.qualityplus.assistant.hologram.TheHologram;
import com.qualityplus.assistant.util.armorstand.ArmorStandUtil;
import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.assistant.util.time.HumanTime;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.minion.animation.MinionAnimation;
import com.qualityplus.minions.api.minion.animation.MinionAnimationContext;
import com.qualityplus.minions.base.config.Skins;
import com.qualityplus.minions.base.minions.entity.animation.MinionAnimationContextImpl;
import com.qualityplus.minions.base.minions.entity.tracker.MinionEntityTracker;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.animations.StartAnimation;
import com.qualityplus.minions.base.minions.entity.status.MinionStatus;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.SkinEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ArmorStandMinion<T> extends MinecraftMinion implements Listener {
    @Getter @Setter
    protected ArmorStand entity;
    @Getter @Setter
    protected TheHologram hologram;
    protected MinionAnimation currentAnimation;
    protected MinionAnimation startAnimation;

    protected ArmorStandMinion(final UUID minionUniqueId, final UUID owner, final Minion minion, final boolean loaded) {
        super(minionUniqueId, owner, minion, loaded);
    }

    @Override
    public void spawnMinionEntity() {
        this.state.setArmorStandLoaded(true);

        Optional.ofNullable(this.state.getSpawn())
                .ifPresent(this::createArmorStand);
    }

    @Override
    public void unloadMinionEntity() {
        this.state.setArmorStandLoaded(false);

        Optional.ofNullable(this.entity)
                .ifPresent(ArmorStand::remove);
        Optional.ofNullable(this.hologram)
                .ifPresent(TheHologram::remove);

        Optional.ofNullable(this.currentAnimation).ifPresent(MinionAnimation::cancel);
        Optional.ofNullable(this.startAnimation).ifPresent(MinionAnimation::cancel);

    }

    @Override
    public void spawnMinion(final Location location, final boolean loadMinionEntity) {
        MinionEntityTracker.registerNewEntity(this);

        this.state.setSpawn(location);

        if (loadMinionEntity) {
            spawnMinionEntity();
        }

        updateInventory();

        getData().ifPresent(minionData -> minionData.setLocation(location));
    }

    @Override
    public void unloadMinion(
            final DeSpawnReason deSpawnReason,
            final boolean saveDBAsync
    ) {
        MinionEntityTracker.unregisterEntity(this);

        unloadMinionEntity();

        final Optional<MinionData> data = getData();
        if (data.isEmpty()) {
            return;
        }

        if (!deSpawnReason.equals(DeSpawnReason.SERVER_TURNED_OFF)) {
            data.get().setLocation(null);
        }

        if (saveDBAsync) {
            Bukkit.getScheduler().runTaskAsynchronously(TheMinions.getInstance(), () -> data.get().save());
        } else {
            data.get().save();
        }
    }

    @Override
    public void tick() {
        if (!ArmorStandUtil.entityIsValid(entity)) {
            return;
        }

        this.handlers.getFuelHandler().removeFuel();

        if (timeHasHappened()) {
            return;
        }

        if (!state.isCanExecuteAnimation() || state.isSelling()) {
            return;
        }

        updateStatus();

        if (!state.getStatus().equals(MinionStatus.IDEAL_LAYOUT)) {
            sellIfItsPossible();
            return;
        }

        rotateToTarget();
    }


    @Override
    public void updateInventory() {
        final int level = getLevel();

        final int maxStorage = this.minion.getMaxStorage(level);

        this.state.setFakeInventory(TheAssistantPlugin.getAPI().getNms().getFakeInventory(null, maxStorage));

        this.state.getFakeInventory().setItems(getData().map(MinionData::getItemStackList).orElse(new HashMap<>()));

    }

    @Override
    public void updateSkin() {
        final Optional<MinionData> data = getData();

        if (data.isEmpty()) {
            return;
        }

        final int level = data.get().getLevel();

        final SkinEntity skinEntity = data.get().getSkinEntity();

        if (skinEntity == null) {
            this.minion.getSkin(level).ifPresent(skin -> skin.apply(this.entity));
            return;
        }

        final String id = data.get().getSkinEntity().getId();

        if (id == null) {
            return;
        }

        Skins.getSkin(id).ifPresent(skin -> skin.apply(this.entity));
    }


    @Override
    public Collection<ItemStack> pickUpAllItems() {
        Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(state.getUuid());

        FakeInventory fakeInventory = state.getFakeInventory();

        List<ItemStack> itemStacks = fakeInventory.getItems()
                .values()
                .stream().filter(Objects::nonNull)
                .map(ItemStack::clone)
                .collect(Collectors.toList());

        fakeInventory.removeItems();

        minionData.ifPresent(minionData1 -> minionData1.setItemStackList(Collections.emptyMap()));

        return itemStacks;
    }


    protected void addItemsToMinionInventory() {
        MinionStorageState storageState = state.getStorageState();

        FakeInventory fakeInventory = state.getFakeInventory();

        //Items added in last check will be removed
        Optional.ofNullable(storageState.getToRemove()).ifPresent(toRemove -> toRemove.forEach((amount, item) -> fakeInventory.removeItems(item, amount)));

        //Items added in last check will be added
        Optional.ofNullable(storageState.getToAdd()).ifPresent(fakeInventory.getInventory()::addItem);

        //Updates items in data
        getData().ifPresent(minionData1 -> minionData1.setItemStackList(fakeInventory.getItems()));
    }

    protected void sellIfItsPossible() {
        handlers.getSellHandler().sellIfItsPossible();
    }

    protected abstract void checkTargetAfterRotate(T target);

    protected abstract void executeWhenTargetIsNull(T target);

    protected abstract void executeWhenTargetIsNotNull(T toCheck);

    protected void resetPositionAndBreakingState() {
        this.state.setLastActionTime(System.currentTimeMillis());

        Bukkit.getScheduler().runTaskLater(TheMinions.getInstance(), () -> {
            if (!this.state.isArmorStandLoaded()) {
                return;
            }
            this.entity.setHeadPose(new EulerAngle(0, 0, 0));
            this.entity.teleport(this.state.getSpawn());
        }, 15);

        Bukkit.getScheduler().runTaskLater(TheMinions.getInstance(), () -> {
            this.state.setCanExecuteAnimation(true);
        }, 20L);
    }

    protected abstract void rotateToTarget();

    private boolean timeHasHappened() {
        int level = getLevel();

        HumanTime timer = minion.getTimer(level);

        if (state.getLastActionTime() == 0) {
            return true;
        }

        long time = timer.getEffectiveTime();

        long reduction = getData().map(data -> data.getFuelReductionMillis(time) + data.getUpgradesReductionMillis(time)).orElse(0L);

        Markable markable = new Markable(time - reduction, state.getLastActionTime());

        return markable.isMarked();
    }

    private void createArmorStand(final Location location) {
        TheMinions.getApi()
                .getMinionArmorStandService()
                .spawnArmorStand(this, location)
                .thenRun(this::updateSkin)
                .thenRun(this::executeStartAnimation);
    }


    private void executeStartAnimation() {
        final MinionAnimationContext context = MinionAnimationContextImpl.builder()
                .minionEntity(this)
                .build();
        this.startAnimation = new StartAnimation();
        this.startAnimation.executeAnimation(context).thenRun(() -> Bukkit.getScheduler().runTaskLater(TheMinions.getInstance(), () -> this.state.setCanExecuteAnimation(true), 20));
    }

    private void updateStatus() {
        final MinionStorageState storageState = this.handlers.getStorageHandler().getMinionStorageState();

        this.state.setStorageState(storageState);

        //TODO save state in database if its removed

        boolean hasInvalidLayout = state.isArmorStandLoaded() ? TheMinions.getApi().getMinionLayoutService().hasInvalidLayout(this) : false;
        boolean hasFullStorage = storageState.isHasFullStorage();

        state.setStatus(hasInvalidLayout ? MinionStatus.INVALID_LAYOUT : hasFullStorage ? MinionStatus.STORAGE_FULL : MinionStatus.IDEAL_LAYOUT);

        TheMinions.getApi().getMinionDisplayNameService().updateDisplayName(this);
    }

}
