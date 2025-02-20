package com.qualityplus.enchanting.base.controller;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.api.handler.EnchantmentController;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.inventory.ItemStack;

@Component
public final class BridgeEnchantmentController implements EnchantmentController<ItemStack> {
    private @Inject("normalController") EnchantmentController<ItemStack> normalController;
    private @Inject("advancedController") EnchantmentController<ItemStack> advancedController;

    @Override
    public ItemStack addEnchantment(final ItemStack itemStack, final ICoreEnchantment enchantment, final int level) {
        if (enchantment.isAE())
            return advancedController.addEnchantment(itemStack, enchantment, level);
        else
            return normalController.addEnchantment(itemStack, enchantment, level);
    }

    @Override
    public ItemStack removeEnchantment(ItemStack itemStack, ICoreEnchantment enchantment) {
        if (enchantment.isAE())
            return advancedController.removeEnchantment(itemStack, enchantment);
        else
            return normalController.removeEnchantment(itemStack, enchantment);
    }
}
