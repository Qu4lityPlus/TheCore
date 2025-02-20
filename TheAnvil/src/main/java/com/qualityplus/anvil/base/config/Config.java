package com.qualityplus.anvil.base.config;

import com.qualityplus.anvil.base.requirement.VanillaEnchantRequirement;
import com.qualityplus.assistant.api.gui.LoreWrapper;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XEnchantment;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Comment;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import com.qualityplus.assistant.util.faster.FastMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration()
@Header("================================")
@Header("       Config      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Config extends OkaeriConfig {
    public String prefix = "[TheAnvil] ";
    @Comment("Mark as true if you want to open plugin's gui")
    @Comment("with the vanilla anvil")
    public boolean openAsVanillaAnvil = true;
    @Comment("- wrapLength = After how many characters in a lore you want")
    @Comment("               to separate it.")
    @Comment("- wrapStart = After line is separated what character do you")
    @Comment("              want to start the new line with.")
    public LoreWrapper loreWrapper = new LoreWrapper(40, "&7");
    public List<XMaterial> allowedItems = getMaterials();


    @Comment("Required levels when TheEnchanting is")
    @Comment("disabled.")
    public Map<XEnchantment, VanillaEnchantRequirement> requiredLevelsForVanilla = FastMap.builder(XEnchantment.class, VanillaEnchantRequirement.class)
           // .put(XEnchantment.SHARPNESS, VanillaEnchantRequirement.builder()
            .put(XEnchantment.DAMAGE_ALL, VanillaEnchantRequirement.builder()
                    .requiredLevelsToEnchant(FastMap.builder(Integer.class, Integer.class)
                            .put(2, 4)
                            .put(3, 6)
                            .put(4, 8)
                            .put(5, 10)
                            .build())
                    .build())
            .build();

    private List<XMaterial> getMaterials() {
        List<XMaterial> allowed = new ArrayList<>();
        for (XMaterial material : XMaterial.VALUES) {
            String parsed = material.toString().toLowerCase();
            if (parsed.contains("waxed")) continue;
            if (parsed.contains("axe") || parsed.contains("pickaxe") || parsed.contains("shovel") || parsed.contains("hoe") || parsed.contains("sword") || parsed.contains("leggings") ||
               parsed.contains("chestplate") || parsed.contains("boots") || parsed.contains("helmet"))
                allowed.add(material);
        }
        allowed.add(XMaterial.ENCHANTED_BOOK);
        return allowed;
    }
}
