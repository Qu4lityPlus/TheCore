package com.qualityplus.runes.base.factory;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.lib.eu.okaeri.injector.OkaeriInjector;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.runes.api.provider.LevelProvider;
import com.qualityplus.runes.base.provider.RuneCraftLevelProvider;
import com.qualityplus.runes.base.provider.XPLevelProvider;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Bean;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.plugin.Plugin;

@Component
public final class LevelProviderFactory {
    private @Inject("injector") OkaeriInjector injector;
    private @Inject Plugin plugin;

    @Bean
    public LevelProvider configureLevelProvider() {
        if (TheAssistantPlugin.getAPI().getDependencyResolver().isPlugin("TheSkills"))
            return injector.createInstance(RuneCraftLevelProvider.class);
        else
            return injector.createInstance(XPLevelProvider.class);
    }
}
