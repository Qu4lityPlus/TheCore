package com.qualityplus.runes.util;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.runes.TheRunes;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.session.RemoveSession;
import com.qualityplus.runes.api.session.RuneInstance;
import com.qualityplus.runes.api.session.RuneSession;
import com.qualityplus.runes.api.session.RuneSession.SessionResult;
import com.qualityplus.runes.base.rune.Rune;
import com.qualityplus.runes.base.rune.RuneLevel;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class RuneFinderUtil {
    public int getItemLevel(ItemStack itemStack, Enchantment enchantment) {
        if (itemStack == null || enchantment == null) return 0;

        ItemMeta meta = itemStack.getItemMeta();

        return Optional.ofNullable(meta).map(m -> m.getEnchantLevel(enchantment)).orElse(0);
    }

    public RemoveSession.RemoveSessionResult getAnswer(RemoveSession session) {
        boolean toUpgradeIsNull = BukkitItemUtil.isNull(session.getItemToRemove());

        if (toUpgradeIsNull) return RemoveSession.RemoveSessionResult.ITEM_IS_NOT_RUNED;

        ItemStack toUpgrade = session.getItemToRemove();

        if (!RunesUtils.isRunedItem(toUpgrade))
            return RemoveSession.RemoveSessionResult.ITEM_IS_NOT_RUNED;

        return RemoveSession.RemoveSessionResult.ITEM_SET;
    }

    public RuneSession.SessionResult getAnswer(RuneSession session) {
        Player player = Bukkit.getPlayer(session.getUuid());

        boolean toUpgradeIsNull = BukkitItemUtil.isNull(session.getItemToUpgrade());
        boolean toSacrificeIsNull = BukkitItemUtil.isNull(session.getItemToSacrifice());

        if (toSacrificeIsNull && toUpgradeIsNull) return SessionResult.NOTHING_SET;

        if (!toUpgradeIsNull && toSacrificeIsNull) return SessionResult.ONLY_ITEM_TO_UPGRADE;

        if (toUpgradeIsNull) return SessionResult.ONLY_ITEM_TO_SACRIFICE;

        ItemStack toUpgrade = session.getItemToUpgrade();
        ItemStack toSacrifice = session.getItemToSacrifice();


        if (!RunesUtils.isRune(toSacrifice))
            return SessionResult.ERROR_CANNOT_COMBINE_THOSE_ITEMS;

        Optional<RuneInstance> second = Optional.ofNullable(RunesUtils.getRuneInstance(toSacrifice));

        if (RunesUtils.isRune(toUpgrade) && RunesUtils.isRune(toSacrifice)) {
            Optional<RuneInstance> first = Optional.ofNullable(RunesUtils.getRuneInstance(toUpgrade));

            if (!first.map(RuneInstance::getRune)
                    .map(Rune::getId).orElse("")
                    .equals(second
                    .map(RuneInstance::getRune)
                    .map(Rune::getId)
                    .orElse("")))
                return SessionResult.MUST_BE_SAME_TYPE;

            if (!first.map(RuneInstance::getLevel).orElse(0).equals(second.map(RuneInstance::getLevel).orElse(0)))
                return SessionResult.MUST_BE_SAME_TIER;

            return SessionResult.BOTH_RUNES_SET;
        } else {
            RuneInstance first = RunesUtils.getRuneItemInstance(toUpgrade);

            String runeId = second.map(RuneInstance::getRune).map(Rune::getId).orElse("");
            int runeLevel = second.map(RuneInstance::getLevel).orElse(0);

            if (first.getRune() != null) {
                if (first.getRune().getId().equals(runeId) && runeLevel < first.getLevel())
                    return SessionResult.CANNOT_ADD_THAT_RUNE;
            }

            if (!second.map(RuneInstance::getRune).map(rune -> rune.getEffect().canBeAppliedIn(toUpgrade)).orElse(false))
                return SessionResult.INCOMPATIBLE_RUNE_APPLY;

            int requiredLevel = second
                    .map(RuneInstance::getRune)
                    .map(rune -> rune.getOptRuneLevel(runeLevel).map(RuneLevel::getRequiredRuneCraftingLevel).orElse(0))
                    .orElse(0);

            if (TheRunes.getApi().getLevelProvider().getLevel(player) < requiredLevel)
                return SessionResult.NO_RUNE_CRAFTING_LEVEL;

            return SessionResult.RUNE_AND_ITEM_SET;
        }
    }

    public boolean hasRequiredLevel(RuneSession session, Player player) {
        int level = session.getRuneInstance().getLevel();

        int newLevel = session.getSessionResult().equals(SessionResult.BOTH_RUNES_SET) ? level * 2 : level;

        int requiredLevel = session.getRuneInstance().getRune()
                .getOptRuneLevel(newLevel)
                .map(RuneLevel::getRequiredRuneCraftingLevel)
                .orElse(0);

        return TheRunes.getApi().getLevelProvider().getLevel(player) >= requiredLevel;
    }


    public List<String> getAddLore(String addLore, int level) {
        return Arrays.asList(StringUtils.color(addLore).replaceAll("%rune_level%", NumberUtil.toRoman(level)).split("\\n"));
    }

    public ItemStack getFinalItem(Box box, RuneSession session) {
        RuneInstance sessionLevelled = session.getRuneInstance();

        if (session.getSessionResult().equals(SessionResult.RUNE_AND_ITEM_SET)) {
            ItemStack newItem = removeRuneFromItem(session.getItemToUpgrade().clone());

            ItemMeta meta = newItem.getItemMeta();

            List<String> lore = BukkitItemUtil.getItemLore(newItem);

            try {
                lore.addAll(getAddLore(sessionLevelled.getRune().getToAddLore(), sessionLevelled.getLevel()));

                meta.addEnchant(GlowFinderUtil.getEnchantment(), 1, true);

                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            } catch (Exception e) {e.printStackTrace();}

            meta.setLore(lore);

            newItem.setItemMeta(meta);

            return RunesUtils.addNBTData(newItem, sessionLevelled.getRune().getId(), sessionLevelled.getLevel());
        } else {
            return RunesUtils.makeRune(box, sessionLevelled.getRune(), sessionLevelled.getLevel() + 1);
        }
    }

    public ItemStack removeRuneFromItem(ItemStack itemStack) {
        return RunesUtils.removeRuneFromItem(itemStack);
    }
}
