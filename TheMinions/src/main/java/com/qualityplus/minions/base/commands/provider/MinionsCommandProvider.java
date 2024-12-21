package com.qualityplus.minions.base.commands.provider;

import com.qualityplus.assistant.api.commands.LabelProvider;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

@Component
public final class MinionsCommandProvider {
    @Delayed(time = MinecraftTimeEquivalent.SECOND / 20, async = true)
    private void configureProvider(@Inject Box box) {
        LabelProvider.builder()
                .id("theminions")
                .label("theminions")
                .plugin(box.plugin())
                .useHelpMessage(box.files().messages().pluginMessages.useHelp)
                .unknownCommandMessage(box.files().messages().pluginMessages.unknownCommand)
                .onlyForPlayersMessage(box.files().messages().pluginMessages.mustBeAPlayer)
                .noPermissionMessage(box.files().messages().pluginMessages.noPermission)
                .build()
                .register();
    }
}
