package com.qualityplus.bank.base.service;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.bank.api.handler.TransactionGateway;
import com.qualityplus.bank.api.response.TrxResponse;
import com.qualityplus.bank.api.service.BankService;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.bank.persistence.data.BankTransaction;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public final class BankServiceImpl implements BankService {
    private final Map<UUID, BankData> dataMap = new HashMap<>();
    private @Inject TransactionGateway gateway;

    @Override
    public Optional<BankData> getData(final UUID uuid) {
        return Optional.ofNullable(this.dataMap.getOrDefault(uuid, null));
    }

    @Override
    public void addData(final BankData data) {
        this.dataMap.put(data.getUuid(), data);
    }

    @Override
    public void removeData(final BankData data) {
        this.dataMap.remove(data.getUuid());
    }

    @Override
    public Optional<TrxResponse> handleTransaction(final Player player,
                                                   final BankTransaction transaction,
                                                   final boolean sendMessages,
                                                   final boolean force,
                                                   final boolean interest) {
        return getData(player.getUniqueId()).flatMap(data -> this.gateway.handle(data, transaction, sendMessages, force, interest));
    }
}
