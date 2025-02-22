package com.qualityplus.bank.base.gui.withdraw;

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

public final class BankWithdrawGUI extends BankGUI {
    private final BankWithdrawGUIConfig config;
    private final GUIType type;
    private final UUID uuid;

    public BankWithdrawGUI(Box box, Player player, GUIType type) {
        super(box.files().inventories().bankWithdrawGUIConfig, box);

        this.config = box.files().inventories().bankWithdrawGUIConfig;
        this.uuid = player.getUniqueId();
        this.type = type;
    }

    private BankData getData() {
        return box.service().getData(uuid).orElse(new BankData());
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<IPlaceholder> placeholders = getPlaceholders(getData());

        setItem(config.getCloseGUI());
        setItem(config.getGoBack());

        setItem(config.getWithdrawAll(), placeholders);
        setItem(config.getWithdrawHalf(), placeholders);
        setItem(config.getWithdrawAmount(), placeholders);
        setItem(config.getWithDrawCustomPercentage(), placeholders);

        return inventory;
    }

    private List<IPlaceholder> getPlaceholders(BankData bankData) {
        return Arrays.asList(
                new Placeholder("bank_balance", bankData.getMoney()),
                new Placeholder("bank_balance_custom_percentage", getCustomPercentage(bankData)),
                new Placeholder("bank_half_balance", getHalf(bankData))
        );
    }

    private double getCustomPercentage(BankData bankData) {
        return (config.getCustomPercentage() * bankData.getMoney()) / 100;
    }

    private double getHalf(BankData bankData) {
        return Math.max(0, bankData.getMoney() / 2);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        int slot = e.getSlot();

        e.setCancelled(true);

        if (!getTarget(e).equals(ClickTarget.INSIDE)) return;

        if (isItem(slot, config.getCloseGUI())) {
            e.setCancelled(true);
            player.closeInventory();
        } else if (isItem(slot, config.getGoBack())) {
            player.openInventory(new BankInterfaceGUI(box, player, type).getInventory());
        } else if (isItem(slot, config.getWithdrawAll())) {
            box.service().handleTransaction(player, new BankTransaction(getData().getMoney(), TransactionType.WITHDRAW, type, TransactionCaller.PLAYER), true, false, false);
            player.openInventory(new BankWithdrawGUI(box, player, type).getInventory());
        } else if (isItem(slot, config.getWithdrawHalf())) {
            box.service().handleTransaction(player, new BankTransaction(getHalf(getData()), TransactionType.WITHDRAW, type, TransactionCaller.PLAYER), true, false, false);
            player.openInventory(new BankWithdrawGUI(box, player, type).getInventory());
        } else if (isItem(slot, config.getWithDrawCustomPercentage())) {
            box.service().handleTransaction(player, new BankTransaction(getCustomPercentage(getData()), TransactionType.WITHDRAW, type, TransactionCaller.PLAYER), true, false, false);
            player.openInventory(new BankWithdrawGUI(box, player, type).getInventory());
        } else if (isItem(slot, config.getWithdrawAmount())) {
            final Location location = player.getLocation().clone().add(0, 10, 0);

            Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> {
                try {
                    final String[] signLines = box.files().messages().bankMessages.enterAmountToWithdraw.toArray(new String[0]);
                    if (XMaterial.getVersion() > 20) {
                        final SignGUIFinishHandler signGUIFinishHandler = (player1, signGUIResult) -> {
                            Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> {
                                this.handleWithdraw(player1, signGUIResult.getLines());
                            }, 5);
                            return Collections.emptyList();
                        };
                        final com.qualityplus.assistant.lib.de.rapha149.signgui.SignGUI signGUI = SignGUI.builder().
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
                                        this.handleWithdraw(result.getPlayer(), result.getLines().toArray(new String[0]));
                                    }, 5);
                                })
                                .withLines(box.files().messages().bankMessages.enterAmountToWithdraw)
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

    private void handleWithdraw(final Player player, final String[] lines) {
        int value = 0;

        try {
            value = Integer.parseInt(lines[0]);
        } catch (final NumberFormatException ignored) {

        }

        box.service().handleTransaction(player, new BankTransaction(Math.max(0, value), TransactionType.WITHDRAW, type, TransactionCaller.PLAYER), true, false, false);

        Bukkit.getScheduler().runTaskLater(box.plugin(), () -> player.openInventory(new BankWithdrawGUI(box, player, type).getInventory()), 3);
    }
}
