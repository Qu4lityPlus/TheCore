package com.qualityplus.collections.base.addons;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.dependency.resolver.DependencyResolver;
import com.qualityplus.assistant.lib.eu.okaeri.injector.OkaeriInjector;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.collections.listener.minions.TheMinionsListener;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

@Component
public final class MinionsRegistry {
    private static final String MINIONS_HOOKED_MESSAGE = "Successfully hooked into %s!";
    private static final String THE_MINIONS_PLUGIN_NAME = "TheMinions";
    private @Inject("injector") OkaeriInjector injector;
    private @Inject Plugin plugin;
    private @Inject Logger logger;

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void configureMinions() {
        final DependencyResolver resolver = TheAssistantPlugin.getAPI().getDependencyResolver();

        if (resolver.isPlugin(THE_MINIONS_PLUGIN_NAME)) {
            sendMinionsMessage();
            Bukkit.getPluginManager().registerEvents(injector.createInstance(TheMinionsListener.class), plugin);
        }
    }

    private void sendMinionsMessage() {
        logger.info(String.format(MINIONS_HOOKED_MESSAGE, MinionsRegistry.THE_MINIONS_PLUGIN_NAME));
    }
}
