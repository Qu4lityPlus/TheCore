package com.qualityplus.bank.api.request;

import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;
import com.qualityplus.bank.persistence.data.TransactionCaller;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class TrxRequest {
    private final BankTransaction transaction;
    private final BankData bankData;
    private final boolean sendMsg;
    private final boolean force;
    private final boolean interest;
}
