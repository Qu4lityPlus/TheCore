package com.qualityplus.dragon.base.service;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.TheDragonAPI;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.api.service.BossBarService;
import com.qualityplus.dragon.base.configs.Messages;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;

import java.util.*;

/**
 * Implementation of {@link BossBarService}
 */
@Component
public final class BossBarServiceImpl implements BossBarService {
    private static final int DRAGON_RADIUS_AROUND = 100;
    private @Inject DragonGame dragonGame;
    private @Inject Messages messages;
    private Integer task;

    @Override
    public void startBossBar() {
        this.task = Bukkit.getScheduler().scheduleAsyncRepeatingTask(TheDragon.getApi().getPlugin(), this::handleBossBar, 20, 20);
    }

    @Override
    public void stopBossBar() {

        Optional.ofNullable(this.task).ifPresent(Bukkit.getScheduler()::cancelTask);

        TheAssistantPlugin.getAPI().getNms().sendBossBar(null, null);
    }

    private void handleBossBar() {
        if (!this.dragonGame.isActive()) {
            return;
        }

        sendBossBar();
    }

    private void sendBossBar() {
        TheDragonAPI api = TheDragon.getApi();

        TheDragonEntity theDragonEntity = api.getDragonService().getActiveDragon();

        int remaining = Optional.ofNullable(api.getGameService().getSwitchableEvent())
                .map(switchableEvent -> switchableEvent.getCurrentEvent().getRemainingTime())
                .orElse(0);

        List<IPlaceholder> placeholders = Arrays.asList(
                new Placeholder("thedragon_dragon_displayname", theDragonEntity.getDisplayName()),
                new Placeholder("thedragon_dragon_max_health", theDragonEntity.getMaxHealth()),
                new Placeholder("thedragon_dragon_health", theDragonEntity.getHealth()),
                new Placeholder("thedragon_next_event_remaining_time", remaining));

        final String message = StringUtils.processMulti(messages.gameMessages.gameEventBossBar, placeholders);

        dragonGame.getPlayers()
                .forEach(player -> TheAssistantPlugin.getAPI().getNms().sendBossBar(player, message));
    }
}

