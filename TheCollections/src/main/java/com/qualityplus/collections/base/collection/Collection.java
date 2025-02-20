package com.qualityplus.collections.base.collection;

import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.collections.base.collection.executor.CollectionExecutor;
import com.qualityplus.collections.base.collection.gui.GUIOptions;
import com.qualityplus.collections.base.collection.registry.CollectionsRegistry;
import com.qualityplus.collections.base.collection.rewards.CollectionsCommandRewards;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public final class Collection extends OkaeriConfig {
    private String id;
    private int maxLevel;
    private String category;
    private boolean enabled;
    private String displayName;
    private List<String> description;
    private GUIOptions guiOptions;
    private double initialAmount;
    private CollectionsCommandRewards commandRewards;
    private Map<Integer, Double> xpRequirements;
    private Map<Integer, List<String>> collectionsInfoGUI;
    private Map<Integer, List<String>> collectionsInfoMessage;
    private CollectionExecutor collectionExecutor;
    private Map<Integer, CommandReward> guiCommandsPerLevel;

    @Builder
    public Collection(String id, boolean enabled, String displayName, List<String> description, int maxLevel, CollectionsCommandRewards commandRewards, GUIOptions guiOptions,
                      Map<Integer, Double> xpRequirements, Map<Integer, List<String>> collectionsInfoGUI, Map<Integer, List<String>> collectionsInfoMessage,
                      String category, CollectionExecutor collectionExecutor, Map<Integer, CommandReward> guiCommandsPerLevel) {
        this.id = id;
        this.enabled = enabled;
        this.maxLevel = maxLevel;
        this.category = category;
        this.guiOptions = guiOptions;
        this.description = description;
        this.displayName = displayName;
        this.commandRewards = commandRewards;
        this.xpRequirements = xpRequirements;
        this.collectionExecutor = collectionExecutor;
        this.collectionsInfoGUI = collectionsInfoGUI;
        this.guiCommandsPerLevel = guiCommandsPerLevel;
        this.collectionsInfoMessage = collectionsInfoMessage;
    }

    public void register() {
        CollectionsRegistry.registerNewCollection(this);
    }

    public List<String> getCollectionsCacheMessage(int level, MessageType type) {
        List<String> lore = new ArrayList<>();

        Map<Integer, List<String>> toWorkWith = type.equals(MessageType.GUI) ? collectionsInfoGUI : collectionsInfoMessage;

        if (toWorkWith.containsKey(level)) {
            lore.addAll(toWorkWith.get(level));
        } else {
            int highestLevel = 1;

            for (Integer startLevel : toWorkWith.keySet()) {
                if (startLevel > level)
                    break;

                if (startLevel > highestLevel)
                    highestLevel = startLevel;
            }

            lore.addAll(toWorkWith.getOrDefault(highestLevel, new ArrayList<>()));
        }

        return lore;
    }


    public List<CommandReward> getRewards(int level) {
        Map<Integer, List<CommandReward>> toWorkWith =  commandRewards.getRewards();

        if (toWorkWith.containsKey(level)) {
            return toWorkWith.get(level);
        } else {
            int highestLevel = 1;

            for (Integer startLevel : toWorkWith.keySet()) {
                if (startLevel > level)
                    break;

                if (startLevel > highestLevel)
                    highestLevel = startLevel;
            }

            return toWorkWith.getOrDefault(highestLevel, new ArrayList<>());
        }
    }

    public double getLevelRequirement(int level) {
        return xpRequirements.getOrDefault(level, 1D);
    }

    public CommandReward getGuiCommand(int level) {
        return Optional.ofNullable(guiCommandsPerLevel)
                .map(map -> map.getOrDefault(level, null))
                .orElse(null);
    }

    public enum MessageType{
        LEVEL_UP,
        GUI
    }

}
