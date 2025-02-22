package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public final class BonusAttackSpeedPerk extends Perk {
    @Builder
    public BonusAttackSpeedPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);

    }

    /**
     * TODO ADD A SEPERATED LISTENER
     */

    @Override
    public List<String> getFormattedDescription(double level) {
        List<IPlaceholder> placeholders = PlaceholderBuilder.create()
                .with(new Placeholder("level_number", level), new Placeholder("level_roman", NumberUtil.toRoman((int)level)))
                .with(new Placeholder("percent", chancePerLevel * level))
                .get();
        return StringUtils.processMulti(description, placeholders);
    }
}
