package com.qualityplus.skills.base.perk.perks.common;

import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public abstract class AbstractPotionPerk extends Perk {
    protected int secondsDurationPerLevel;
    protected int baseSecondsDuration;
    protected int level;

    public AbstractPotionPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel,
                              int secondsDurationPerLevel, int baseSecondsDuration, int level) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);

        this.secondsDurationPerLevel = secondsDurationPerLevel;
        this.baseSecondsDuration = baseSecondsDuration;
        this.level = level;
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        return StringUtils.processMulti(super.getFormattedDescription(level), PlaceholderBuilder.create(
                new Placeholder("duration", getDurationSeconds((int)level)),
                new Placeholder("potion_level_roman", NumberUtil.toRoman(getLevel())),
                new Placeholder("potion_level_number", getLevel())
        ).get());
    }

    protected int getFinalLevel() {
        return Math.max(0, level - 1);
    }

    protected int getDurationTicks(double level) {
        return (int) (baseSecondsDuration + (secondsDurationPerLevel * level) * 20);
    }

    private int getDurationSeconds(int level) {
        return baseSecondsDuration + (secondsDurationPerLevel * level);
    }
}
