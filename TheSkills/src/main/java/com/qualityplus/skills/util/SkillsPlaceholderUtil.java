package com.qualityplus.skills.util;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.actionbar.ActionBarUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.skills.api.effect.CommonObject;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.registry.Perks;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.stat.Stat;
import com.qualityplus.skills.base.stat.registry.Stats;
import com.qualityplus.skills.persistance.data.UserData;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class SkillsPlaceholderUtil {
    public List<IPlaceholder> getSkillsPlaceholders(UserData userData, Skill skill) {
        double level = userData.getSkills().getLevel(skill.getId());

        return getSkillsPlaceholders(userData, skill, level);
    }

    public List<IPlaceholder> getSkillsPlaceholders(UserData userData, Skill skill, double level) {
        double xp = userData.getSkills().getXp(skill.getId());
        double maxXp = skill.getLevelRequirement((int)level + 1);
        double percentage = ActionBarUtils.getPercentageFromTotal(xp, maxXp);

        return Arrays.asList(
                new Placeholder("skill_displayname", skill.getDisplayName()),
                new Placeholder("skill_description", skill.getDescription()),
                new Placeholder("skill_level_roman", NumberUtil.toRoman((int)level)),
                new Placeholder("skill_level_number", level),
                new Placeholder("skill_level_progress", percentage),
                new Placeholder("skill_action_bar", ActionBarUtils.getReplacedBar(percentage)),
                new Placeholder("skill_xp", xp),
                new Placeholder("skill_max_xp", maxXp)
        );
    }

    public static PlaceholderBuilder getPerksPlaceholders(UserData userData) {
        List<IPlaceholder> placeholders = new ArrayList<>();

        PlaceholderBuilder builder = PlaceholderBuilder.create();

        for (Perk perk : Perks.values())
            placeholders.addAll(getCommonPlaceholders(perk, userData.getSkills().getLevel(perk.getId())));

        builder.with(placeholders);

        return builder;
    }

    public static PlaceholderBuilder getPerksPlaceholders(Integer level) {
        List<IPlaceholder> placeholders = new ArrayList<>();

        PlaceholderBuilder builder = PlaceholderBuilder.create();

        for (Perk perk : Perks.values())
            placeholders.addAll(getCommonPlaceholders(perk, level));

        builder.with(placeholders);

        return builder;
    }

    public static PlaceholderBuilder getStatPlaceholders(UserData userData) {
        List<IPlaceholder> placeholders = new ArrayList<>();

        PlaceholderBuilder builder = PlaceholderBuilder.create();

        for (Stat stat : Stats.values())
            placeholders.addAll(getCommonPlaceholders(stat, userData.getSkills().getLevel(stat.getId())));

        builder.with(placeholders);

        return builder;
    }

    public static PlaceholderBuilder getStatPlaceholders(Integer level) {
        List<IPlaceholder> placeholders = new ArrayList<>();

        PlaceholderBuilder builder = PlaceholderBuilder.create();

        for (Stat stat : Stats.values())
            placeholders.addAll(getCommonPlaceholders(stat, level));

        builder.with(placeholders);

        return builder;
    }

    private static List<IPlaceholder> getCommonPlaceholders(CommonObject object, double level) {
        List<IPlaceholder> placeholders = new ArrayList<>();
        placeholders.add(new Placeholder("skill_level_" + object.getId() + "_number", level));
        placeholders.add(new Placeholder("skill_level_" + object.getId() + "_roman", NumberUtil.toRoman((int)level)));
        placeholders.add(new Placeholder("skill_" + object.getId() + "_displayname", object.getDisplayName()));
        placeholders.add(new Placeholder("skill_" + object.getId() + "_description", object.getFormattedDescription(level)));
        return placeholders;
    }

    public static PlaceholderBuilder getHealthPlaceholders(Player player) {
        return PlaceholderBuilder.create()
                .with(new Placeholder("player_max_health", NumberUtil.toInt(player.getMaxHealth())))
                .with(new Placeholder("player_health", NumberUtil.toInt(player.getHealth())));
    }

    public static PlaceholderBuilder getAllPlaceholders(UserData userData, Skill skill) {
        return PlaceholderBuilder.create()
                .with(getSkillsPlaceholders(userData, skill))
                .with(getPerksPlaceholders(userData))
                .with(getStatPlaceholders(userData));
    }

    public static PlaceholderBuilder getAllPlaceholders(UserData userData, Skill skill, int level) {
        return PlaceholderBuilder.create()
                .with(getSkillsPlaceholders(userData, skill, level))
                .with(getPerksPlaceholders(level))
                .with(getStatPlaceholders(level));
    }
}
