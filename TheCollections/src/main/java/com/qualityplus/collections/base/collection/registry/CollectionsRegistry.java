package com.qualityplus.collections.base.collection.registry;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.collections.api.box.Box;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.collection.category.CollectionCategory;
import com.qualityplus.collections.base.collection.executor.ExecutorType;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public final class CollectionsRegistry {
    private static final Map<String, Collection> COLLECTIONS_REGISTRY = new HashMap<>();
    private static final Map<ItemStack, String> ITEM_COLLECTIONS_REGISTRY = new HashMap<>();

    @ApiStatus.Internal
    public static void registerNewCollection(@NotNull final Collection collection) {
        COLLECTIONS_REGISTRY.put(collection.getId().toLowerCase(), collection);

        if (collection.getCollectionExecutor().getExecutorType().equals(ExecutorType.BY_PICK_UP_MATERIAL))
            ITEM_COLLECTIONS_REGISTRY.put(collection.getCollectionExecutor().getMaterial().parseItem(), collection.getId());
        else
            ITEM_COLLECTIONS_REGISTRY.put(collection.getCollectionExecutor().getItem(), collection.getId());
    }

    @Nullable
    public static Collection getByID(@NotNull final String id) {
        return COLLECTIONS_REGISTRY.get(id.toLowerCase());
    }

    @Nullable
    public static Collection getByKey(@NotNull final NamespacedKey key) {
        return COLLECTIONS_REGISTRY.get(key.getKey());
    }

    public static Set<Collection> getByCategory(CollectionCategory category) {
        return getByCategory(category.getId());
    }

    public static Set<Collection> getByCategory(String id) {
        return COLLECTIONS_REGISTRY.values().stream()
                .filter(collection -> collection.getCategory().equals(id))
                .collect(Collectors.toSet());
    }

    public static Set<Collection> values() {
        return ImmutableSet.copyOf(COLLECTIONS_REGISTRY.values());
    }

    public static Set<Collection> values(Predicate<Collection> filter) {
        return ImmutableSet.copyOf(COLLECTIONS_REGISTRY.values().stream().filter(filter).collect(Collectors.toList()));
    }

    public static Optional<Collection> getByMaterial(XMaterial toMatch) {
        ItemStack key = ITEM_COLLECTIONS_REGISTRY.keySet().stream().filter(item -> item.isSimilar(toMatch.parseItem())).findFirst().orElse(null);

        Collection collection = key == null ? null : getByID(ITEM_COLLECTIONS_REGISTRY.get(key));

        return Optional.ofNullable(collection);
    }

    public static Optional<Collection> getByItem(ItemStack toMatch) {
        ItemStack key = ITEM_COLLECTIONS_REGISTRY.keySet().stream().filter(item -> item.isSimilar(toMatch)).findFirst().orElse(null);

        Collection collection = key == null ? null : getByID(ITEM_COLLECTIONS_REGISTRY.get(key));

        return Optional.ofNullable(collection);
    }


    @Delayed(time = MinecraftTimeEquivalent.SECOND, async = true)
    public static void reloadCollections(@Inject Box box) {
        COLLECTIONS_REGISTRY.clear();
        ITEM_COLLECTIONS_REGISTRY.clear();




        box.files().collections().collections.forEach(Collection::register);

        //box.files().collections().collections.forEach(Collection::register);
    }
}
