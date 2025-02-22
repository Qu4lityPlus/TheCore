package com.qualityplus.bank.base.handler;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Scheduled;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.bank.api.box.Box;
import com.qualityplus.bank.api.response.TrxResponse;
import com.qualityplus.bank.api.transaction.TransactionType;
import com.qualityplus.bank.base.gui.main.BankInterfaceGUI;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;

import com.qualityplus.bank.persistence.data.TransactionCaller;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@Scheduled(rate = MinecraftTimeEquivalent.SECOND)
public final class BankInterestHandler implements Runnable {
    private @Inject Box box;

    @Override
    public void run() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final BankData bankData = box.service().getData(player.getUniqueId()).orElse(new BankData());

            final Markable markable = new Markable(box.files().config().bankInterestDelay.getEffectiveTime(), bankData.getLastInterestTime());

            if (!markable.getRemainingTime().isZero()) {
                continue;
            }

            final double interest = bankData.getCalculatedInterest(box.files().bankUpgrades());

            if (interest <= 0) {
                continue;
            }

            final BankTransaction trx = new BankTransaction(interest, TransactionType.DEPOSIT, BankInterfaceGUI.GUIType.GENERAL, TransactionCaller.SERVER);

            final Optional<TrxResponse> response = box.service().handleTransaction(player, trx, false, false, true);

            if (response.isEmpty()) {
                continue;
            }

            bankData.setLastInterestTime(System.currentTimeMillis());
            bankData.setLastInterest(interest);

            final List<IPlaceholder> placeholders = new Placeholder("bank_calculated_interest", interest).alone();

            final String message = StringUtils.processMulti(box.files().messages().bankMessages.receivedInterestMessage, placeholders);

            player.sendMessage(message);
        }
    }
}