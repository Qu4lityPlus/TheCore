package com.qualityplus.anvil.base.provider;

import com.qualityplus.anvil.api.session.AnvilSession;
import com.qualityplus.anvil.base.config.Config;
import com.qualityplus.anvil.base.requirement.VanillaEnchantRequirement;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XEnchantment;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.util.map.MapUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class VanillaEnchantingProvider extends CommonEnchantmentProvider {
    private @Inject Config config;

    @Override
    public AnvilSession.SessionResult getAnswer(final AnvilSession session) {
        final AnvilSession.SessionResult commonResult = getCommonAnswer(session);

        if (commonResult != null) {
            return commonResult;
        }

        if (getEnchantments(session.getItemToSacrifice())
                .keySet()
                .stream()
                .anyMatch(itemToSacrificeEnch -> getIncompatibility(session.getItemToUpgrade(), itemToSacrificeEnch).isPresent()))
            return AnvilSession.SessionResult.ERROR_CONFLICT_ENCHANTMENTS;

        return AnvilSession.SessionResult.BOTH_ITEMS_SET;
    }

    @Override
    public ItemStack getFinalItem(AnvilSession session) {
        ItemStack newItem = session.getItemToUpgrade().clone();

        for (Map.Entry<Enchantment, Integer> entry : getNewEnchantments(session).entrySet()) {
            Enchantment enchantment = entry.getKey();

            newItem = addEnchantment(newItem, new VanillaEnchantmentSession(enchantment, entry.getValue()));
        }

        return newItem;
    }

    @Override
    public double getMoneyCost(AnvilSession session) {
        return 0;
    }

    @Override
    public double getLevelsCost(AnvilSession session) {
        Map<Integer, Integer> levelsReference = FastMap.builder(Integer.class, Integer.class)
                .put(1, 2)
                .put(2, 4)
                .put(3, 6)
                .put(4, 8)
                .put(5, 10)
                .build();

        return getNewEnchantments(session)
                .entrySet()
                .stream()
                .map(entry -> {

                    XEnchantment enchantment = XEnchantment.matchXEnchantment(entry.getKey());

                    Map<Integer, Integer> requirement = Optional.ofNullable(config.requiredLevelsForVanilla.getOrDefault(enchantment, null))
                            .map(VanillaEnchantRequirement::getRequiredLevelsToEnchant)
                            .filter(Objects::nonNull)
                            .orElse(levelsReference);

                    return (double) requirement.get(entry.getValue());
                })
                .reduce(0D, Double::sum);
    }

    @Override
    public boolean dontHavePermission(AnvilSession session, Player player) {
        return false;
    }


    private Optional<Enchantment> getIncompatibility(ItemStack itemStack, Enchantment enchantment) {
        return getEnchantments(itemStack)
                .keySet()
                .stream()
                .filter(itemEnch -> itemEnch.equals(enchantment) != enchantment.conflictsWith(itemEnch))
                .findFirst();
    }


    public ItemStack addEnchantment(ItemStack itemStack, VanillaEnchantmentSession session) {
        Optional<Enchantment> incompatibility = getIncompatibility(itemStack, session.getEnchantment());

        Map<Enchantment, Integer> enchantmentList = getEnchantments(itemStack);

        //Remove All Enchantments
        removeAllEnchantments(itemStack, enchantmentList);

        //Add new Enchantment
        enchantmentList.put(session.getEnchantment(), session.getLevel());
        //Remove Incompatibility Enchantment
        incompatibility.ifPresent(enchantmentList::remove);

        //Add All Enchantments
        addAllEnchantments(itemStack, enchantmentList);

        /*ItemMeta meta = itemStack.getItemMeta();

        Optional.ofNullable(meta).ifPresent(m -> m.getItemFlags().remove(ItemFlag.HIDE_ENCHANTS));

        itemStack.setItemMeta(meta);*/

        return itemStack;
    }

    private void removeAllEnchantments(ItemStack itemStack, Map<Enchantment, Integer> toRemove) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta instanceof EnchantmentStorageMeta)
            toRemove.forEach((key, value) -> ((EnchantmentStorageMeta) meta).removeStoredEnchant(key));
        else
            toRemove.forEach((key, value) -> Optional.ofNullable(meta).ifPresent(m -> m.removeEnchant(key)));

        itemStack.setItemMeta(meta);
    }

    private void addAllEnchantments(ItemStack itemStack, Map<Enchantment, Integer> toAdd) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta instanceof EnchantmentStorageMeta) {
            toAdd.forEach((ench, level) -> ((EnchantmentStorageMeta) meta).addStoredEnchant(ench, level, false));
        } else {
            toAdd.forEach((ench, level) -> Optional.ofNullable(meta).ifPresent(m -> m.addEnchant(ench, level, false)));
        }

        itemStack.setItemMeta(meta);
    }

    private Map<Enchantment, Integer> getNewEnchantments(AnvilSession session) {
        Map<Enchantment, Integer> toUpgradeEnchants = getEnchantments(session.getItemToUpgrade());
        Map<Enchantment, Integer> toSacrificeEnchants = getEnchantments(session.getItemToSacrifice());

        Map<Enchantment, Integer> newEnchants = new HashMap<>();

        for (Enchantment enchantment : toSacrificeEnchants.keySet()) {

            if (!toUpgradeEnchants.containsKey(enchantment)) {
                newEnchants.put(enchantment, toSacrificeEnchants.get(enchantment));
            } else {
                int sacrificeLevel = toSacrificeEnchants.get(enchantment);
                int upgradeLevel = toUpgradeEnchants.get(enchantment);

                if (upgradeLevel + sacrificeLevel > enchantment.getMaxLevel()) continue;

                newEnchants.put(enchantment, upgradeLevel + sacrificeLevel);
            }
        }

        return newEnchants;
    }

    private Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        return meta instanceof EnchantmentStorageMeta ? new HashMap<>(((EnchantmentStorageMeta) meta).getStoredEnchants()): MapUtils.check(new HashMap<>(meta.getEnchants()));
    }

    @Data
    @Builder
    @AllArgsConstructor
    private static class VanillaEnchantmentSession {
        private Enchantment enchantment;
        private int level;
    }
}
