package com.qualityplus.enchanting.base.provider.registry;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.enchanting.api.enchantment.CoreEnchants;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.api.provider.EnchantmentProvider;
import com.qualityplus.enchanting.base.config.enchantments.eco.EcoEnchantments;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public final class EcoProviderRegistry {
    private @Inject("ecoProvider") EnchantmentProvider provider;
    private @Inject EcoEnchantments config;
    private @Inject Logger logger;

    @Delayed(time = 1)
    public void reload() {
        if (!this.config.loadAllEcoEnchants) {
            return;
        }

        final List<ICoreEnchantment> enchants = this.provider.getEnchantments();

        if (enchants.isEmpty()) {
            this.logger.info("EcoEnchant Wasn't found!");
            return;
        }

        List<ICoreEnchantment> enchantments = enchants
                .stream()
                .filter(enchantment -> !this.config.blockedEnchants.contains(enchantment.getIdentifier()))
                .toList();

        enchantments.forEach(CoreEnchants::registerNewEnchantment);

        this.logger.info("Successfully registered " + enchantments.size() + " Enchantments From EcoEnchants!");
    }
}
