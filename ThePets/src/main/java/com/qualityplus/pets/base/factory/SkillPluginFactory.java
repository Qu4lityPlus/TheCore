package com.qualityplus.pets.base.factory;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.pets.api.skills.SkillDependency;
import com.qualityplus.pets.base.skills.AureliumSkillsPlugin;
import com.qualityplus.pets.base.skills.EmptySkillsPlugin;
import com.qualityplus.pets.base.skills.TheSkillsPlugin;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Bean;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.logging.Logger;

@Component
public final class SkillPluginFactory {
    private @Inject Logger logger;

    @Bean
    public SkillDependency getSkillDependency() {
        if (TheAssistantPlugin.getAPI().getDependencyResolver().isPlugin("TheSkills")) {
            logger.info("TheSkills Plugin hook created for stats!");
            return new TheSkillsPlugin();
        } else if (TheAssistantPlugin.getAPI().getDependencyResolver().isPlugin("AureliumSkills")) {
            logger.info("AureliumSkills Plugin hook created for stats!");
            return new AureliumSkillsPlugin();
        } else {
            logger.warning("No Skills Plugin hook was found for stats!");
            return new EmptySkillsPlugin();
        }
    }
}
