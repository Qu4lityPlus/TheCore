package com.qualityplus.bank.base.handler;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.bank.api.handler.TrxHandler;
import com.qualityplus.bank.api.handler.TransactionGateway;
import com.qualityplus.bank.api.request.TrxRequest;
import com.qualityplus.bank.api.response.TrxResponse;
import com.qualityplus.bank.base.config.Messages;
import com.qualityplus.bank.base.event.BankInteractEvent;
import com.qualityplus.bank.base.exception.BankLimitException;
import com.qualityplus.bank.base.exception.NotEnoughMoneyException;
import com.qualityplus.bank.base.exception.ZeroMoneyException;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;
import com.qualityplus.bank.util.BankMessageUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public final class TransactionGatewayImpl implements TransactionGateway {
    private @Inject("withdraw") TrxHandler withdrawHandler;
    private @Inject("deposit") TrxHandler depositHandler;
    private @Inject Messages messages;

    @Override
    public Optional<TrxResponse> handle(
            final BankData bankData,
            final BankTransaction transaction,
            final boolean sendMessages,
            final boolean force,
            final boolean interest) {

        if (bankData == null) {
            return Optional.empty();
        }

        final BankInteractEvent bankInteractEvent = new BankInteractEvent(bankData, transaction);

        Bukkit.getPluginManager().callEvent(bankInteractEvent);

        if (bankInteractEvent.isCancelled()) {
            return Optional.empty();
        }

        final TrxRequest request = createRequest(bankData, transaction, sendMessages, force, interest);

        switch (transaction.getType()) {
            case SET:
                return handleSet(request);
            case DEPOSIT:
                return handleDeposit(request);
            case WITHDRAW:
                return handleWithdraw(request);
            default:
                return Optional.empty();
        }
    }


    private Optional<TrxResponse> handleSet(final TrxRequest request) {
        final BankData data = request.getBankData();

        final double amount = request.getTransaction().getAmount();

        data.setMoney(amount);

        return Optional.of(TrxResponse.builder()
                        .bankBalance(data.getMoney())
                        .amount(amount)
                        .build());
    }

    private Optional<TrxResponse> handleDeposit(final TrxRequest request) {
        try {
            final TrxResponse response = this.depositHandler.handle(request);

            final List<IPlaceholder> placeholder = Arrays.asList(
                    new Placeholder("deposited", response.getAmount()),
                    new Placeholder("bank_balance", response.getBankBalance())
            );

            final String message = StringUtils.processMulti(this.messages.bankMessages.youHaveDeposited, placeholder);

            BankMessageUtil.sendMessageToTrxActor(request, message);

            return Optional.of(response);
        } catch (final NotEnoughMoneyException e) {
            BankMessageUtil.sendMessageToTrxActor(request, this.messages.bankMessages.youDontHaveAnyCoins);

            return Optional.empty();
        } catch (final BankLimitException e) {
            BankMessageUtil.sendMessageToTrxActor(request, this.messages.bankMessages.youCantDepositReachBankLimit);

            return Optional.empty();
        }
    }

    private Optional<TrxResponse> handleWithdraw(final TrxRequest request) {
        try {
            final TrxResponse response = this.withdrawHandler.handle(request);

            final List<IPlaceholder> placeholder = Arrays.asList(
                    new Placeholder("withdrawn", response.getAmount()),
                    new Placeholder("bank_balance", response.getBankBalance())
            );

            final String message = StringUtils.processMulti(messages.bankMessages.youHaveWithdrawn, placeholder);

            BankMessageUtil.sendMessageToTrxActor(request, message);

            return Optional.of(response);
        } catch (final NotEnoughMoneyException e) {
            BankMessageUtil.sendMessageToTrxActor(request, messages.bankMessages.youDontHaveThatAmountToWithdraw);

            return Optional.empty();
        } catch (final ZeroMoneyException e) {
            BankMessageUtil.sendMessageToTrxActor(request, messages.bankMessages.youDontHaveAnyCoinsInYourBank);

            return Optional.empty();
        }
    }


    private TrxRequest createRequest(final BankData data, final BankTransaction trx, final boolean sendMessages, final boolean force, final boolean interest) {
        return TrxRequest.builder()
                .sendMsg(sendMessages)
                .transaction(trx)
                .bankData(data)
                .force(force)
                .interest(interest)
                .build();
    }
}
