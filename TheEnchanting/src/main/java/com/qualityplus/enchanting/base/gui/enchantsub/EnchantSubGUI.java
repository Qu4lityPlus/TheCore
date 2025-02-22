package com.qualityplus.enchanting.base.gui.enchantsub;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.enchanting.TheEnchanting;
import com.qualityplus.enchanting.api.box.Box;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.api.session.EnchantmentSession;
import com.qualityplus.enchanting.base.event.TheEnchantingEnchantEvent;
import com.qualityplus.enchanting.base.gui.EnchantingGUI;
import com.qualityplus.enchanting.base.gui.enchantmain.EnchantMainGUI;
import com.qualityplus.enchanting.base.session.EnchantmentSessionImpl;
import com.qualityplus.enchanting.util.EnchantingFinderUtil;
import com.qualityplus.enchanting.util.EnchantingPlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class EnchantSubGUI extends EnchantingGUI {
    private final Map<Integer, Integer> enchantments = new HashMap<>();
    private final ICoreEnchantment enchantment;
    private final EnchantSubGUIConfig config;
    private boolean giveItem = true;
    private final ItemStack item;
    private final int bookShelf;
    private final UUID uuid;

    public EnchantSubGUI(final Box box, final int bookShelf, final ItemStack item, final ICoreEnchantment enchantment, final UUID uuid) {
        super(box.files().inventories().levelEnchantGUI, box);

        this.config = box.files().inventories().levelEnchantGUI;
        this.enchantment = enchantment;
        this.bookShelf = bookShelf;
        this.uuid = uuid;
        this.item = item;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        final List<Integer> slots = config.getEnchantmentSlotsMap().getOrDefault(enchantment.getMaxLevel(), Collections.emptyList());

        int level = enchantment.getStartLevel();

        //enchanting_enchant_level_to_enchant_roman
        for (final Integer slot : slots) {
            List<IPlaceholder> placeholders = EnchantingPlaceholderUtil.getEnchantPlaceholders(enchantment, level);

            inventory.setItem(slot, ItemStackUtils.makeItem(config.getEnchantItem(), PlaceholderBuilder.create(placeholders)
                    .with(new Placeholder("enchanting_enchant_level_to_enchant_roman", NumberUtil.toRoman(level)))
                    .with(new Placeholder("enchanting_enchant_level_to_enchant_number", level))
                    .with(getWarnings(level))
                    .with(getEnchantingCost(level))
                    .with(getClickPlaceholder(level))
                    .get()));

            enchantments.put(slot, level);
            level++;
        }

        setItem(config.getGoBackItem());
        setItem(config.getCloseGUI());

        return inventory;
    }

    @Override
    public void onInventoryClick(final InventoryClickEvent event) {
        event.setCancelled(true);

        if (!getTarget(event).equals(ClickTarget.INSIDE)) {
            return;
        }

        final Player player = (Player) event.getWhoClicked();

        final int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBackItem())) {
            giveItem = false;
            player.openInventory(new EnchantMainGUI(box, 1, bookShelf, item).getInventory());
        } else if (enchantments.containsKey(slot)) {
            final int level = enchantments.get(slot);

            final ClickStatus status = getClickStatus(level);

            if (status != ClickStatus.ENCHANT_AVAILABLE && status != ClickStatus.REMOVE_AVAILABLE) {
                sendWrongMessage(player, status);
                return;
            }
            final boolean isRemoveEnchant = status == ClickStatus.REMOVE_AVAILABLE;

            final EnchantmentSession session = new EnchantmentSessionImpl(enchantment, level);

            final TheEnchantingEnchantEvent event1 = new TheEnchantingEnchantEvent(player, session);

            Bukkit.getServer().getPluginManager().callEvent(event1);

            if (event1.isCancelled()) {
                return;
            }

            removeRequirements(player, isRemoveEnchant, level);

            giveItem = false;

            final ItemStack itemStack = isRemoveEnchant ? TheEnchanting.getApi().removeEnchantment(item, session) : TheEnchanting.getApi().addEnchantment(item, session);

            player.openInventory(new EnchantSubGUI(box, bookShelf, itemStack, enchantment, uuid).getInventory());

            final String message = isRemoveEnchant ? box.files().messages().enchantingMessages.enchantRemoved : box.files().messages().enchantingMessages.enchantApplied;

            final List<IPlaceholder> placeholders = PlaceholderBuilder.create(EnchantingPlaceholderUtil.getEnchantPlaceholders(enchantment, level))
                    .with(new Placeholder("enchanting_enchant_level_roman", NumberUtil.toRoman(level)))
                    .with(new Placeholder("enchanting_item_name", BukkitItemUtil.getName(itemStack)))
                    .get();

            player.sendMessage(StringUtils.processMulti(message, placeholders));
        }
    }


    private void sendWrongMessage(final Player player, final ClickStatus status) {
        if (status.equals(ClickStatus.NOT_ENOUGH_MONEY_TO_REMOVE) || status.equals(ClickStatus.NOT_ENOUGH_MONEY)) {
            player.sendMessage(StringUtils.color(box.files().messages().enchantingMessages.youDontHaveEnoughMoney));
        } else if (status.equals(ClickStatus.NOT_ENOUGH_LEVELS_TO_REMOVE) || status.equals(ClickStatus.NOT_ENOUGH_LEVELS)) {
            player.sendMessage(StringUtils.color(box.files().messages().enchantingMessages.youDontHaveEnoughLevels));
        } else if (status.equals(ClickStatus.NOT_ENOUGH_PERMISSIONS_TO_REMOVE) || status.equals(ClickStatus.NOT_ENOUGH_PERMISSIONS)) {
            player.sendMessage(StringUtils.color(box.files().messages().enchantingMessages.youDontHaveEnoughPermissions));
        } else {
            player.sendMessage(StringUtils.color(box.files().messages().enchantingMessages.higherLevelAlreadyPresent));
        }
    }

    private void removeRequirements(final Player player, final boolean isRemoveEnchant, final int level) {
        final double xp = isRemoveEnchant ? enchantment.getRequiredLevelToRemove(level) : enchantment.getRequiredLevelToEnchant(level);
        final double money = isRemoveEnchant ? enchantment.getRequiredMoneyToRemove(level) : enchantment.getRequiredMoneyToEnchant(level);

        player.setLevel((int) (player.getLevel() - xp));
        TheAssistantPlugin.getAPI().getAddons().getEconomy().withdrawMoney(player, money);
    }

    @Override
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();

        if (BukkitItemUtil.isNull(item)) return;

        if (!giveItem) return;

        player.getInventory().addItem(item);
    }


    public List<IPlaceholder> getEnchantingCost(int level) {
        int itemLevel = EnchantingFinderUtil.getItemLevel(item, enchantment.getEnchantment());

        boolean isRemoveEnchant = itemLevel == level;

        double xp = isRemoveEnchant ? enchantment.getRequiredLevelToRemove(level) : enchantment.getRequiredLevelToEnchant(level);
        double money = isRemoveEnchant ? enchantment.getRequiredMoneyToRemove(level) : enchantment.getRequiredMoneyToEnchant(level);

        return Arrays.asList(
                new Placeholder("enchanting_enchant_exp_cost", box.files().messages().enchantingPlaceholders.experienceCost.replace("%enchanting_enchant_exp_cost_amount%", String.valueOf(xp))),
                new Placeholder("enchanting_enchant_money_cost", box.files().messages().enchantingPlaceholders.moneyCost.replace("%enchanting_enchant_money_cost_amount%", String.valueOf(money)))
        );
    }

    private List<IPlaceholder> getWarnings(int level) {
        final int itemLevel = EnchantingFinderUtil.getItemLevel(item, enchantment.getEnchantment());
        final Optional<ICoreEnchantment> incompatibility = TheEnchanting.getApi().getIncompatibility(item, enchantment);
        if (incompatibility.isPresent()) {
            final List<String> processed = StringUtils.processMulti(box.files().messages().enchantingPlaceholders.warningThisWillRemoveEnchantment, Collections.singletonList(new Placeholder("enchanting_enchant_conflict_displayname", incompatibility.get().getName())));
            return Collections.singletonList(new Placeholder("enchanting_enchant_warning", processed));
        } else if (itemLevel == level) {
            return Collections.singletonList(new Placeholder("enchanting_enchant_warning", box.files().messages().enchantingPlaceholders.enchantmentIsAlreadyPresent));
        } else
            return Collections.singletonList(new Placeholder("enchanting_enchant_warning", box.files().messages().enchantingPlaceholders.emptyWarning));
    }

    private IPlaceholder getClickPlaceholder(final int level) {
        final ClickStatus status = getClickStatus(level);
        if (status.equals(ClickStatus.HIGHER_LEVEL_PRESENT))
            return new Placeholder("enchanting_enchant_click_placeholder", box.files().messages().enchantingPlaceholders.higherLevelPresent);
        else if (status.equals(ClickStatus.NOT_ENOUGH_MONEY_TO_REMOVE) || status.equals(ClickStatus.NOT_ENOUGH_MONEY))
            return new Placeholder("enchanting_enchant_click_placeholder", box.files().messages().enchantingPlaceholders.youDontHaveEnoughMoney);
        else if (status.equals(ClickStatus.NOT_ENOUGH_LEVELS_TO_REMOVE) || status.equals(ClickStatus.NOT_ENOUGH_LEVELS))
            return new Placeholder("enchanting_enchant_click_placeholder", box.files().messages().enchantingPlaceholders.youDontHaveEnoughExpLevels);
        else if (status.equals(ClickStatus.NOT_ENOUGH_PERMISSIONS_TO_REMOVE) || status.equals(ClickStatus.NOT_ENOUGH_PERMISSIONS))
            return new Placeholder("enchanting_enchant_click_placeholder", box.files().messages().enchantingPlaceholders.youDontHaveEnoughPermissions);
        else if (status.equals(ClickStatus.REMOVE_AVAILABLE))
            return new Placeholder("enchanting_enchant_click_placeholder", box.files().messages().enchantingPlaceholders.clickToRemove);
        else
            return new Placeholder("enchanting_enchant_click_placeholder", box.files().messages().enchantingPlaceholders.clickToEnchant);
    }


    private ClickStatus getClickStatus(int level) {
        int itemLevel = EnchantingFinderUtil.getItemLevel(item, enchantment.getEnchantment());

        Player player = Bukkit.getPlayer(uuid);

        double moneyCost = enchantment.getRequiredMoneyToEnchant(level);
        double levelCost = enchantment.getRequiredLevelToEnchant(level);

        double playerMoney = TheAssistantPlugin.getAPI().getAddons().getEconomy().getMoney(Bukkit.getOfflinePlayer(uuid));
        double playerLevel = Optional.ofNullable(player).map(Player::getLevel).orElse(0);

        if (itemLevel == level) {
            if (moneyCost > playerMoney)
                return ClickStatus.NOT_ENOUGH_MONEY_TO_REMOVE;
            else if (levelCost > playerLevel)
                return ClickStatus.NOT_ENOUGH_LEVELS_TO_REMOVE;
            else if (!enchantment.canEnchant(player, level))
                return ClickStatus.NOT_ENOUGH_PERMISSIONS_TO_REMOVE;
            else
                return ClickStatus.REMOVE_AVAILABLE;
        } else if (itemLevel > 0 && level < itemLevel)
            return ClickStatus.HIGHER_LEVEL_PRESENT;
        else if (moneyCost > playerMoney)
            return ClickStatus.NOT_ENOUGH_MONEY;
        else if (levelCost > playerLevel)
            return ClickStatus.NOT_ENOUGH_LEVELS;
        else if (!enchantment.canEnchant(player, level))
            return ClickStatus.NOT_ENOUGH_PERMISSIONS;
        else
            return ClickStatus.ENCHANT_AVAILABLE;
    }

    public enum ClickStatus{
        NOT_ENOUGH_MONEY_TO_REMOVE,
        NOT_ENOUGH_LEVELS_TO_REMOVE,
        NOT_ENOUGH_PERMISSIONS_TO_REMOVE,
        NOT_ENOUGH_MONEY,
        NOT_ENOUGH_LEVELS,
        NOT_ENOUGH_PERMISSIONS,
        REMOVE_AVAILABLE,
        ENCHANT_AVAILABLE,
        HIGHER_LEVEL_PRESENT,
    }

}
