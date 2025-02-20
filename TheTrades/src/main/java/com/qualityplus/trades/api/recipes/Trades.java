package com.qualityplus.trades.api.recipes;

import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.trades.api.box.Box;
import com.qualityplus.trades.base.trades.PluginTrade;
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
public final class Trades {
    private static final Map<String, PluginTrade> TRADES_REGISTRY = new HashMap<>();
    private @Inject Box box;

    @ApiStatus.Internal
    public static void registerNewTrade(@NotNull final PluginTrade trade) {
        TRADES_REGISTRY.put(trade.getId().toLowerCase(), trade);
    }

    @Nullable
    public static PluginTrade getByID(@NotNull final String id) {
        return TRADES_REGISTRY.get(id.toLowerCase());
    }

    @Nullable
    public static PluginTrade getByKey(@NotNull final NamespacedKey key) {
        return TRADES_REGISTRY.get(key.getKey());
    }

    @Nullable
    public static PluginTrade removeByID(@NotNull final String id) {
        return TRADES_REGISTRY.remove(id.toLowerCase());
    }

    @Nullable
    public static PluginTrade removeByKey(@NotNull final NamespacedKey key) {
        return TRADES_REGISTRY.remove(key.getKey());
    }

    public static Set<PluginTrade> values() {
        return ImmutableSet.copyOf(TRADES_REGISTRY.values());
    }

    public static Set<PluginTrade> values(Predicate<PluginTrade> filter) {
        return ImmutableSet.copyOf(TRADES_REGISTRY.values()).stream().filter(filter).collect(Collectors.toSet());
    }

    @Delayed(time = 1)
    public static void reloadTrades(@Inject Box box) {
        box.files().trades().pluginTrades.forEach(PluginTrade::register);
    }
}
