package com.qualityplus.collections.util;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.actionbar.ActionBarUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.collections.api.box.Box;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.collection.category.CollectionCategory;
import com.qualityplus.collections.base.collection.registry.CollectionsRegistry;
import com.qualityplus.collections.persistance.data.UserData;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class CollectionsPlaceholderUtil {
    public List<IPlaceholder> getCollectionsPlaceholders(UserData userData, Collection skill) {
        int level = userData.getCollections().getLevel(skill.getId());

        return getCollectionsPlaceholders(userData, skill, level).get();
    }

    public PlaceholderBuilder getCollectionsPlaceholdersBuilder(UserData userData, Collection skill) {
        int level = userData.getCollections().getLevel(skill.getId());

        return getCollectionsPlaceholders(userData, skill, level);
    }

    public PlaceholderBuilder getCollectionsPlaceholders(UserData userData, Collection skill, int level) {
        double xp = userData.getCollections().getXp(skill.getId());
        double maxXp = skill.getLevelRequirement(level + 1);
        double percentage = ActionBarUtils.getPercentageFromTotal(xp, maxXp);

        return PlaceholderBuilder.create(
                new Placeholder("collection_displayname", skill.getDisplayName()),
                new Placeholder("collection_description", Optional.ofNullable(skill.getDescription()).orElse(Collections.emptyList())),

                new Placeholder("collection_level_roman", NumberUtil.toRoman(level)),
                new Placeholder("collection_level_number", level),
                new Placeholder("collection_next_level_roman", NumberUtil.toRoman(level + 1)),
                new Placeholder("collection_next_level_number", level + 1),

                new Placeholder("collection_level_progress", percentage > 100 ? 100 : percentage),
                new Placeholder("collection_action_bar", ActionBarUtils.getReplacedBar(percentage)),
                new Placeholder("collection_xp", xp),
                new Placeholder("collection_max_xp", maxXp)
        );
    }

    public PlaceholderBuilder getCategoryPlaceholders(UserData userData, CollectionCategory category) {
        List<Collection> collections = new ArrayList<>(CollectionsRegistry.getByCategory(category));

        double xp = collections.stream()
                .filter(collection -> userData.getCollections().getXp(collection.getId()) > 0)
                .count();
        double maxXp = collections.size();
        double percentage = ActionBarUtils.getPercentageFromTotal(xp, maxXp);

        return PlaceholderBuilder.create(
                new Placeholder("collection_category_displayname", category.getDisplayName()),
                new Placeholder("collection_category_level_progress", percentage),
                new Placeholder("collection_category_action_bar", ActionBarUtils.getReplacedBar(percentage)),
                new Placeholder("collection_category_xp", xp),
                new Placeholder("collection_category_max_xp", maxXp)
        );
    }

    public Placeholder getCategoryStatePlaceholder(UserData userData, CollectionCategory category, Box box) {
        List<Collection> collections = new ArrayList<>(CollectionsRegistry.getByCategory(category));

        double xp = collections.stream()
                .filter(collection -> userData.getCollections().getXp(collection.getId()) > 0)
                .count();
        double maxXp = collections.size();

        String value = xp >= maxXp ? box.files().messages().collectionsPlaceholders.isMaxedOutPlaceholder : box.files().messages().collectionsPlaceholders.unlockedPlaceholder;

        return new Placeholder("collection_category_is_maxed_out", value);
    }


    public List<IPlaceholder> getAllCategoriesPlaceholders(UserData userData) {

        int xp = (int) userData.getCollections().getXp().values().stream().filter(v -> v > 0)
                .count();
        double maxXp = userData.getCollections().getXp().size();

        double percentage = ActionBarUtils.getPercentageFromTotal((double)xp, maxXp);

        return PlaceholderBuilder.create(
                new Placeholder("collection_all_categories_progress", percentage),
                new Placeholder("collection_all_categories_action_bar", ActionBarUtils.getReplacedBar(percentage)),
                new Placeholder("collection_all_categories_xp", xp),
                new Placeholder("collection_all_categories_max_xp", maxXp)
        ).get();
    }

}
