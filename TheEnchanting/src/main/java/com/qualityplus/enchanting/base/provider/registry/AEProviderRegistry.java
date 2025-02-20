package com.qualityplus.enchanting.base.provider.registry;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.enchanting.api.enchantment.CoreEnchants;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.api.provider.EnchantmentProvider;
import com.qualityplus.enchanting.base.config.enchantments.advanced.AdvancedEnchantments;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public final class AEProviderRegistry {
    private @Inject("advancedProvider") EnchantmentProvider provider;
    private @Inject AdvancedEnchantments config;
    private @Inject Logger logger;

    @Delayed(time = 1)
    public void reload() {
        if (!config.loadAllAdvancedEnchantments)
            return;

        List<ICoreEnchantment> enchants = provider.getEnchantments();

        if (enchants.size() <= 0) {
            logger.info("AdvancedEnchantments Wasn't found!");
            return;
        }

        List<ICoreEnchantment> enchantments = enchants
                .stream()
                .filter(enchantment -> !config.blockedEnchants.contains(enchantment.getIdentifier()))
                .collect(Collectors.toList());

        enchantments.forEach(CoreEnchants::registerNewEnchantment);

        logger.info("Successfully registered " + enchantments.size() + " Enchantments From AdvancedEnchantments!");
    }
}
