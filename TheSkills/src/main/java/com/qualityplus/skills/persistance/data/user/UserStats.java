package com.qualityplus.skills.persistance.data.user;

import com.qualityplus.assistant.api.data.Levellable;
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
public final class UserStats extends Document implements Levellable<String, Integer>, LevellableArmorData<String> {
    private Map<String, Integer> level = new HashMap<>();
    private Map<String, Integer> fromArmor = new HashMap<>();

    public void fillIfEmpty() {
        Stats.values().stream().map(Stat::getId).forEach(perk -> level.putIfAbsent(perk, 0));
        Stats.values().stream().map(Stat::getId).forEach(perk -> fromArmor.putIfAbsent(perk, 0));
    }

    public int getTotalLevel(String value) {
        return getLevel(value) + getArmor(value);
    }

    @Override
    public Integer getDefault() {
        return 0;
    }
}