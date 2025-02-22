package com.qualityplus.bank.base.handler;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.addons.EconomyAddon;
import com.qualityplus.bank.api.handler.TrxHandler;
import com.qualityplus.bank.api.request.TrxRequest;
import com.qualityplus.bank.api.response.TrxResponse;
import com.qualityplus.bank.base.config.BankUpgrades;
import com.qualityplus.bank.base.exception.BankLimitException;
import com.qualityplus.bank.base.exception.NotEnoughMoneyException;
import com.qualityplus.bank.base.gui.main.BankInterfaceGUI;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;
import com.qualityplus.bank.persistence.data.TransactionCaller;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

@RequiredArgsConstructor
public final class DepositHandler implements TrxHandler {
    private final BankUpgrades upgrades;

    @Override
    public TrxResponse handle(final TrxRequest request) throws NotEnoughMoneyException, BankLimitException {
        final BankData bankData = request.getBankData();
        final BankTransaction transaction = request.getTransaction();
        final OfflinePlayer player = Bukkit.getOfflinePlayer(bankData.getUuid());
        final EconomyAddon economy = TheAssistantPlugin.getAPI().getAddons().getEconomy();

        if (request.isForce()) {
            final double newBalance = transaction.getAmount() + bankData.getMoney();

            if (newBalance > bankData.getLimit(upgrades)) {
                throw new BankLimitException(bankData);
            }

            if (request.getTransaction().getCaller().equals(TransactionCaller.PLAYER)) {
                final double balance = economy.getMoney(player);

                if (balance <= 0) {
                    throw new NotEnoughMoneyException(bankData);
                }
                economy.withdrawMoney(player, transaction.getAmount());
            }
        }

        if (!request.isInterest()) {
            economy.withdrawMoney(player, transaction.getAmount());
        }

        bankData.addMoney(transaction.getAmount());

        bankData.addTrx(transaction);

        if (transaction.getGuiType() == BankInterfaceGUI.GUIType.PERSONAL) {
            bankData.setLastTimeUsedPersonal(System.currentTimeMillis());
        }

        return TrxResponse.builder()
                .amount(transaction.getAmount())
                .bankBalance(bankData.getMoney())
                .build();
    }
}
