package com.qualityplus.bank.base.addons;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.addons.PlaceholdersAddon;
import com.qualityplus.assistant.api.addons.registrable.RegistrableAddon;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.bank.api.service.BankService;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.stream.Stream;

@Component
public final class PlaceholdersRegistry {
    @Delayed(time = MinecraftTimeEquivalent.SECOND * 5)
    public void registerBankPlaceholders(@Inject BankService service) {
        PlaceholdersAddon addon = TheAssistantPlugin.getAPI().getAddons().getPlaceholders();

        addon.registerPlaceholders("bank_user_money",
                e -> String.valueOf(service.getData(e.getPlayer().getUniqueId()).map(BankData::getMoney).orElse(0D)));

        addon.registerPlaceholders("bank_last_interest",
                e -> String.valueOf(service.getData(e.getPlayer().getUniqueId()).map(BankData::getLastInterest).orElse(0D)));

        addon.registerPlaceholders("bank_current_profile_id",
                e -> service.getData(e.getPlayer().getUniqueId()).map(BankData::getBankUpgrade).orElse(""));

        Stream.of(addon)
                .filter(a -> a instanceof RegistrableAddon)
                .map(a -> (RegistrableAddon) a)
                .forEach(RegistrableAddon::registerAddon);
    }
}
