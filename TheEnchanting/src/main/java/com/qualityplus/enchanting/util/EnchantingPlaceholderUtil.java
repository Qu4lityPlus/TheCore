package com.qualityplus.enchanting.util;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class EnchantingPlaceholderUtil {

    public PlaceholderBuilder getEnchantBuilder(ICoreEnchantment enchantment, int level) {

        return PlaceholderBuilder.create(
                new Placeholder("enchanting_enchant_displayname", enchantment.getName()),
                new Placeholder("enchanting_enchant_description", getDescription(enchantment, level)),
                new Placeholder("enchanting_enchant_required_bookshelf", enchantment.getRequiredBookShelf())
        );
    }

    public List<IPlaceholder> getEnchantPlaceholders(ICoreEnchantment enchantment, int level) {
        return getEnchantBuilder(enchantment, level).get();
    }

    private String getDescription(ICoreEnchantment enchantment, int level) {
        return enchantment.isVanilla() ? enchantment.getDescription() : enchantment.getDescriptionPerLevel(level);
    }

    private String emptyIfNull(String input) {
        return input == null || input.equals("") ? "&cEmpty" : input;
    }
}
