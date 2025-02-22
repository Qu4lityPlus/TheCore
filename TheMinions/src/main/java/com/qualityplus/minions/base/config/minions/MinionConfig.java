package com.qualityplus.minions.base.config.minions;

import com.qualityplus.minions.base.minions.entity.MinionEntityOptions;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.MinionType;
import com.qualityplus.minions.base.minions.minion.egg.MinionEgg;
import com.qualityplus.minions.base.minions.minion.layout.LayoutGUISettings;
import com.qualityplus.minions.base.minions.minion.layout.MinionLayout;
import com.qualityplus.minions.base.minions.minion.level.MinionLevel;
import com.qualityplus.minions.base.minions.minion.update.MinionSettings;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.CustomKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class MinionConfig extends OkaeriConfig {
    public String id;
    @CustomKey("display-name")
    public String displayName;
    public MinionType type;
    @CustomKey("minion-entity-options")
    public MinionEntityOptions minionEntityOptions;
    @CustomKey("minion-layout")
    public MinionLayout minionLayout;
    @CustomKey("minion-egg")
    public MinionEgg minionEgg;
    @CustomKey("minion-levels")
    public Map<Integer, MinionLevel> minionLevels;
    public List<String> description;
    @CustomKey("layout-gu-isettings")
    public LayoutGUISettings layoutGUISettings;
    @CustomKey("minion-settings")
    public MinionSettings minionSettings;


    public Minion getMinion() {
        return Minion.builder()
                .id(id)
                .displayName(displayName)
                .type(type)
                .minionEntityOptions(minionEntityOptions)
                .minionLayout(minionLayout)
                .minionEgg(minionEgg)
                .minionLevels(minionLevels)
                .minionUpdateSettings(minionSettings)
                .description(description)
                .layoutGUISettings(layoutGUISettings)
                .minionConfig(this)
                .build();
    }

}
