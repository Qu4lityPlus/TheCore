package com.qualityplus.runes.api.recipes;

import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.base.rune.Rune;

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

@Component
public final class Runes {
    private static final Map<String, Rune> RUNE_REGISTRY = new HashMap<>();
    private @Inject Box box;

    @ApiStatus.Internal
    public static void registerNewRune(@NotNull final Rune rune) {
        RUNE_REGISTRY.put(rune.getId().toLowerCase(), rune);
    }

    @Nullable
    public static Rune getByID(@NotNull final String id) {
        return RUNE_REGISTRY.get(id.toLowerCase());
    }

    @Nullable
    public static Rune getByKey(@NotNull final NamespacedKey key) {
        return RUNE_REGISTRY.get(key.getKey());
    }

    @Nullable
    public static Rune removeByID(@NotNull final String id) {
        return RUNE_REGISTRY.remove(id.toLowerCase());
    }

    @Nullable
    public static Rune removeByKey(@NotNull final NamespacedKey key) {
        return RUNE_REGISTRY.remove(key.getKey());
    }

    public static Set<Rune> values() {
        return ImmutableSet.copyOf(RUNE_REGISTRY.values());
    }

    public static Set<Rune> values(Predicate<Rune> filter) {
        return ImmutableSet.copyOf(RUNE_REGISTRY.values()).stream().filter(filter).collect(Collectors.toSet());
    }

    @Delayed(time = 1)
    public static void reloadRunes(@Inject Box box) {
        box.files().trades().runes.forEach(Rune::register);
    }
}
