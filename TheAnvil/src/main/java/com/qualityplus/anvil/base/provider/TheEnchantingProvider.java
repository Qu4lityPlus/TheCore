package com.qualityplus.anvil.base.provider;

import com.qualityplus.anvil.api.session.AnvilSession.SessionResult;
import com.qualityplus.anvil.api.session.AnvilSession;
import com.qualityplus.enchanting.TheEnchanting;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.base.session.EnchantmentSessionImpl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class TheEnchantingProvider extends CommonEnchantmentProvider {
    @Override
    public SessionResult getAnswer(AnvilSession session) {
        SessionResult commonResult = getCommonAnswer(session);

        if (commonResult != null) return commonResult;

        if (TheEnchanting.getApi().getEnchantments(session.getItemToSacrifice())
                .keySet()
                .stream()
                .anyMatch(enchantment -> TheEnchanting.getApi().getIncompatibility(session.getItemToUpgrade(), enchantment).isPresent()))
            return AnvilSession.SessionResult.ERROR_CONFLICT_ENCHANTMENTS;

        return AnvilSession.SessionResult.BOTH_ITEMS_SET;
    }

    @Override
    public ItemStack getFinalItem(final AnvilSession session) {
        ItemStack newItem = session.getItemToUpgrade().clone();

        for (Map.Entry<ICoreEnchantment, Integer> entry : getNewEnchantments(session).entrySet())
            newItem = TheEnchanting.getApi().addEnchantment(newItem, new EnchantmentSessionImpl(entry.getKey(), entry.getValue()));

        return newItem;
    }

    @Override
    public double getMoneyCost(AnvilSession session) {
        return getNewEnchantments(session)
                .entrySet()
                .stream()
                .map(entry -> entry.getKey().getRequiredMoneyToEnchant(entry.getValue()))
                .reduce(0D, Double::sum);
    }

    @Override
    public double getLevelsCost(AnvilSession session) {
        return getNewEnchantments(session)
                .entrySet()
                .stream()
                .map(entry -> entry.getKey().getRequiredLevelToEnchant(entry.getValue()))
                .reduce(0D, Double::sum);
    }

    @Override
    public boolean dontHavePermission(AnvilSession session, Player player) {
        return getNewEnchantments(session)
                .entrySet()
                .stream()
                .anyMatch(entry -> !entry.getKey().canEnchant(player, entry.getValue()));
    }

    public Map<ICoreEnchantment, Integer> getNewEnchantments(AnvilSession session) {
        Map<ICoreEnchantment, Integer> toUpgradeEnchants = TheEnchanting.getApi().getEnchantments(session.getItemToUpgrade());
        Map<ICoreEnchantment, Integer> toSacrificeEnchants = TheEnchanting.getApi().getEnchantments(session.getItemToSacrifice());

        Map<ICoreEnchantment, Integer> newEnchants = new HashMap<>();

        for (ICoreEnchantment enchantment : toSacrificeEnchants.keySet()) {
            if (!toUpgradeEnchants.containsKey(enchantment)) {
                newEnchants.put(enchantment, toSacrificeEnchants.get(enchantment));
            } else {
                int sacrificeLevel = toSacrificeEnchants.get(enchantment);
                int upgradeLevel = toUpgradeEnchants.get(enchantment);

                if (sacrificeLevel != upgradeLevel) continue;

                int newLevel = upgradeLevel + 1;

                if (newLevel > enchantment.getMaxLevel()) continue;

                newEnchants.put(enchantment, newLevel);
            }
        }

        return newEnchants;
    }
}
