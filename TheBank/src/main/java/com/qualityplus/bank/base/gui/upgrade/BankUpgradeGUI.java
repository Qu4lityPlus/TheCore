package com.qualityplus.bank.base.gui.upgrade;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.bank.api.box.Box;
import com.qualityplus.bank.base.gui.BankGUI;
import com.qualityplus.bank.base.gui.main.BankInterfaceGUI;
import com.qualityplus.bank.base.gui.main.BankInterfaceGUI.GUIType;
import com.qualityplus.bank.base.upgrade.BankUpgrade;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.util.BankItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class BankUpgradeGUI extends BankGUI {
    private final Map<Integer, BankUpgrade> bankUpgradeMap = new HashMap<>();
    private final BankUpgradeGUIConfig config;
    private final GUIType type;
    private final UUID uuid;

    public BankUpgradeGUI(Box box, Player player, GUIType type) {
        super(box.files().inventories().bankUpgradeGUI, box);

        this.config = box.files().inventories().bankUpgradeGUI;
        this.uuid = player.getUniqueId();
        this.type = type;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        BankData bankData = box.service().getData(uuid).orElse(new BankData());

        Optional<BankUpgrade> playerUpgrade = box.files().bankUpgrades().getUpgrade(bankData);

        for (BankUpgrade upgrade : box.files().bankUpgrades().bankUpgrades) {
            List<IPlaceholder> placeholders = getPlaceholders(playerUpgrade.orElse(null), upgrade);

            Item item = upgrade.getId().equals(playerUpgrade.map(BankUpgrade::getId).orElse("")) ? config.getCurrentUpgradeItem() : config.getNotCurrentUpgradeItem();

            inventory.setItem(upgrade.getDisplayItem().getSlot(), BankItemStackUtils.makeUpgradeItem(item, placeholders, upgrade.getDisplayItem(), box.files().config().loreWrapper));

            bankUpgradeMap.put(upgrade.getDisplayItem().getSlot(), upgrade);
        }

        setItem(config.getCloseGUI());

        setItem(config.getGoBackItem());

        return inventory;
    }

    private List<IPlaceholder> getPlaceholders(BankUpgrade playerUpgrade, BankUpgrade toCompare) {

        String status = getPlaceholder(getUpgradeStatus(playerUpgrade, toCompare));

        return Arrays.asList(
                new Placeholder("bank_upgrade_displayname", toCompare.getDisplayName()),
                new Placeholder("bank_upgrade_description", toCompare.getDisplayItem().getDescription()),
                new Placeholder("bank_upgrade_status", status)
        );
    }

    private UpgradeStatus getUpgradeStatus(BankUpgrade playerUpgrade, BankUpgrade toCompare) {
        double playerMoney = TheAssistantPlugin.getAPI().getAddons().getEconomy().getMoney(Bukkit.getOfflinePlayer(uuid));

        if (toCompare.getHierarchy() < playerUpgrade.getHierarchy()) {
            return UpgradeStatus.YOU_HAVE_BETTER_ACCOUNT;
        } else if (toCompare.getHierarchy() == playerUpgrade.getHierarchy()) {
            return UpgradeStatus.THIS_IS_YOUR_ACCOUNT;
        } else if (playerMoney < toCompare.getCoinsCost()) {
            return UpgradeStatus.NOT_ENOUGH_COINS;
        } else if (!toCompare.canPayItems(Bukkit.getPlayer(uuid))) {
            return UpgradeStatus.NOT_ENOUGH_ITEMS;
        } else if (toCompare.getHierarchy() - 1 != playerUpgrade.getHierarchy()) {
            return UpgradeStatus.NEED_PREVIOUS_UPGRADE;
        } else {
            return UpgradeStatus.AVAILABLE_TO_PURCHASE;
        }
    }

    private String getPlaceholder(UpgradeStatus status) {
        switch (status) {
            case YOU_HAVE_BETTER_ACCOUNT:
                return box.files().messages().bankMessages.youHaveBetterAccount;

            case THIS_IS_YOUR_ACCOUNT:
                return box.files().messages().bankMessages.thisIsYourAccount;
            case NOT_ENOUGH_COINS:
                return box.files().messages().bankMessages.notEnoughCoins;
            case NOT_ENOUGH_ITEMS:
                return box.files().messages().bankMessages.notEnoughItems;
            case NEED_PREVIOUS_UPGRADE:
                return box.files().messages().bankMessages.needPreviousUpgrade;
            case AVAILABLE_TO_PURCHASE:
                return box.files().messages().bankMessages.clickToBuy;
            default:
                return "&cERROR";
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        int slot = e.getSlot();

        e.setCancelled(true);

        if (!getTarget(e).equals(ClickTarget.INSIDE)) return;

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBackItem())) {
            player.openInventory(new BankInterfaceGUI(box, player, type).getInventory());
        } else if (bankUpgradeMap.containsKey(slot)) {
            BankUpgrade upgrade = bankUpgradeMap.getOrDefault(slot, null);

            if (upgrade == null) return;

            BankData bankData = box.service().getData(uuid).orElse(new BankData());

            BankUpgrade playerUpgrade = box.files().bankUpgrades().getUpgrade(bankData).orElse(null);

            if (playerUpgrade == null) return;

            UpgradeStatus status = getUpgradeStatus(playerUpgrade, upgrade);

            if (status == UpgradeStatus.AVAILABLE_TO_PURCHASE) {
                TheAssistantPlugin.getAPI().getAddons().getEconomy().withdrawMoney(player, upgrade.getCoinsCost());
                upgrade.payItems(player);

                bankData.setBankUpgrade(upgrade.getId());

                player.sendMessage(StringUtils.color(box.files().messages().bankMessages.successfullyUpdatedBank.replace("%bank_upgrade_displayname%", upgrade.getDisplayName())));

                player.openInventory(new BankUpgradeGUI(box, player, type).getInventory());
            } else {
                String placeholder = getPlaceholder(status);

                player.sendMessage(StringUtils.color(placeholder));
            }

        }

    }

    enum UpgradeStatus{
        YOU_HAVE_BETTER_ACCOUNT,
        THIS_IS_YOUR_ACCOUNT,
        NOT_ENOUGH_COINS,
        NOT_ENOUGH_ITEMS,
        NEED_PREVIOUS_UPGRADE,
        AVAILABLE_TO_PURCHASE;
    }
}
