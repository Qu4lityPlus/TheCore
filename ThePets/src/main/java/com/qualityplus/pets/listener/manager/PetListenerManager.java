package com.qualityplus.pets.listener.manager;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.lib.eu.okaeri.injector.OkaeriInjector;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.pets.listener.BlockBreakListener;
import com.qualityplus.pets.listener.skills.SkillsListener;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@Component
public final class PetListenerManager {
    private @Inject("injector") OkaeriInjector injector;
    private @Inject Plugin plugin;

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void registerListener() {
        PluginManager manager = Bukkit.getPluginManager();

        if (TheAssistantPlugin.getAPI().getDependencyResolver().isPlugin("TheSkills"))
            manager.registerEvents(injector.createInstance(SkillsListener.class), plugin);
        else
            manager.registerEvents(injector.createInstance(BlockBreakListener.class), plugin);
    }
}
