package com.qualityplus.skills.base;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.skills.api.TheSkillsAPI;
import com.qualityplus.skills.api.service.SkillsService;
import com.qualityplus.skills.base.stat.Stat;
import com.qualityplus.skills.base.stat.registry.Stats;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public final class TheSkillsAPIImpl implements TheSkillsAPI {
    private static final String NBT_STAT_KEY = "SKILL_STAT_";
    private @Getter @Inject SkillsService skillsService;
    private @Getter @Inject Plugin plugin;

    @Override
    public ItemStack setItemStats(ItemStack itemStack, Map<Stat, Integer> stats) {
        if (BukkitItemUtil.isNull(itemStack)) return itemStack;

        NBTItem item = new NBTItem(itemStack);

        stats.forEach((key, value) -> item.setInteger(key.getId(), value));

        return item.getItem();
    }

    @Override
    public Map<Stat, Integer> getItemStats(ItemStack itemStack) {
        if (BukkitItemUtil.isNull(itemStack)) return new HashMap<>();

        NBTItem item = new NBTItem(itemStack);

        return item.getKeys().stream()
                .filter(key -> key.contains(NBT_STAT_KEY))
                .map(key -> Stats.getByID(key.replace(NBT_STAT_KEY, "")))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(stat -> stat, stat -> item.getInteger(NBT_STAT_KEY + stat.getId())));
    }
}
