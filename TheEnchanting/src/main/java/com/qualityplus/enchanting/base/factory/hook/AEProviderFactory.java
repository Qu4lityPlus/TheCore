package com.qualityplus.enchanting.base.factory.hook;

import com.qualityplus.assistant.lib.eu.okaeri.injector.OkaeriInjector;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.enchanting.api.provider.EnchantmentProvider;
import com.qualityplus.enchanting.base.provider.AEProvider;
import com.qualityplus.enchanting.base.provider.VanillaEnchantmentProvider;
import com.qualityplus.enchanting.util.EnchantingAddonsUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Bean;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

@Component
public final class AEProviderFactory {
    private @Inject("injector") OkaeriInjector injector;

    @Bean("advancedProvider")
    private EnchantmentProvider setupEco() {
        if (EnchantingAddonsUtil.ADVANCED_ENCHANTMENTS)
            return injector.createInstance(AEProvider.class);
        else
            return injector.createInstance(VanillaEnchantmentProvider.class);
    }
}
