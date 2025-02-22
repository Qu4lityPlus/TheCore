package com.qualityplus.bank.base.handler;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.addons.EconomyAddon;
import com.qualityplus.bank.api.handler.TrxHandler;
import com.qualityplus.bank.api.request.TrxRequest;
import com.qualityplus.bank.api.response.TrxResponse;
import com.qualityplus.bank.base.exception.NotEnoughMoneyException;
import com.qualityplus.bank.base.exception.ZeroMoneyException;
import com.qualityplus.bank.base.gui.main.BankInterfaceGUI;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class WithdrawHandler implements TrxHandler {

    @Override
    public TrxResponse handle(final TrxRequest request) {
        final BankData bankData = request.getBankData();
        final BankTransaction transaction = request.getTransaction();
        final EconomyAddon economy = TheAssistantPlugin.getAPI().getAddons().getEconomy();

        if (request.isForce()) {

            double bankMoney = bankData.getMoney();

            if (bankMoney <= 0) {
                throw new ZeroMoneyException(bankData);
            }

            if (transaction.getAmount() > bankMoney) {
                throw new NotEnoughMoneyException(bankData);
            }

            transaction.setAmount(Math.max(transaction.getAmount(), 0));
        }

        final OfflinePlayer player = Bukkit.getOfflinePlayer(bankData.getUuid());

        transaction.setAmount(Math.max(transaction.getAmount(), 0));

        economy.depositMoney(player, transaction.getAmount());

        bankData.removeMoney(transaction.getAmount());

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
