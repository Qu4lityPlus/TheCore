package com.qualityplus.enchanting.base.provider;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.api.provider.EnchantmentProvider;
import com.qualityplus.enchanting.base.config.enchantments.EnchantConfig;
import com.qualityplus.enchanting.base.config.enchantments.advanced.AdvancedEnchantments;
import com.qualityplus.enchanting.base.factory.AEEnchantmentFactory;

import net.advancedplugins.ae.api.AEAPI;
import net.advancedplugins.ae.enchanthandler.enchantments.AdvancedEnchantment;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class AEProvider implements EnchantmentProvider {
    private @Inject AdvancedEnchantments config;

    @Override
    public List<ICoreEnchantment> getEnchantments() {
        return AEAPI.getAllEnchantments()
                .stream()
                .map(this::build)
                .collect(Collectors.toList());
    }

    private ICoreEnchantment build(String id) {
        Optional<EnchantConfig> conf = Optional.ofNullable(config.customAEOptions.getOrDefault(id, null));

        AdvancedEnchantment enchantment =  AEAPI.getEnchantmentInstance(id);

        return AEEnchantmentFactory.builder()
                .identifier(id)
                .requiredPermissionsToEnchant(conf.map(EnchantConfig::getRequiredPermissionsToEnchant).orElse(new HashMap<>()))
                .requiredXpLevelToEnchant(conf.map(EnchantConfig::getRequiredXpLevelToEnchant).orElse(new HashMap<>()))
                .requiredMoneyToEnchant(conf.map(EnchantConfig::getRequiredMoneyToEnchant).orElse(new HashMap<>()))
                .description(enchantment.getDescription())
                .maxLevel(enchantment.getHighestLevel())
                .displayName(enchantment.getName())
                .requiredBookShelf(1)
                .enchantment(null)
                .enabled(true)
                .build()
                .buildVanilla();
    }
}
