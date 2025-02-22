package com.qualityplus.dragon.base.configs;

import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.dragon.api.event.DragonGameEvent;
import com.qualityplus.dragon.base.events.DragonFireBallEvent;
import com.qualityplus.dragon.base.events.DragonGuardianEvent;
import com.qualityplus.dragon.base.events.DragonLightningEvent;
import com.qualityplus.dragon.base.events.DragonNormalFireBallEvent;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration(path = "events.yml")
@Header("================================")
@Header("       Dragon Events      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class DragonEventsFile extends OkaeriConfig {
    public Map<String, SelfDragonEvents> events = FastMap.builder(String.class, SelfDragonEvents.class)
            .put("ancient_dragon", new SelfDragonEvents(FastMap.builder(Integer.class, SerializableEvent.class)
                    .put(1, SerializableEvent.builder()
                            .generalSettings(new EventConfig(EventType.DRAGON_FIREBALLS, 10, false, 0, 5))
                            .dragonFireballSettings(new DragonFireballsConfig())
                            .build())
                    .put(2, SerializableEvent.builder()
                            .generalSettings(new EventConfig(EventType.FIREBALLS, 10, false, 0, 5))
                            .fireBallsSettings(new FireBallsConfig())
                            .build())
                    .put(3, SerializableEvent.builder()
                            .generalSettings(new EventConfig(EventType.LIGHTNING, 10, false, 0, 5))
                            .lightningSettings(new LightningConfig())
                            .build())
                    .put(4, SerializableEvent.builder()
                            .generalSettings(new EventConfig(EventType.GUARDIANS, 10, false, 0, 5))
                            .guardianSettings(new GuardianConfig(Collections.singletonList(new GuardianChanceConfig("epic_zombie", 100, 0, false)), 10))
                            .build())
                    .build()))
            .build();

    @AllArgsConstructor
    public static class SelfDragonEvents extends OkaeriConfig {
        private @Getter Map<Integer, SerializableEvent> dragonEventMap;

        public Map<Integer, DragonGameEvent> getEvents() {
            Map<Integer, DragonGameEvent> eventMap = new HashMap<>();

            for (Integer value : dragonEventMap.keySet()) {
                SerializableEvent event = dragonEventMap.get(value);

                switch (event.generalSettings.eventType) {
                    case FIREBALLS:
                        eventMap.put(value, new DragonNormalFireBallEvent(event));
                        continue;
                    case GUARDIANS:
                        eventMap.put(value, new DragonGuardianEvent(event));
                        continue;
                    case LIGHTNING:
                        eventMap.put(value, new DragonLightningEvent(event));
                        continue;
                    case DRAGON_FIREBALLS:
                        eventMap.put(value, new DragonFireBallEvent(event));
                }
            }

            return eventMap;
        }
    }

    @AllArgsConstructor
    @Builder
    public static class SerializableEvent extends OkaeriConfig{
        public EventConfig generalSettings;
        public DragonFireballsConfig dragonFireballSettings = new DragonFireballsConfig();
        public FireBallsConfig fireBallsSettings = new FireBallsConfig();
        public LightningConfig lightningSettings = new LightningConfig();
        public GuardianConfig guardianSettings = new GuardianConfig();
    }

    @AllArgsConstructor
    public static class EventConfig extends OkaeriConfig{
        public EventType eventType;
        public int secondsDuration = 10;
        public boolean keepDragonAFK = true;
        public int dragonSpeedAmplifier = 0;
        public int repeatEventAfterSeconds = 5;
    }

    public static class DragonFireballsConfig extends OkaeriConfig{
        public int dragonFireballsPerSecond = 3;
    }

    public static class FireBallsConfig extends OkaeriConfig{
        public int fireballsPerSecond = 3;
        public double fireballDamage = 100;
        public boolean showParticle = true;
    }

    public static class LightningConfig extends OkaeriConfig{
        public double lightningDamage = 100;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class GuardianConfig extends OkaeriConfig{
        public List<GuardianChanceConfig> guardians;
        public int guardiansAmount;
    }

    @AllArgsConstructor
    public static class GuardianChanceConfig extends OkaeriConfig{
        public String id;
        public int chance;
        public int mythicMobLevel;
        public boolean isFromMythicMobs;
    }

    public enum EventType{
        GUARDIANS,
        DRAGON_FIREBALLS,
        FIREBALLS,
        LIGHTNING,
    }
}
