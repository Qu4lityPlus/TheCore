package com.qualityplus.skills.base.addons;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.dependency.resolver.DependencyResolver;
import com.qualityplus.assistant.lib.eu.okaeri.injector.OkaeriInjector;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.skills.api.listener.ExtraListener;
import com.qualityplus.skills.listener.minion.TheMinionsListener;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Bean;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.logging.Logger;

@Component
public final class MinionsRegistry {
    private static final String MINIONS_HOOKED_MESSAGE = "Successfully hooked into %s!";
    private static final String THE_MINIONS_PLUGIN_NAME = "TheMinions";
    private @Inject("injector") OkaeriInjector injector;
    private @Inject Plugin plugin;
    private @Inject Logger logger;

    @Bean("minionListener")
    public Optional<Class<? extends ExtraListener>> configureMinions() {
        final DependencyResolver resolver = TheAssistantPlugin.getAPI().getDependencyResolver();

        if (resolver.isPlugin(THE_MINIONS_PLUGIN_NAME)) {
            sendMinionsMessage();
            return Optional.of(TheMinionsListener.class);
        }

        return Optional.empty();
    }

    private void sendMinionsMessage() {
        logger.info(String.format(MINIONS_HOOKED_MESSAGE, MinionsRegistry.THE_MINIONS_PLUGIN_NAME));
    }
}
