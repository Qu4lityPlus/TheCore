package com.qualityplus.bank.api.handler;

import com.qualityplus.bank.api.response.TrxResponse;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;

import java.util.Optional;

public interface TransactionGateway {
    public Optional<TrxResponse> handle(final BankData data,
                                        final BankTransaction transaction,
                                        final boolean showMessage,
                                        final boolean force,
                                        final boolean interest);
}
