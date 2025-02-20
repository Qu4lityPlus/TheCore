package com.qualityplus.enchanting.base.controller.extensions;

import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.api.handler.EnchantmentController;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

@Component()
public final class AdvancedExtensionController implements EnchantmentController<ItemStack> {
    @Override
    public ItemStack addEnchantment(final ItemStack itemStack, final ICoreEnchantment enchantment, final int level) {
        final ItemMeta meta = itemStack.getItemMeta();

        final Enchantment vanillaEnchantment = enchantment.getEnchantment();


        if (meta instanceof EnchantmentStorageMeta)
            ((EnchantmentStorageMeta) meta).addStoredEnchant(vanillaEnchantment, level, false);
        else
            Optional.ofNullable(meta).ifPresent(m -> m.addEnchant(vanillaEnchantment, level, false));

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    @Override
    public ItemStack removeEnchantment(ItemStack itemStack, ICoreEnchantment enchantment) {
        final ItemMeta meta = itemStack.getItemMeta();

        final Enchantment vanillaEnchantment = enchantment.getEnchantment();

        if (meta instanceof EnchantmentStorageMeta)
            ((EnchantmentStorageMeta) meta).removeStoredEnchant(vanillaEnchantment);
        else
            Optional.ofNullable(meta).ifPresent(m -> m.removeEnchant(vanillaEnchantment));

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
