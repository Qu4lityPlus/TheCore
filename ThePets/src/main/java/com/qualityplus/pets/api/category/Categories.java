package com.qualityplus.pets.api.category;

import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.pets.api.pet.category.PetCategory;
import com.qualityplus.pets.base.config.CategoriesFile;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public final class Categories {
    private static final Map<String, PetCategory> PET_CATEGORY_MAP = new HashMap<>();

    @ApiStatus.Internal
    public static void registerNewCategory(@NotNull final PetCategory pet) {
        PET_CATEGORY_MAP.put(pet.getId().toLowerCase(), pet);
    }

    @NotNull
    public static Optional<PetCategory> getByID(@NotNull final String id) {
        return Optional.ofNullable(PET_CATEGORY_MAP.getOrDefault(id.toLowerCase(), null));
    }

    @NotNull
    public static Optional<PetCategory> getByKey(@NotNull final NamespacedKey key) {
        return Optional.ofNullable(PET_CATEGORY_MAP.getOrDefault(key.getKey(), null));
    }

    public static Set<PetCategory> values() {
        return ImmutableSet.copyOf(PET_CATEGORY_MAP.values());
    }

    public static Set<PetCategory> values(Predicate<PetCategory> filter) {
        return ImmutableSet.copyOf(PET_CATEGORY_MAP.values().stream().filter(filter).collect(Collectors.toList()));
    }

    @Delayed(time = 1)
    public static void reloadCategories(@Inject CategoriesFile categories, @Inject Logger logger) {
        PET_CATEGORY_MAP.clear();

        categories.petCategories.forEach(Categories::registerNewCategory);

        logger.info("Registered " + values().size() + " Pet Categories!");
    }
}
