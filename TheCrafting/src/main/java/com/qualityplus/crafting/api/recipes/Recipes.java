package com.qualityplus.crafting.api.recipes;

import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.base.recipes.CustomRecipe;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public final class Recipes {
    private static final Map<String, CustomRecipe> RECIPE_REGISTRY = new HashMap<>();
    private @Inject Box box;

    @ApiStatus.Internal
    public static void registerNewRecipe(@NotNull final CustomRecipe skill) {
        RECIPE_REGISTRY.put(skill.getId().toLowerCase(), skill);
    }

    @Nullable
    public static CustomRecipe getByID(@NotNull final String id) {
        return RECIPE_REGISTRY.get(id.toLowerCase());
    }

    @Nullable
    public static CustomRecipe getByKey(@NotNull final NamespacedKey key) {
        return RECIPE_REGISTRY.get(key.getKey());
    }

    @Nullable
    public static CustomRecipe removeByID(@NotNull final String id) {
        return RECIPE_REGISTRY.remove(id.toLowerCase());
    }

    @Nullable
    public static CustomRecipe removeByKey(@NotNull final NamespacedKey key) {
        return RECIPE_REGISTRY.remove(key.getKey());
    }

    public static Set<CustomRecipe> values() {
        return ImmutableSet.copyOf(RECIPE_REGISTRY.values());
    }

    @Delayed(time = 1)
    public static void reloadRecipes(@Inject Box box) {
        box.files().recipes().craftingRecipes.forEach(CustomRecipe::register);
    }
}
