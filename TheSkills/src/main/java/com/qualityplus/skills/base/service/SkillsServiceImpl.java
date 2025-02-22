package com.qualityplus.skills.base.service;

import com.qualityplus.skills.api.event.SkillEvent;
import com.qualityplus.skills.api.service.SkillsService;
import com.qualityplus.skills.base.event.SkillsLevelUPEvent;
import com.qualityplus.skills.base.event.SkillsXPGainEvent;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.persistance.data.UserData;
import com.qualityplus.skills.persistance.data.user.UserSkills;
import com.qualityplus.skills.util.SkillsPlayerUtil;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public final class SkillsServiceImpl implements SkillsService {
    private final Map<UUID, UserData> dataMap = new HashMap<>();

    @Override
    public Optional<UserData> getData(UUID uuid) {
        return Optional.ofNullable(dataMap.getOrDefault(uuid, null));
    }

    @Override
    public void addData(UserData data) {
        dataMap.put(data.getUuid(), data);
    }

    @Override
    public void removeData(UserData data) {
        dataMap.remove(data.getUuid());
    }

    @Override
    public void addXp(Player player, boolean useMulti, boolean showXpActionBar, Skill skill, double xp) {
        double toAdd = Math.abs(xp * (useMulti ? SkillsPlayerUtil.getXpMultiplier(player) : 1));

        SkillsXPGainEvent skillsXPGainEvent = new SkillsXPGainEvent(player, skill, toAdd, showXpActionBar);

        Bukkit.getPluginManager().callEvent(skillsXPGainEvent);

        if (skillsXPGainEvent.isCancelled()) return;

        toAdd = skillsXPGainEvent.getXp();

        UserSkills skills = getData(player.getUniqueId())
                .map(UserData::getSkills)
                .orElse(new UserSkills());

        double level = skills.getLevel(skill.getId());

        skills.addXp(skill.getId(), toAdd);

        if (skills.getXp(skill.getId()) >= skill.getLevelRequirement((int)level + 1) && level + 1 <= skill.getMaxLevel()) {
            double remaining = skills.getXp(skill.getId()) - skill.getLevelRequirement((int)level + 1);
            skills.setXp(skill.getId(), 0.0);
            skills.addLevel(skill.getId(),  1D);
            SkillEvent levelUpEvent = new SkillsLevelUPEvent(player, skill, (int)level + 1);
            Bukkit.getPluginManager().callEvent(levelUpEvent);

            if (remaining > 0) addXp(player, false, false, skill, remaining);
        }
    }
}
