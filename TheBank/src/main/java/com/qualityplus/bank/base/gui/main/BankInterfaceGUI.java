package com.qualityplus.bank.base.gui.main;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.org.h2.mvstore.tx.Transaction;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.assistant.util.time.RemainingTime;
import com.qualityplus.assistant.util.time.TimeUtils;
import com.qualityplus.bank.api.box.Box;
import com.qualityplus.bank.api.transaction.TransactionType;
import com.qualityplus.bank.base.config.Messages;
import com.qualityplus.bank.base.gui.BankGUI;
import com.qualityplus.bank.base.gui.deposit.BankDepositGUI;
import com.qualityplus.bank.base.gui.upgrade.BankUpgradeGUI;
import com.qualityplus.bank.base.gui.withdraw.BankWithdrawGUI;
import com.qualityplus.bank.base.upgrade.BankUpgrade;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class BankInterfaceGUI extends BankGUI {
    private final BankInterfaceGUIConfig config;
    private final GUIType type;
    private final UUID uuid;

    public BankInterfaceGUI(Box box, Player player, GUIType type) {
        super(box.files().inventories().bankGUI, box);

        this.config = box.files().inventories().bankGUI;
        this.uuid = player.getUniqueId();
        this.type = type;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        BankData bankData = box.service().getData(uuid).orElse(new BankData());

        List<IPlaceholder> placeholders = getPlaceholders(bankData);

        setItem(config.getCloseGUI());

        setItem(config.getInformationItem(), placeholders);
        setItem(config.getTransactionsItem(), placeholders);
        setItem(config.getBankUpgradesItem(), placeholders);
        setItem(config.getDepositItem(), placeholders);
        setItem(config.getWithDrawItem(), placeholders);

        return inventory;
    }

    private List<IPlaceholder> getPlaceholders(BankData bankData) {
        Optional<BankUpgrade> upgrade = box.files().bankUpgrades().getUpgrade(bankData);

        return Arrays.asList(
                new Placeholder("bank_transaction_list", getLatestTransactions(bankData)),
                new Placeholder("bank_balance", bankData.getMoney()),
                new Placeholder("bank_interest_calculated", bankData.getCalculatedInterest(box.files().bankUpgrades())),
                new Placeholder("bank_interest_percentage", bankData.getInterest(box.files().bankUpgrades())),
                new Placeholder("bank_interest_remaining_time", getLastInterest(bankData)),
                new Placeholder("bank_interest_last", bankData.getLastInterest()),
                new Placeholder("bank_limit_amount", upgrade.map(BankUpgrade::getBankLimit).orElse(0D)),
                new Placeholder("bank_limit_displayname", upgrade.map(BankUpgrade::getDisplayName).orElse(""))
        );
    }

    private String getLastInterest(BankData data) {
        Messages.BankMessages messages = box.files().messages().bankMessages;

        RemainingTime time = TimeUtils.getRemainingTime(new Markable(box.files().config().bankInterestDelay.getEffectiveTime(), data.getLastInterestTime()).remainingTime());

        return getParsedTime(time, messages.interestRemainingTime);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        int slot = e.getSlot();

        e.setCancelled(true);

        if (!getTarget(e).equals(ClickTarget.INSIDE)) return;

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getDepositItem())) {
            player.openInventory(new BankDepositGUI(box, player, type).getInventory());
        } else if (isItem(slot, config.getWithDrawItem())) {
            player.openInventory(new BankWithdrawGUI(box, player, type).getInventory());
        } else if (isItem(slot, config.getBankUpgradesItem()))
            player.openInventory(new BankUpgradeGUI(box, player, type).getInventory());

    }

    private List<String> getLatestTransactions(BankData data) {
        int count = 0;
        final List<String> transactions = new ArrayList<>();
        final List<BankTransaction> transactionsOrdered = data.getTransactionList().reversed();

        for (BankTransaction trx : transactionsOrdered) {
            if (count == config.getTransactionLimit()) {
                break;
            }

            Messages.BankMessages messages = box.files().messages().bankMessages;

            RemainingTime time = TimeUtils.getTimeWhenAgo(trx.getWhenHappened());

            List<IPlaceholder> placeholders = PlaceholderBuilder.create(new Placeholder("transaction_amount", trx.getAmount())).get();

            String message = trx.getType().equals(TransactionType.DEPOSIT) ? messages.transactionDepositAgoTime : messages.transactionWithdrawAgoTime;

            String finalMessage = StringUtils.processMulti(message, placeholders);

            transactions.add(getParsedTime(time, finalMessage));

            count++;
        }

        return transactions;
    }

    private String getParsedTime(RemainingTime remainingTime, String message) {
        Messages.BankMessages messages = box.files().messages().bankMessages;

        String days = messages.days;
        String hours = messages.hours;
        String minutes = messages.minutes;
        String seconds = messages.seconds;
        String noTime = messages.noTime;

        return TimeUtils.getParsedTime(remainingTime, message, days, hours, minutes, seconds, noTime, messages.showTimeIfZero);
    }

    public enum GUIType{
        PERSONAL,
        GENERAL;
    }
}
