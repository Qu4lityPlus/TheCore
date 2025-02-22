package com.qualityplus.minions.base.minions;

import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.configs.ConfigManager;
import com.qualityplus.assistant.lib.eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import com.qualityplus.assistant.lib.eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.okaeri.serdes.SerdesAssistantBukkit;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.base.config.minions.*;
import com.qualityplus.minions.base.minions.minion.Minion;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public final class Minions {
    private static final Map<String, Minion> MINIONS_REGISTRY = new HashMap<>();
    private static @Inject Logger LOGGER;

    @ApiStatus.Internal
    public static void registerNewMinion(@NotNull final Minion minion) {
        MINIONS_REGISTRY.put(minion.getId().toLowerCase(), minion);
    }

    @Nullable
    public static Minion getByID(@NotNull final String id) {
        return MINIONS_REGISTRY.get(id.toLowerCase());
    }

    @Nullable
    public static Minion getByKey(@NotNull final NamespacedKey key) {
        return MINIONS_REGISTRY.get(key.getKey());
    }

    public static Set<Minion> values() {
        return ImmutableSet.copyOf(MINIONS_REGISTRY.values());
    }

    public static Set<Minion> values(Predicate<Minion> filter) {
        return ImmutableSet.copyOf(MINIONS_REGISTRY.values().stream().filter(filter).collect(Collectors.toList()));
    }

    @Delayed(time = 1)
    public static void reloadMinions(@Inject SnowMinion snowMinion, @Inject DiamondMinion diamondMinion, @Inject WheatMinion wheatMinion, @Inject CowMinion cowMinion) {
        registerNewMinion(diamondMinion.getMinion());
        registerNewMinion(wheatMinion.getMinion());
        registerNewMinion(snowMinion.getMinion());
        registerNewMinion(cowMinion.getMinion());

        loadMinionsConfig().forEach(Minions::registerNewMinion);
    }

    private static List<Minion> loadMinionsConfig() {

        File folder = new File(TheMinions.getInstance().getDataFolder() + "/custom_minions");

        if (!folder.exists())
            Optional.of(folder).ifPresent(File::mkdirs);

        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .collect(Collectors.toList())
                .stream()
                .filter(Objects::nonNull)
                .map(Minions::loadMinion)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Optional<Minion> loadMinion(File file) {
        try {
            MinionConfig minionConfig = ConfigManager.create(MinionConfig.class, (it) -> {
                it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer()));
                it.withSerdesPack(new SerdesAssistantBukkit());
                it.withBindFile(file);
                it.load(true);
            });

            LOGGER.info(String.format("Successfully loaded custom minion with id %s!", minionConfig.getId()));

            return Optional.ofNullable(minionConfig.getMinion());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, String.format("Error occurred loading config file %s", file.getName()), e);
            return Optional.empty();
        }
    }

}
