package com.qualityplus.skills.persistance.data.user;

import com.qualityplus.assistant.api.data.Levellable;
import com.qualityplus.assistant.api.data.Progressable;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Exclude;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.registry.Perks;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.registry.Skills;
import com.qualityplus.skills.base.stat.Stat;
import com.qualityplus.skills.base.stat.registry.Stats;
import com.qualityplus.skills.persistance.data.user.armor.LevellableArmorData;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public final class UserSkills extends Document implements Levellable<String, Double>, Progressable<String, Double>,
        LevellableArmorData<String> {
    @Exclude
    private Map<String, Integer> fromArmor = new HashMap<>();
    private Map<String, Double> level = new HashMap<>();
    private Map<String, Double> xp = new HashMap<>();

    public void fillIfEmpty() {
        Skills.values().stream().map(Skill::getId).forEach(skill -> level.putIfAbsent(skill, 0D));
        Skills.values().stream().map(Skill::getId).forEach(skill -> xp.putIfAbsent(skill, 0D));
        Skills.values().stream().map(Skill::getId).forEach(skill -> fromArmor.putIfAbsent(skill, 0));

        //Perks
        Perks.values().stream().map(Perk::getId).forEach(perk -> level.putIfAbsent(perk, 0D));
        Perks.values().stream().map(Perk::getId).forEach(perk -> fromArmor.putIfAbsent(perk, 0));
        //Stats
        Stats.values().stream().map(Stat::getId).forEach(perk -> level.putIfAbsent(perk, 0D));
        Stats.values().stream().map(Stat::getId).forEach(perk -> fromArmor.putIfAbsent(perk, 0));
    }

    @Override
    public Double getDefault() {
        return 0D;
    }

    @Override
    public Double getDefaultXp() {
        return 0D;
    }
}