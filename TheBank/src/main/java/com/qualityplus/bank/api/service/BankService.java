package com.qualityplus.bank.api.service;

import com.qualityplus.assistant.api.service.DataManagementService;
import com.qualityplus.bank.api.response.TrxResponse;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface BankService extends DataManagementService<BankData> {
    Optional<TrxResponse> handleTransaction(
            final Player player,
            final BankTransaction transaction,
            final boolean sendMessages,
            final boolean force,
            final boolean interest);
}
