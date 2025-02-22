package com.qualityplus.skills.base.perk;

import com.qualityplus.assistant.api.util.MathUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.api.effect.CommonObject;
import com.qualityplus.skills.api.listener.ExtraListener;
import com.qualityplus.skills.api.registry.ListenerRegistrable;
import com.qualityplus.skills.base.perk.registry.Perks;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.persistance.data.UserData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public abstract class Perk extends CommonObject implements ListenerRegistrable {
    protected double chancePerLevel;

    public Perk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount);

        this.chancePerLevel = chancePerLevel;
    }

    public void register() {
        Perks.registerNewPerk(this);
    }

    public double getStat(Player player, String id) {
        return TheSkills.getApi().getSkillsService().getData(player.getUniqueId())
                .map(UserData::getSkills)
                .map(userPerks -> userPerks.getLevel(id))
                .orElse(1D);
    }

    public double getStat(Player player) {
        return getStat(player, id);
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        return StringUtils.processMulti(description, getPlaceholders(level).get());
    }

    @Override
    public void addExtraListener(Class<? extends ExtraListener> listener) {
        extraListeners.add(listener);
    }

    protected PlaceholderBuilder getPlaceholders(double level) {
        return PlaceholderBuilder.create()
                .with(new Placeholder("level_number", level), new Placeholder("level_roman", NumberUtil.toRoman((int)level)))
                .with(new Placeholder("percent", MathUtil.round(getChancePerLevel() * level)));
    }

}