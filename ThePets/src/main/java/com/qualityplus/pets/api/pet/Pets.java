package com.qualityplus.pets.api.pet;

import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.configs.ConfigManager;
import com.qualityplus.assistant.lib.eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import com.qualityplus.assistant.lib.eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.pets.base.config.pet.PetConfig;
import com.qualityplus.pets.base.config.pet.Tiger;
import com.qualityplus.pets.base.pet.Pet;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Handles Pets from config files Cache
 */
@Component
public final class Pets {
    private static final Map<String, Pet> PETS_REGISTRY = new HashMap<>();
    private static @Inject Logger LOGGER;

    @ApiStatus.Internal
    public static void registerNewPet(@NotNull final Pet pet) {
        PETS_REGISTRY.put(pet.getId().toLowerCase(), pet);
    }

    @Nullable
    public static Pet getByID(@NotNull final String id) {
        return PETS_REGISTRY.get(id.toLowerCase());
    }

    @Nullable
    public static Pet getByKey(@NotNull final NamespacedKey key) {
        return PETS_REGISTRY.get(key.getKey());
    }

    public static Set<Pet> values() {
        return ImmutableSet.copyOf(PETS_REGISTRY.values());
    }

    public static Set<Pet> values(Predicate<Pet> filter) {
        return ImmutableSet.copyOf(PETS_REGISTRY.values().stream().filter(filter).collect(Collectors.toList()));
    }

    @Delayed(time = 1)
    public static void reloadPets(@Inject Tiger tiger, @Inject Plugin plugin) {
        registerNewPet(tiger.getPet());

        loadPetsConfig(plugin).forEach(Pets::registerNewPet);

        LOGGER.info("Plugin has registered in total " + values().size() + " Pets!");
    }

    private static List<Pet> loadPetsConfig(Plugin plugin) {

        File folder = new File(plugin.getDataFolder() + "/custom_pets");

        if (!folder.exists())
            Optional.of(folder).ifPresent(File::mkdirs);

        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .collect(Collectors.toList())
                .stream()
                .filter(Objects::nonNull)
                .map(Pets::loadPet)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Optional<Pet> loadPet(File file) {
        try {
            PetConfig petConfig = ConfigManager.create(PetConfig.class, (it) -> {
                it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer()));
                it.withBindFile(file);
                it.load(true);
            });

            LOGGER.info(String.format("Successfully loaded custom pet with id %s!", petConfig.getId()));

            return Optional.ofNullable(petConfig.getPet());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, String.format("Error occurred loading config file %s", file.getName()), e);
            return Optional.empty();
        }
    }
}
