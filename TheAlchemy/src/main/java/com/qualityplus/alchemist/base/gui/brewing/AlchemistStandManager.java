package com.qualityplus.alchemist.base.gui.brewing;

import com.qualityplus.alchemist.api.box.Box;
import com.qualityplus.alchemist.base.event.AlchemistBrewEvent;
import com.qualityplus.alchemist.base.recipes.BrewingRecipe;
import com.qualityplus.alchemist.base.stand.StandEffect;
import com.qualityplus.alchemist.base.stand.StandEffects;
import com.qualityplus.alchemist.util.AlchemistFinderUtil;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.assistant.util.time.RemainingTime;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Alchemist stand manager
 */
public final class AlchemistStandManager {
    private final Map<UUID, Integer> tasksMap = new HashMap<>();
    private final Inventory inventory;
    @Getter
    private final Location location;
    private final Box box;
    private Markable time;
    private int effect;

    /**
     *
     * @param inventory {@link Inventory}
     * @param location  {@link Location}
     * @param box       {@link Box}
     */
    public AlchemistStandManager(final Inventory inventory, final Location location, final Box box) {
        this.inventory = inventory;
        this.location = location;
        this.box = box;
        this.time = new Markable(0, 0);
        this.effect = 0;
    }

    /**
     * Check stand gui for player
     *
     * @param player {@link Player}
     */
    public void check(final Player player) {
        Bukkit.getScheduler().runTaskLater(this.box.getPlugin(), () -> {
            final BrewingRecipe recipe = AlchemistFinderUtil.getAlchemyRecipe(this.inventory, this.box.getFiles().inventories().getStandGUIConfig(), player);

            if (recipe == null) {
                if (this.time.remainingTime() > 0) {
                    resetDecoration();
                }
            } else {
                if (this.time.remainingTime() <= 0) {
                    manageTime(recipe);
                    startBrewing();
                    checkIfEnd(player, recipe);
                }
            }
            setItemsInBrewingStand();
        }, 0L);
    }

    /**
     * Set items in inventory
     */
    public void setItemsInInventory() {
        final BrewingStand brewingStand = getBrewingStand();

        if (brewingStand == null) {
            return;
        }

        final Map<Integer, Integer> relationSlots = getSimilarSlots();

        relationSlots.forEach((key, value) -> this.inventory.setItem(value, brewingStand.getInventory().getItem(key)));

    }

    private void setItemsInBrewingStand() {
        final BrewingStand brewingStand = getBrewingStand();

        if (brewingStand == null) {
            return;
        }

        getSimilarSlots().forEach((key, value) -> {
            final ItemStack item = this.inventory.getItem(value);

            brewingStand.getInventory().setItem(key, item);
        });
    }

    private Map<Integer, Integer> getSimilarSlots() {
        final AlchemistStandGUIConfig config = this.box.getFiles().inventories().getStandGUIConfig();

        return new HashMap<>() {
            {
                int i = 0;
                for (final Integer slot : config.getInputSlots()) {
                    put(i, slot);
                    i++;
                }
                put(i, config.getFuelSlot());
            }
        };
    }

    private void startBrewing() {
        final UUID uuid = UUID.randomUUID();

        final StandEffects standEffects = this.box.getFiles().inventories().getStandGUIConfig().getStandEffects();

        this.tasksMap.put(uuid, Bukkit.getScheduler().runTaskTimer(this.box.getPlugin(), () -> {
            if (this.time.remainingTime() > 0) {
                standEffects.getEffectList()
                        .get(this.effect)
                        .forEach(this::setEffectInInventory);

                this.effect = standEffects.getEffectList().containsKey(this.effect + 1) ? this.effect + 1 : 0;
            } else {
                cancelTask(uuid);
            }

        }, 0L, 3L).getTaskId());
    }

    private void setEffectInInventory(final StandEffect effect) {
        final List<IPlaceholder> placeholders = new Placeholder("remaining_time", remainingTime()).alone();

        this.inventory.setItem(effect.getSlot(), ItemStackUtils.makeItem(effect.getItem(), placeholders));
    }

    private void checkIfEnd(final Player player, final BrewingRecipe recipe) {
        final UUID uuid = UUID.randomUUID();

        this.tasksMap.put(uuid, Bukkit.getScheduler().runTaskTimer(this.box.getPlugin(), () -> {
            if (this.time.remainingTime() < 0) {
                finish(player, recipe);
                resetDecoration();
                cancelTask(uuid);
            }
        }, 0L, 0L).getTaskId());
    }


    private String remainingTime() {
        final RemainingTime toParse = this.time.getRemainingTime();

        return toParse.getSeconds() + "." + toParse.getMillis();
    }

    private void manageTime(final BrewingRecipe recipe) {
        this.time = new Markable(recipe.getTimer().getEffectiveTime(), System.currentTimeMillis());
    }

    private void resetDecoration() {
        this.tasksMap.keySet().forEach(this::cancelTask);

        this.time = new Markable(0, 0);
        this.effect = 0;

        InventoryUtils.fillInventory(this.inventory, this.box.getFiles().inventories().getStandGUIConfig().getBackground());
    }

    private void finish(final Player player, final BrewingRecipe recipe) {
        final AlchemistBrewEvent event = new AlchemistBrewEvent(player, recipe);

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        this.box.getFiles().inventories().getStandGUIConfig().getInputSlots()
                .stream()
                .filter(slot -> !BukkitItemUtil.isNull(this.inventory.getItem(slot)))
                .forEach(i -> {
                    this.inventory.setItem(i, null);

                    Bukkit.getScheduler().runTask(this.box.getPlugin(), () -> this.inventory.setItem(i, recipe.getOutPut()));
                });

        final int slot = this.box.getFiles().inventories().getStandGUIConfig().getFuelSlot();

        this.inventory.setItem(slot, getItemToPut(recipe, this.inventory, slot));

        Bukkit.getScheduler().runTaskLater(this.box.getPlugin(), this::setItemsInBrewingStand, 1);
    }

    private ItemStack getItemToPut(final BrewingRecipe recipe, final Inventory inventory, final int slot) {
        final ItemStack itemStack = inventory.getItem(slot);

        if (BukkitItemUtil.isNull(itemStack)) {
            return null;
        }

        final int newAmount = itemStack.getAmount() - Optional.ofNullable(recipe.getFuel())
                .map(ItemStack::getAmount)
                .orElse(1);

        if (newAmount <= 0) {
            return null;
        }

        itemStack.setAmount(newAmount);

        return itemStack;
    }

    private void cancelTask(final UUID uuid) {
        Optional.ofNullable(this.tasksMap.getOrDefault(uuid, null)).ifPresent(Bukkit.getScheduler()::cancelTask);
    }

    /**
     * Retrieves manager's brewing stand
     *
     * @return {@link BrewingRecipe}
     */
    public BrewingStand getBrewingStand() {
        if (this.location == null) {
            return null;
        }

        final Block bl = this.location.getBlock();

        if (!bl.getType().equals(Material.BREWING_STAND)) {
            return null;
        }
        return (BrewingStand) bl.getState();
    }
}
