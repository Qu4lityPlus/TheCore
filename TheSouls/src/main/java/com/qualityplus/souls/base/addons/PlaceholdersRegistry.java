package com.qualityplus.souls.base.addons;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.addons.PlaceholdersAddon;
import com.qualityplus.assistant.api.addons.registrable.RegistrableAddon;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.souls.api.service.SoulsService;
import com.qualityplus.souls.base.config.Souls;
import com.qualityplus.souls.persistance.data.SoulsData;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public final class PlaceholdersRegistry {
    @Delayed(time = MinecraftTimeEquivalent.SECOND * 5)
    public void registerBankPlaceholders(@Inject SoulsService service, @Inject Souls file) {
        PlaceholdersAddon addon = TheAssistantPlugin.getAPI().getAddons().getPlaceholders();

        addon.registerPlaceholders("souls_user_amount",
                e -> String.valueOf(service.getData(e.getPlayer().getUniqueId()).map(SoulsData::getSoulsCollected).map(List::size).orElse(0)));

        addon.registerPlaceholders("souls_server_amount",
                e -> String.valueOf(file.soulList.size()));

        addon.registerPlaceholders("souls_user_tia_amount",
                e -> String.valueOf(service.getData(e.getPlayer().getUniqueId()).map(SoulsData::getTiaSoulsCollected).map(List::size).orElse(0)));

        Stream.of(addon)
                .filter(a -> a instanceof RegistrableAddon)
                .map(a -> (RegistrableAddon) a)
                .forEach(RegistrableAddon::registerAddon);
    }
}
