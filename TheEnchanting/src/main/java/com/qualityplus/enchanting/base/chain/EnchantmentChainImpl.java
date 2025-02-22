package com.qualityplus.enchanting.base.chain;

import com.google.common.collect.Lists;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.enchanting.TheEnchanting;
import com.qualityplus.enchanting.api.chain.EnchantmentChain;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.api.session.EnchantmentSession;
import com.qualityplus.enchanting.base.config.Config;
import com.qualityplus.enchanting.util.EnchantingPlaceholderUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class EnchantmentChainImpl implements EnchantmentChain {
    private @Inject Config config;

    @Override
    public ItemStack addEnchantment(ItemStack itemStack, EnchantmentSession session) {
        Optional<ICoreEnchantment> incompatibility = TheEnchanting.getApi().getIncompatibility(itemStack, session.getEnchantment());

        Map<ICoreEnchantment, Integer> enchantmentList = TheEnchanting.getApi().getEnchantments(itemStack);

        checkIfShouldBeHidden(itemStack);

        //Remove All Enchantments
        removeAllEnchantments(itemStack, enchantmentList);
        //Remove All Enchantment from Lore
        removeAllFromLore(itemStack, new ArrayList<>(enchantmentList.keySet()));

        //Add new Enchantment
        enchantmentList.put(session.getEnchantment(), session.getLevel());
        //Remove Incompatibility Enchantment
        incompatibility.ifPresent(enchantmentList::remove);

        //Add All Enchantments
        addAllEnchantments(itemStack, enchantmentList);

        //Add All Enchantments to Lore
        if (config.enchantmentsDisplay.displayEnchantmentsInLore)
            addAllToLore(itemStack, enchantmentList);


        return itemStack;
    }

    @Override
    public ItemStack removeEnchantment(ItemStack itemStack, EnchantmentSession session) {
        Map<ICoreEnchantment, Integer> enchantmentList = TheEnchanting.getApi().getEnchantments(itemStack);

        checkIfShouldBeHidden(itemStack);

        //Remove All Enchantments
        removeAllEnchantments(itemStack, enchantmentList);
        //Remove All Enchantment from Lore
        removeAllFromLore(itemStack, new ArrayList<>(enchantmentList.keySet()));

        enchantmentList.remove(session.getEnchantment());

        //Add All Enchantments
        addAllEnchantments(itemStack, enchantmentList);
        //Add All Enchantments to Lore
        if (config.enchantmentsDisplay.displayEnchantmentsInLore)
            addAllToLore(itemStack, enchantmentList);

        return itemStack;
    }

    private void checkIfShouldBeHidden(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        if (config.enchantmentsDisplay.hideVanillaEnchantments)
            Optional.ofNullable(meta).ifPresent(m -> m.addItemFlags(ItemFlag.HIDE_ENCHANTS));
        else
            Optional.ofNullable(meta).ifPresent(m -> m.removeItemFlags(ItemFlag.HIDE_ENCHANTS));

        itemStack.setItemMeta(meta);
    }

    private void removeAllFromLore(ItemStack itemStack, List<ICoreEnchantment> toRemove) {
        ItemMeta meta = itemStack.getItemMeta();

        List<String> newLore = new ArrayList<>();

        for (String line : Optional.ofNullable(meta.getLore()).orElse(new ArrayList<>())) {
            String uncolored = StringUtils.unColor(line);

            boolean addLine = true;

            for (ICoreEnchantment enchantment : toRemove) {
                String displayName = StringUtils.unColor(enchantment.getName());
                String description = StringUtils.unColor(enchantment.getDescription());

                //Si la linea contiene el nombre
                if (uncolored.contains(displayName))
                    addLine = false;

                //Si la description contiene la linea o Si la linea contiene la description
                if (description.contains(uncolored) || uncolored.contains(description))
                    addLine = false;
            }

            if (addLine) newLore.add(line);
        }

        meta.setLore(newLore);

        itemStack.setItemMeta(meta);
    }

    private void removeAllEnchantments(ItemStack itemStack, Map<ICoreEnchantment, Integer> toRemove) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta instanceof EnchantmentStorageMeta)
            translate(toRemove).forEach((key, value) -> ((EnchantmentStorageMeta) meta).removeStoredEnchant(key));
        else
            translate(toRemove).forEach((key, value) -> Optional.ofNullable(meta).ifPresent(m -> m.removeEnchant(key)));

        itemStack.setItemMeta(meta);
    }

    private void addAllEnchantments(ItemStack itemStack, Map<ICoreEnchantment, Integer> toAdd) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta instanceof EnchantmentStorageMeta)
            translate(toAdd).forEach((ench, level) -> ((EnchantmentStorageMeta) meta).addStoredEnchant(ench, level, false));
        else
            translate(toAdd).forEach((ench, level) -> Optional.ofNullable(meta).ifPresent(m -> m.addEnchant(ench, level, false)));

        itemStack.setItemMeta(meta);
    }

    private void addAllToLore(ItemStack itemStack, Map<ICoreEnchantment, Integer> toAdd) {
        ItemMeta meta = itemStack.getItemMeta();

        List<String> toAddLore = toAdd.size() > config.enchantmentsDisplay.maxEnchantsInLore ? getShrinkLore(toAdd) : getNoShrinkLore(toAdd);

        List<String> finalLore = Optional.ofNullable(meta.getLore()).orElse(new ArrayList<>());

        Config.LoreLocation location = config.enchantmentsDisplay.loreLocation;

        if (location == Config.LoreLocation.UNDER_ALL && !finalLore.isEmpty())
            finalLore.addAll(0, toAddLore);
        else if (location == Config.LoreLocation.LINE && config.enchantmentsDisplay.loreLine < finalLore.size())
            finalLore.addAll(config.enchantmentsDisplay.loreLine, toAddLore);
        else if (location == Config.LoreLocation.ABOVE_ALL && !finalLore.isEmpty())
            finalLore.addAll(toAddLore);
        else
            finalLore = toAddLore;

        meta.setLore(finalLore);

        itemStack.setItemMeta(meta);

        ItemStackUtils.parseWrappedLore(itemStack, config.loreWrapper);
    }

    private List<String> getNoShrinkLore(Map<ICoreEnchantment, Integer> enchantments) {
        List<String> lore = new ArrayList<>();

        enchantments.forEach((key, value) -> lore.addAll(
                StringUtils.processMulti(config.enchantmentsDisplay.enchantmentFormat, EnchantingPlaceholderUtil.getEnchantBuilder(key, value)
                        .with(new Placeholder("enchanting_enchant_level_roman", NumberUtil.toRoman(value)))
                        .with(new Placeholder("enchanting_enchant_level_number", value))
                        .get())
        ));

        return lore;
    }

    private List<String> getShrinkLore(Map<ICoreEnchantment, Integer> enchantments) {
        List<String> shrinkLore = new ArrayList<>();

        if (enchantments.size() < config.enchantmentsDisplay.enchantsPerLineIfExceed) return shrinkLore;

        List<String> names = enchantments
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().isVanilla())
                .map(entry -> StringUtils.processMulti(config.enchantmentsDisplay.enchantmentShrinkFormat, EnchantingPlaceholderUtil.getEnchantBuilder(entry.getKey(), entry.getValue())
                        .with(new Placeholder("enchanting_enchant_level_roman", NumberUtil.toRoman(entry.getValue())))
                        .with(new Placeholder("enchanting_enchant_level_number", entry.getValue()))
                        .get()))
                .collect(Collectors.toList());

        List<List<String>> shrinkNames = Lists.partition(names, config.enchantmentsDisplay.enchantsPerLineIfExceed);

        shrinkNames.forEach(list -> {
            StringBuilder builder = new StringBuilder();

            list.forEach(line -> builder.append(line).append(config.enchantmentsDisplay.enchantmentSeparator));

            String line = builder.toString();

            line = line.substring(0, line.length() - 2);

            shrinkLore.add(StringUtils.color(line));
        });

        return shrinkLore;
    }

    private Map<Enchantment, Integer> translate(Map<ICoreEnchantment, Integer> enchantments) {
        return enchantments.keySet().stream().collect(Collectors.toMap(ICoreEnchantment::getEnchantment, enchantments::get));
    }
}
