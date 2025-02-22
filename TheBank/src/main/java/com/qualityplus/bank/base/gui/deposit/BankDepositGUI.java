package com.qualityplus.bank.base.gui.deposit;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.de.rapha149.signgui.SignGUI;
import com.qualityplus.assistant.lib.de.rapha149.signgui.SignGUIFinishHandler;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.bank.TheBank;
import com.qualityplus.bank.api.box.Box;
import com.qualityplus.bank.api.transaction.TransactionType;
import com.qualityplus.bank.base.gui.BankGUI;
import com.qualityplus.bank.base.gui.main.BankInterfaceGUI;
import com.qualityplus.bank.base.gui.main.BankInterfaceGUI.GUIType;
import com.qualityplus.bank.base.sign.SignGUIAPI;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;
import com.qualityplus.bank.persistence.data.TransactionCaller;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class BankDepositGUI extends BankGUI {
    private final BankDepositGUIConfig config;
    private final GUIType type;
    private final UUID uuid;

    public BankDepositGUI(Box box, Player player, GUIType type) {
        super(box.files().inventories().bankDepositGUIConfig, box);

        this.config = box.files().inventories().bankDepositGUIConfig;
        this.uuid = player.getUniqueId();
        this.type = type;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        BankData bankData = box.service().getData(uuid).orElse(new BankData());

        List<IPlaceholder> placeholders = getPlaceholders(bankData);

        setItem(config.getCloseGUI());
        setItem(config.getGoBack());

        setItem(config.getDepositAll(), placeholders);
        setItem(config.getDepositHalf(), placeholders);
        setItem(config.getDepositCustomAmount(), placeholders);

        return inventory;
    }

    private List<IPlaceholder> getPlaceholders(BankData bankData) {
        return Arrays.asList(
                new Placeholder("bank_balance", bankData.getMoney()),
                new Placeholder("player_balance", getBalance()),
                new Placeholder("player_half_balance", getHalf())
        );
    }


    private double getHalf() {
        return Math.max(0, TheAssistantPlugin.getAPI().getAddons().getEconomy().getMoney(Bukkit.getOfflinePlayer(uuid)) / 2);
    }

    private double getBalance() {
        return TheAssistantPlugin.getAPI().getAddons().getEconomy().getMoney(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public void onInventoryClick(final InventoryClickEvent e) {
        final Player player = (Player) e.getWhoClicked();

        final int slot = e.getSlot();

        e.setCancelled(true);

        if (!getTarget(e).equals(ClickTarget.INSIDE)) {
            return;
        }

        if (isItem(slot, config.getCloseGUI())) {
            e.setCancelled(true);
            player.closeInventory();
        } else if (isItem(slot, config.getGoBack())) {
            player.openInventory(new BankInterfaceGUI(box, player, type).getInventory());
        } else if (isItem(slot, config.getDepositAll())) {
            box.service().handleTransaction(player, new BankTransaction(getBalance(), TransactionType.DEPOSIT, type, TransactionCaller.PLAYER), true, false, false);
            player.openInventory(new BankDepositGUI(box, player, type).getInventory());
        } else if (isItem(slot, config.getDepositHalf())) {
            box.service().handleTransaction(player, new BankTransaction(getHalf(), TransactionType.DEPOSIT, type, TransactionCaller.PLAYER), true, false, false);
            player.openInventory(new BankDepositGUI(box, player, type).getInventory());
        } else if (isItem(slot, config.getDepositCustomAmount())) {
            final Location location = player.getLocation().clone().add(0, 10, 0);

            Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> {
                try {
                    final String[] signLines = box.files().messages().bankMessages.enterAmountToDeposit.toArray(new String[0]);
                    if (XMaterial.getVersion() > 20) {
                        final SignGUIFinishHandler signGUIFinishHandler = (player1, signGUIResult) -> {
                            Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> {
                                this.handleDeposit(player1, signGUIResult.getLines());
                            }, 5);
                            return Collections.emptyList();
                        };
                        final SignGUI signGUI = SignGUI.builder().
                            setLocation(location).
                            setColor(DyeColor.BLACK).
                            setType(Material.OAK_SIGN).
                            setHandler(signGUIFinishHandler).setGlow(false).
                            setLines(signLines).
                            build();
                        signGUI.open(player);
                    } else {
                        SignGUIAPI.builder()
                                .action((result) -> {
                                    Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> {
                                        this.handleDeposit(result.getPlayer(), result.getLines().toArray(new String[0]));
                                    }, 5);
                                })
                                .withLines(box.files().messages().bankMessages.enterAmountToDeposit)
                                .uuid(player.getUniqueId())
                                .plugin(TheBank.getApi().getPlugin())
                                .build()
                                .open();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }, 5);
        }
    }

    private void handleDeposit(final Player player, final String[] event) {
        int value = 0;
        try {
            value = Integer.parseInt(event[0]);
        } catch (NumberFormatException ignored) {}

        box.service().handleTransaction(player, new BankTransaction(Math.max(0, value), TransactionType.DEPOSIT, type, TransactionCaller.PLAYER), true, false, false);

        Bukkit.getScheduler().runTaskLater(box.plugin(), () -> player.openInventory(new BankDepositGUI(box, player, type).getInventory()), 3);
    }
}
