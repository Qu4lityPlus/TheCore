package com.qualityplus.skills.base.service;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.config.ConfigActionBar;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.base.nms.AbstractNMS;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.skills.api.service.ActionBarService;
import com.qualityplus.skills.api.service.SkillsService;
import com.qualityplus.skills.base.config.Config;
import com.qualityplus.skills.persistance.data.UserData;
import com.qualityplus.skills.util.SkillsPlaceholderUtil;
import com.qualityplus.skills.util.SkillsPlayerUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Scheduled;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class ActionBarServiceImpl implements ActionBarService {
    @Override
    public void sendActionBar(Player player, String message) {
        TheAssistantPlugin.getAPI().getNms().sendActionBar(player, message);
    }

    @Override
    public void blacklist(UUID uuid) {
        ((AbstractNMS) TheAssistantPlugin.getAPI().getNms()).blacklist(uuid);
    }

    @Override
    public boolean isBlacklisted(UUID uuid) {
        return ((AbstractNMS) TheAssistantPlugin.getAPI().getNms()).isBlacklisted(uuid);
    }

    @Override
    public void whitelistTemp(UUID uuid) {
        ((AbstractNMS) TheAssistantPlugin.getAPI().getNms()).whitelistTemp(uuid);
    }

    @Override
    public boolean isWhitelisted(UUID uuid) {
        return ((AbstractNMS) TheAssistantPlugin.getAPI().getNms()).isWhitelisted(uuid);
    }

    @Scheduled(rate = 5, delay = 5)
    public void startScheduling(@Inject SkillsService service, @Inject Config config) {
        AbstractNMS nms = (AbstractNMS) TheAssistantPlugin.getAPI().getNms();

        for (Player player :  Bukkit.getOnlinePlayers()) {
            if (isBlacklisted(player.getUniqueId()))
                continue;

            if (!SkillsPlayerUtil.isInSurvival(player))
                continue;

            ConfigActionBar actionBar = config.informationActionBar;

            if (!actionBar.isEnabled()) return;

            Optional<UserData> data = service.getData(player.getUniqueId());

            List<IPlaceholder> placeholders = data.map(SkillsPlaceholderUtil::getStatPlaceholders)
                    .map(p -> p.with(SkillsPlaceholderUtil.getHealthPlaceholders(player)))
                    .map(PlaceholderBuilder::get)
                    .orElse(Collections.emptyList());

            String message = StringUtils.processMulti(actionBar.getMessage(), placeholders);

            whitelistTemp(player.getUniqueId());

            nms.sendActionBar(player, message);
        }
    }
}
