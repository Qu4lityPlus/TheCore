package com.qualityplus.runes.util;

import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.runes.base.rune.Rune;
import com.qualityplus.runes.base.rune.RuneLevel;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class RunesPlaceholderUtils {
    public PlaceholderBuilder getRecipePlaceholders(Rune rune) {
        return PlaceholderBuilder.create(
                new Placeholder("rune_id", rune.getId()),
                new Placeholder("rune_description", rune.getDescription()),
                new Placeholder("rune_displayname", rune.getDisplayName())
        );
    }

    public PlaceholderBuilder placeholderWithRequired(Rune rune, int level) {
        RuneLevel runeLevel = rune.getRuneLevel(level);

        int requiredLevel = Optional.ofNullable(runeLevel).map(RuneLevel::getRequiredRuneCraftingLevel).orElse(1);
        double successChance = Optional.ofNullable(runeLevel).map(RuneLevel::getSuccessChance).orElse(1d);

        return getRecipePlaceholders(rune)
                .with(new Placeholder("rune_level", NumberUtil.toRoman(level)), new Placeholder("rune_required_level", requiredLevel), new Placeholder("rune_success_chance", successChance));
    }

}
