package com.qualityplus.skills.base.perk.perks.common;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@NoArgsConstructor
public abstract class AbstractFortunePerk extends Perk {
    @Getter @Setter
    private List<XMaterial> allowedMaterials;

    public AbstractFortunePerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double baseAmount, double chancePerLevel, List<XMaterial> allowedMaterials) {
        super(id, enabled, displayName, description, skillGUIOptions, baseAmount, chancePerLevel);

        this.allowedMaterials = allowedMaterials;
    }

    protected ItemStack getModified(ItemStack itemStack, int additional) {
        itemStack.setAmount(itemStack.getAmount() * additional);

        return itemStack;
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        return StringUtils.processMulti(super.getFormattedDescription(level), PlaceholderBuilder.create(
                new Placeholder("multiplier", getMultiplier(level)),
                new Placeholder("chance", getChance(level)))
                .get());
    }

    protected int getMultiplier(double level) {
        double chance = chancePerLevel * level;

        double add = 2;

        if (chance % 100 == 0.0)
            add = 1;

        return (int) ((chance / 100) + add);
    }

    protected double getChance(double level) {
        double chance = chancePerLevel * level;

        chance -= ((getMultiplier(level) - 2) * 100);
        if (chance == 0.0)
            chance = 100.0;

        return chance;
    }
}
