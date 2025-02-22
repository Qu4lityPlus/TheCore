package com.qualityplus.bank.persistence;

import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.DocumentPersistence;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.repository.DocumentRepository;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.repository.annotation.DocumentCollection;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.repository.annotation.DocumentIndex;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.repository.annotation.DocumentPath;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.DependsOn;
import com.qualityplus.bank.persistence.data.BankData;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@DependsOn(type = DocumentPersistence.class, name = "persistence")
@DocumentCollection(path = "player", keyLength = 36, indexes = {
        @DocumentIndex(path = "name", maxLength = 24)
})
public interface BankRepository extends DocumentRepository<UUID, BankData> {

    @DocumentPath("name")
    Optional<BankData> findByName(String name);

    @DocumentPath("uuid")
    Optional<BankData> findByUuid(UUID uuid);

    default BankData get(OfflinePlayer player) {
        BankData user = this.findOrCreateByPath(player.getUniqueId());

        if (user.getUuid() == null) user.setUuid(player.getUniqueId());

        if (user.getTransactionList() == null) user.setTransactionList(new ArrayList<>());

        Optional.ofNullable(player.getName()).ifPresent(user::setName);

        return user;
    }
}
