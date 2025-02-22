package com.qualityplus.runes.util;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.runes.api.enchantment.legacy.RuneEnchantLegacy;
import com.qualityplus.runes.api.enchantment.newest.RuneEnchantNewest;
import com.qualityplus.runes.api.enchantment.newest.RuneEnchantNewest_1_20;
import lombok.experimental.UtilityClass;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;

@UtilityClass
public class GlowFinderUtil {

    private final static Enchantment GLOW_ENCHANTMENT;

    static {
        if (XMaterial.getVersion() >= 20) {
            GLOW_ENCHANTMENT = new RuneEnchantNewest_1_20();
        } else if (XMaterial.getVersion() >= 13) {
            GLOW_ENCHANTMENT = new RuneEnchantNewest();
        } else {
            GLOW_ENCHANTMENT = new RuneEnchantLegacy();
        }

        if (XMaterial.getVersion() < 13) {
            try {
                Field field = Enchantment.class.getDeclaredField("acceptingNew");
                field.setAccessible(true);
                field.set(null, true);
                Enchantment.registerEnchantment(GLOW_ENCHANTMENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Enchantment getEnchantment() {
        return GLOW_ENCHANTMENT;
    }
}
