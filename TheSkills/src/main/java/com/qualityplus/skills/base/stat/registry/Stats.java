package com.qualityplus.skills.base.stat.registry;

import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.api.effect.CommonObject;
import com.qualityplus.skills.base.stat.Stat;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public final class Stats {
    private static final Map<String, Stat> STAT_REGISTRY = new HashMap<>();

    @ApiStatus.Internal
    public static void registerNewStat(@NotNull final Stat stat) {
        STAT_REGISTRY.put(stat.getId().toLowerCase(), stat);
    }

    @Nullable
    public static Stat getByID(@NotNull final String id) {
        return STAT_REGISTRY.get(id.toLowerCase());
    }

    @Nullable
    public static Stat getByKey(@NotNull final NamespacedKey key) {
        return STAT_REGISTRY.get(key.getKey());
    }

    public static Set<Stat> values() {
        return ImmutableSet.copyOf(STAT_REGISTRY.values());
    }

    public static Set<Stat> values(Predicate<Stat> filter) {
        return ImmutableSet.copyOf(STAT_REGISTRY.values().stream().filter(filter).collect(Collectors.toList()));
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND, async = true)
    public static void reloadStats(@Inject Box box) {
        values().forEach(Stat::unregisterListeners);

        Stream.of(box.statFiles().defense().getStat(),
                        box.statFiles().intelligence().getStat(),
                        box.statFiles().critChance().getStat(),
                        box.statFiles().critDamage().getStat(),
                        box.statFiles().magicFind().getStat(),
                        box.statFiles().ferocity().getStat(),
                        box.statFiles().petLuck().getStat(),
                        box.statFiles().speed().getStat(),
                        box.statFiles().strength().getStat())
                .filter(CommonObject::isEnabled)
                .forEach(stat -> {
                    stat.register();
                    stat.registerListeners(box);
                });

        box.log().info("Registered " + values().size() + " Stats!");

    }
}
