package com.qualityplus.runes.base.rune;

import com.qualityplus.runes.api.config.RuneTableConfig.RuneItem;
import com.qualityplus.runes.api.recipes.Runes;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public final class Rune extends OkaeriConfig {
    private final String id;
    private final String displayName;
    private final List<String> description;
    private final RuneItem runeItem;
    private final String toAddLore;
    private final RuneEffect effect;
    private final Map<Integer, RuneLevel> levelDataMap;

    @Builder
    public Rune(String id, String displayName, List<String> description, RuneItem runeItem, String toAddLore, RuneEffect effect, Map<Integer, RuneLevel> levelDataMap) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.runeItem = runeItem;
        this.toAddLore = toAddLore;
        this.effect = effect;
        this.levelDataMap = levelDataMap;
    }

    public @Nullable RuneLevel getRuneLevel(int level) {
        return levelDataMap == null || !levelDataMap.containsKey(level) ? null : levelDataMap.get(level);
    }

    public Optional<RuneLevel> getOptRuneLevel(int level) {
        return Optional.ofNullable(getRuneLevel(level));
    }


    public void register() {
        Runes.registerNewRune(this);
    }
}
