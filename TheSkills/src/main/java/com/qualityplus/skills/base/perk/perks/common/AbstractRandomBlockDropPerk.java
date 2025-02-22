package com.qualityplus.skills.base.perk.perks.common;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.random.EasyRandom;
import com.qualityplus.assistant.util.random.RandomSelector;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
public abstract class AbstractRandomBlockDropPerk extends Perk {
    @Getter @Setter
    private Map<XMaterial, Double> itemAndChances;

    public AbstractRandomBlockDropPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double baseAmount, double chancePerLevel, Map<XMaterial, Double> itemAndChances) {
        super(id, enabled, displayName, description, skillGUIOptions, baseAmount, chancePerLevel);

        this.itemAndChances = itemAndChances;
    }

    protected ItemStack getItem() {
        List<EasyRandom<XMaterial>> items = itemAndChances.keySet().stream()
                .map(item -> new EasyRandom<>(item, itemAndChances.get(item)))
                .collect(Collectors.toList());

        return Optional.ofNullable(new RandomSelector<>(items).getRandom()).map(item -> item.getItem().parseItem()).orElse(null);
    }
}