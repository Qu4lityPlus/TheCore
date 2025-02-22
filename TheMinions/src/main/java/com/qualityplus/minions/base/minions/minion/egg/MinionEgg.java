package com.qualityplus.minions.base.minions.minion.egg;

import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class MinionEgg extends OkaeriConfig {
    private String displayName;
    private String eggDisplayName;

    public String getEggDisplayName() {
        return StringUtils.processMulti(eggDisplayName, new Placeholder("minion_egg_displayname", displayName).alone());
    }
}
