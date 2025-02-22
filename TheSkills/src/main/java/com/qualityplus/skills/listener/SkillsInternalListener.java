package com.qualityplus.skills.listener;

import com.qualityplus.assistant.api.config.ConfigActionBar;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.sound.SoundUtils;
import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.base.config.Config;
import com.qualityplus.skills.base.event.SkillsLevelUPEvent;
import com.qualityplus.skills.base.event.SkillsXPGainEvent;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.persistance.data.UserData;
import com.qualityplus.skills.util.SkillsPlaceholderUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class SkillsInternalListener implements Listener {
    private @Inject Box box;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onXpGain(SkillsXPGainEvent event) {
        Player player = event.getPlayer();

        UUID uuid = player.getUniqueId();

        Optional<UserData> data = box.service().getData(uuid);

        Bukkit.getScheduler().runTask(box.plugin(), () -> {

            List<IPlaceholder> placeholders = data.map(d -> SkillsPlaceholderUtil.getAllPlaceholders(d, event.getSkill()))
                    .map(PlaceholderBuilder::get)
                    .orElse(Collections.emptyList());

            SoundUtils.playSound(player, box.files().config().gainXPSettings.sound);

            ConfigActionBar actionBar = box.files().config().gainXPSettings.actionBar;

            String message = StringUtils.processMulti(actionBar.getMessage(), placeholders);

            if (actionBar.isEnabled() && event.isShowXpActionBar())
                box.actionBarService().sendActionBar(event.getPlayer(), message);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onXpGain(SkillsLevelUPEvent event) {
        Player player = event.getPlayer();

        UUID uuid = player.getUniqueId();

        Optional<UserData> data = box.service().getData(uuid);

        Skill skill = event.getSkill();

        Bukkit.getScheduler().runTask(box.plugin(), () -> {
            event.getSkill()
                    .getCommandRewards(event.getNewLevel())
                    .forEach(reward -> reward.execute(player));

            event.getSkill()
                    .getStatRewards(event.getNewLevel())
                    .forEach(reward -> reward.execute(player));

            PlaceholderBuilder builder = data
                    .map(d -> SkillsPlaceholderUtil.getAllPlaceholders(d, skill))
                    .orElse(PlaceholderBuilder.create());

            List<IPlaceholder> placeholders = builder
                    .with(new Placeholder("skill_info_message", StringUtils.processMulti(skill.getCachedMessage(event.getNewLevel() + 1), builder.get())))
                    .get();

            Config.LevelUpSettings settings = box.files().config().levelUpSettings;

            SoundUtils.playSound(player, settings.sound);

            ConfigActionBar actionBar = settings.actionBar;

            String message = StringUtils.processMulti(actionBar.getMessage(), placeholders);

            if (settings.message.isEnabled())
                StringUtils.processMulti(settings.message.getMessages(), placeholders).forEach(msg -> player.sendMessage(StringUtils.color(msg)));

            if (actionBar.isEnabled())
                box.actionBarService().sendActionBar(event.getPlayer(), message);


        });
    }
}
