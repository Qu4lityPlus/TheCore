package com.qualityplus.bank.listener;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.tasker.core.Tasker;
import com.qualityplus.bank.api.box.Box;
import com.qualityplus.bank.base.upgrade.BankUpgrade;
import com.qualityplus.bank.persistence.data.BankData;

import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public final class UserListener implements Listener {
    private @Inject Tasker tasker;
    private @Inject Box box;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.tasker.newChain()
                .async(() -> box.repository().get(event.getPlayer()))
                .acceptSync(box.service()::addData)
                .acceptSync(this::setupUpgrade)
                .execute();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.tasker.newChain()
                .async(() -> box.service().getData(event.getPlayer().getUniqueId()))
                .acceptAsync((Consumer<Optional<BankData>>) user -> user.ifPresent(Document::save))
                .acceptAsync((Consumer<Optional<BankData>>) user -> user.ifPresent(box.service()::removeData))
                .execute();
    }

    private void setupUpgrade(BankData bankData) {
        if (bankData.getBankUpgrade() != null) return;

        bankData.setBankUpgrade(box.files().bankUpgrades().getLowest().map(BankUpgrade::getId).orElse(null));
    }
}