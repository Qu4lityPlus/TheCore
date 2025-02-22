package com.qualityplus.collections.base.config;

import com.qualityplus.collections.base.collection.category.Categories;
import com.qualityplus.collections.base.collection.category.CollectionCategory;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Configuration(path = "categories.yml")
@Header("================================")
@Header("       Categories      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class CategoriesFile extends OkaeriConfig {
    public List<CollectionCategory> collectionCategories = Arrays.asList(
        Categories.FARMING.getCategory(),
        Categories.MINING.getCategory(),
        Categories.COMBAT.getCategory(),
        Categories.FORAGING.getCategory(),
        Categories.FISHING.getCategory()
    );

    public Optional<CollectionCategory> getById(String id) {
        return collectionCategories.stream().filter(collection -> collection.getId().equals(id)).findFirst();
    }

    public Optional<CollectionCategory> getBySlot(int slot) {
        return collectionCategories.stream().filter(category -> category.getGuiOptions().getSlot() == slot).findFirst();
    }
}
