package com.qualityplus.bank;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.okaeri.OkaeriSilentPlugin;
import com.qualityplus.bank.api.TheBankAPI;
import com.qualityplus.bank.api.box.Box;
import com.qualityplus.bank.persistence.data.BankData;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Scan;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.ExecutionPhase;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.Planned;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@Scan(deep = true)
public final class TheBank extends OkaeriSilentPlugin {
    private static @Inject @Getter TheBankAPI api;

    @Planned(ExecutionPhase.PRE_SETUP)
    private void beforeStart() {

    }

    @Planned(ExecutionPhase.PRE_SHUTDOWN)
    private void whenStop(Box box) {
        Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getUniqueId)
                .map(box.service()::getData)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(BankData::save);
    }
}
