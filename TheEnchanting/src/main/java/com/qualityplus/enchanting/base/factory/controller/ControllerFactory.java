package com.qualityplus.enchanting.base.factory.controller;

import com.qualityplus.enchanting.api.handler.EnchantmentController;
import com.qualityplus.enchanting.base.controller.extensions.VanillaExtensionController;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Bean;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.inventory.ItemStack;

@Component
public final class ControllerFactory {
    @Bean("normalController")
    public EnchantmentController<ItemStack> createNormalController() {
        return new VanillaExtensionController();
    }

    @Bean("advancedController")
    public EnchantmentController<ItemStack> createAdvancedController() {
        return new VanillaExtensionController();
    }

}
