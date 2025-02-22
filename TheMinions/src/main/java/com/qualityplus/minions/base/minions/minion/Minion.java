package com.qualityplus.minions.base.minions.minion;

import com.qualityplus.assistant.util.time.HumanTime;
import com.qualityplus.minions.base.config.Skins;
import com.qualityplus.minions.base.minions.minion.egg.MinionEgg;
import com.qualityplus.minions.base.minions.entity.MinionEntityOptions;
import com.qualityplus.minions.base.minions.minion.layout.LayoutGUISettings;
import com.qualityplus.minions.base.minions.minion.layout.LayoutType;
import com.qualityplus.minions.base.minions.minion.layout.MinionLayout;
import com.qualityplus.minions.base.minions.minion.level.MatRequirement;
import com.qualityplus.minions.base.minions.minion.level.MinionLevel;
import com.qualityplus.minions.base.minions.minion.recipes.MinionRecipeConfig;
import com.qualityplus.minions.base.minions.minion.skin.MinionSkin;
import com.qualityplus.minions.base.minions.minion.update.MinionSettings;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class Minion extends OkaeriConfig {
    private String id;
    private MinionType type;
    private String displayName;
    private MinionEgg minionEgg;
    private List<String> description;
    private MinionLayout minionLayout;
    private Map<Integer, MinionLevel> minionLevels;
    private MinionEntityOptions minionEntityOptions;
    private MinionSettings minionUpdateSettings;
    private LayoutGUISettings layoutGUISettings;
    private OkaeriConfig minionConfig;

    public boolean isLayoutType(LayoutType layoutType) {
        return minionLayout.getType().equals(layoutType);
    }

    public int getMaxLevel() {
        return minionLevels.size();
    }

    public MinionRecipeConfig getRecipe(int level) {
        return getMinionLevel(level)
                .map(MinionLevel::getMinionRecipe)
                .orElse(null);
    }

    public Optional<MinionSkin> getSkin(int level) {
        return getMinionLevel(level)
                .map(MinionLevel::getMinionSkin)
                .map(Skins::getSkin)
                .orElse(Optional.empty());
    }

    public HumanTime getTimer(int level) {
        return getMinionLevel(level)
                .map(MinionLevel::getExecuteActionsTime)
                .orElse(null);
    }

    public int getTimerSeconds(int level) {
        return Optional.ofNullable(getTimer(level))
                .filter(Objects::nonNull)
                .map(timer -> (int) timer.getSeconds())
                .orElse(-1);
    }

    public int getMaxStorage(int level) {
        return getMinionLevel(level)
                .map(MinionLevel::getMaxStorage)
                .orElse(-1);
    }

    public int getMaxStorageForInv(int level) {
        return getMaxStorage(level) * 64;
    }

    public MatRequirement getRequirement(int level) {
        return getMinionLevel(level)
                .map(MinionLevel::getMatRequirement)
                .orElse(null);
    }

    public Optional<MinionLevel> getMinionLevel(int level) {
        Map<Integer, MinionLevel> toWorkWith = minionLevels;

        if (toWorkWith.containsKey(level)) {
            return Optional.of(toWorkWith.get(level));
        } else {
            int highestLevel = 1;

            for (Integer startLevel : toWorkWith.keySet()) {
                if (startLevel > level)
                    break;

                if (startLevel > highestLevel)
                    highestLevel = startLevel;
            }

            return Optional.ofNullable(toWorkWith.getOrDefault(highestLevel, null));
        }
    }
}
