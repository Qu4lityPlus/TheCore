package com.qualityplus.enchanting.base.config.enchantments.eco;

import com.google.common.collect.ImmutableMap;
import com.qualityplus.enchanting.base.config.enchantments.EnchantConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration(path = "enchantments/eco_enchantments.yml")
@Header("================================")
@Header("       Eco Enchantments      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class EcoEnchantments extends OkaeriConfig {
    public boolean loadAllEcoEnchants = true;
    public List<String> blockedEnchants = Collections.singletonList("blockedName");

    public Map<String, EnchantConfig> customEcoOptions = ImmutableMap.<String, EnchantConfig>builder()
            .put("exampleId",
                    new EnchantConfig("Example Display Name",
                            "Example description.",
                            new HashMap<Integer, String>() {{
                                for (int i = 0; i<1; i++) put(i, "permission.example.level." + i);
                            }},
                            new HashMap<Integer, Double>() {{
                            for (int i = 0; i<1; i++) put(i, 15d * i);
                            }},
                            new HashMap<Integer, Double>() {{
                                for (int i = 0; i<1; i++) put(i, 15d * i);
                            }}))
            .build();
}
