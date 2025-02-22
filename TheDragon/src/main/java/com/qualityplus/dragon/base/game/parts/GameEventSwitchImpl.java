package com.qualityplus.dragon.base.game.parts;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.event.DragonGameEvent;
import com.qualityplus.dragon.api.event.SwitchableEvents;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.api.game.part.GameEventSwitch;
import com.qualityplus.dragon.base.configs.Config;
import com.qualityplus.dragon.base.configs.DragonEventsFile;
import com.qualityplus.dragon.base.configs.Messages;
import com.qualityplus.dragon.base.events.*;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.Optional;

@Component
public final class GameEventSwitchImpl implements GameEventSwitch {
    private @Getter SwitchableEvents switchableEvent;
    private @Inject DragonEventsFile dragonEvents;
    private @Inject DragonGame dragonGame;
    private @Inject Config configuration;
    private @Inject Messages messages;
    private Integer eventTask;

    @Override
    public void switchEvents() {

        TheDragonEntity dragon = TheDragon.getApi().getDragonService().getActiveDragon();

        DragonEventsFile.SelfDragonEvents serializableEvent = dragonEvents.events.get(dragon.getId());

        switchableEvent = new SwitchableEventImpl(serializableEvent.getEvents());

        eventTask = Bukkit.getScheduler().scheduleAsyncRepeatingTask(TheDragon.getApi().getPlugin(), () -> {

            if (switchableEvent.getCurrentEvent() == null || !switchableEvent.getCurrentEvent().isActive()) {

                Optional.ofNullable(switchableEvent.getNext())
                        .ifPresent(nextEvent -> {
                            nextEvent.start(dragonGame);

                            String message = StringUtils.processMulti(parseMessage(nextEvent), Collections.singletonList(new Placeholder("prefix", configuration.prefix)));

                            TheDragon.getApi().getUserService().sendMessage(Collections.singletonList(message));
                        });
            }
        }, 0, 20);
    }

    public String parseMessage(DragonGameEvent dragonEvent) {
        if (!messages.gameConfigMessages.enabled) return "";

        if (dragonEvent instanceof DragonFireBallEvent)
            return messages.gameConfigMessages.dragonBall;
        else if (dragonEvent instanceof DragonNormalFireBallEvent)
            return messages.gameConfigMessages.fireball;
        else if (dragonEvent instanceof DragonGuardianEvent)
            return messages.gameConfigMessages.guardians;
        else if (dragonEvent instanceof DragonLightningEvent)
            return messages.gameConfigMessages.lightning;
        else
            return "";
    }

    @Override
    public void stopSwitchEvents() {
        Optional.ofNullable(eventTask).ifPresent(Bukkit.getScheduler()::cancelTask);
    }
}
