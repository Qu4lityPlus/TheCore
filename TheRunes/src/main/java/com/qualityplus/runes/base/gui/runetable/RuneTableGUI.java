package com.qualityplus.runes.base.gui.runetable;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.session.RuneSession;
import com.qualityplus.runes.api.session.RuneSession.SessionResult;
import com.qualityplus.runes.base.gui.ClickHandler;
import com.qualityplus.runes.base.gui.RuneGUI;
import com.qualityplus.runes.base.gui.derune.RemoveRuneGUI;
import com.qualityplus.runes.base.gui.runetable.effect.EffectHandler;
import com.qualityplus.runes.base.gui.runetable.effect.RuneTableEffectHandler;
import com.qualityplus.runes.base.gui.runetable.handler.MainClickHandler;
import com.qualityplus.runes.base.gui.runetable.handler.NormalClickHandler;
import com.qualityplus.runes.base.gui.runetable.handler.ShiftClickHandler;
import com.qualityplus.runes.base.rune.RuneLevel;
import com.qualityplus.runes.base.session.RemoveSessionImpl;
import com.qualityplus.runes.util.RuneFinderUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class RuneTableGUI extends RuneGUI {
    private final EffectHandler<RuneTableGUI> effectHandler;
    private @Getter final RuneTableGUIConfig config;
    private @Setter boolean giveItem = true;
    private final ClickHandler handler;
    private final RuneSession session;

    public RuneTableGUI(Box box, RuneSession session) {
        super(box.files().inventories().runeTableGUIConfig, box);

        this.effectHandler = new RuneTableEffectHandler(session, box);
        this.handler = new MainClickHandler(
                new NormalClickHandler(box, this, session, effectHandler),
                new ShiftClickHandler(box, this, session));
        this.config = box.files().inventories().runeTableGUIConfig;
        this.session = session;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        SessionResult answer = RuneFinderUtil.getAnswer(session);

        //Izquierda
        if (session.bothItemsAreSet() || answer.equals(SessionResult.ONLY_ITEM_TO_UPGRADE))
            config.getToUpgradeFilledSlots().forEach(slot -> inventory.setItem(slot, ItemStackUtils.makeItem(config.getToUpgradeFilledItem())));

        //Derecha
        if (session.bothItemsAreSet() || answer.equals(SessionResult.ONLY_ITEM_TO_SACRIFICE))
            config.getToSacrificeFilledSlots().forEach(slot -> inventory.setItem(slot, ItemStackUtils.makeItem(config.getToSacrificeFilledItem())));

        //Items de abajo
        if (session.bothItemsAreSet()) {
            ItemStack newItem = RuneFinderUtil.getFinalItem(box, session);
            //New Item with rune combined
            inventory.setItem(config.getCombinedFilledItem().getSlot(), ItemStackUtils.makeItem(config.getCombinedFilledItem(), getPlaceholders(newItem), newItem));

            config.getReadyToCombineSlots().forEach(slot -> inventory.setItem(slot, ItemStackUtils.makeItem(config.getReadyToCombineItem())));
            //If there are two runes
            if (session.getSessionResult().equals(SessionResult.BOTH_RUNES_SET)) {

                int newLevel = session.getRuneInstance().getLevel() * 2;

                double chance = session.getRuneInstance().getRune().getOptRuneLevel(newLevel).map(RuneLevel::getSuccessChance).orElse(0D);

                setItem(config.getClickToCombineRunesItem(), Arrays.asList(
                        new Placeholder("rune_succeed_chance", chance),
                        new Placeholder("rune_fail_chance", 100 - chance)
                ));
            } else {
                setItem(config.getClickToCombineItemAndRuneItem());
            }

        }

        if (!BukkitItemUtil.isNull(session.getResult()))
            inventory.setItem(config.getCombinedFilledItem().getSlot(), ItemStackUtils.makeItem(config.getCombinedFilledItem(), getPlaceholders(session.getResult()), session.getResult()));


        if (answer.isError())
            setItem(config.getCombinedErrorItem(), Collections.singletonList(new Placeholder("rune_error", getErrorPlaceholder(answer))));


        if (!BukkitItemUtil.isNull(session.getItemToSacrifice()))
            inventory.setItem(config.getToSacrificeSlot(), session.getItemToSacrifice());

        if (!BukkitItemUtil.isNull(session.getItemToUpgrade()))
            inventory.setItem(config.getToUpgradeSlot(), session.getItemToUpgrade());


        setItem(config.getCloseGUI());
        setItem(config.getRemovalItem());

        return inventory;
    }

    private List<IPlaceholder> getPlaceholders(ItemStack itemStack) {
        return Arrays.asList(
                new Placeholder("rune_result_item_displayname", BukkitItemUtil.getName(itemStack)),
                new Placeholder("rune_result_item_lore", BukkitItemUtil.getItemLore(itemStack))
        );
    }

    private List<String> getErrorPlaceholder(SessionResult answer) {
        switch (answer) {
            case MUST_BE_SAME_TIER:
                return box.files().messages().runePlaceholders.mustBeSameTier;
            case MUST_BE_SAME_TYPE:
                return box.files().messages().runePlaceholders.mustBeSameType;
            case CANNOT_ADD_THAT_RUNE:
                return box.files().messages().runePlaceholders.youCannotAddThatRuneToThatItem;
            case NO_RUNE_CRAFTING_LEVEL:
                return box.files().messages().runePlaceholders.noRuneCraftingLevel;
            case INCOMPATIBLE_RUNE_APPLY:
                return box.files().messages().runePlaceholders.youCannotAddThatRuneToThatItem;
            default:
                return box.files().messages().runePlaceholders.youCannotCombineThoseItems;
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (giveItem && !session.isFusing()) {
            giveItems(player);
        }

        if (session.isFusing()) {
            effectHandler.setHasBeenClosed(true);

            Optional.ofNullable(effectHandler.getResult(player, session))
                    .filter(itemStack -> !BukkitItemUtil.isNull(itemStack))
                    .ifPresent(itemStack -> player.getInventory().addItem(itemStack));
        }
    }

    private void giveItems(Player player) {
        Optional.ofNullable(session.getItemToSacrifice()).ifPresent(itemStack -> player.getInventory().addItem(itemStack));
        Optional.ofNullable(session.getItemToUpgrade()).ifPresent(itemStack -> player.getInventory().addItem(itemStack));
        Optional.ofNullable(session.getResult()).ifPresent(itemStack -> player.getInventory().addItem(itemStack));

    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        int slot = e.getSlot();

        if (getTarget(e).equals(ClickTarget.INSIDE)) {

            if (isItem(slot, config.getCloseGUI())) {
                e.setCancelled(true);
                player.closeInventory();
            } else if (isItem(slot, config.getRemovalItem())) {
                player.openInventory(new RemoveRuneGUI(box, new RemoveSessionImpl(null, false)).getInventory());
            } else if (slot == config.getToUpgradeSlot() || slot == config.getToSacrificeSlot() || slot == config.getClickToCombineRunesItem().getSlot() || slot == config.getCombinedFilledItem().getSlot()) {
                handler.handle(e);
            } else {
                e.setCancelled(true);
            }
        } else {
            handler.handleOutSide(e);
        }


    }
}
