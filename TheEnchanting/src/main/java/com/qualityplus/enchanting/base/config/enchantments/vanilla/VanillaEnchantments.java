package com.qualityplus.enchanting.base.config.enchantments.vanilla;

import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XEnchantment;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.map.MapUtils;
import com.qualityplus.enchanting.api.enchantment.CoreEnchants;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.api.enchantment.ProviderType;
import com.qualityplus.enchanting.base.config.enchantments.EnchantConfig;
import com.qualityplus.enchanting.base.factory.VanillaEnchantmentFactory;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration(path = "enchantments/vanilla_enchantments.yml")
@Header("================================")
@Header("       Vanilla Enchantments      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class VanillaEnchantments extends OkaeriConfig {
    /*public Map<XEnchantment, EnchantConfig> vanillaEnchantments = ImmutableMap.<XEnchantment, EnchantConfig>builder()
            .put(XEnchantment.POWER, new EnchantConfig("Power", "Makes your arrows stronger.", easyTxtMap(XEnchantment.POWER), easyMap(XEnchantment.POWER), easyMap(XEnchantment.POWER)))
            .put(XEnchantment.FLAME, new EnchantConfig("Flame", "Ignite your arrows to burn your enemies.", easyTxtMap(XEnchantment.FLAME), easyMap(XEnchantment.FLAME), easyMap(XEnchantment.FLAME)))
            .put(XEnchantment.INFINITY, new EnchantConfig("Infinity", "Get an infinity Arrow supply.", easyTxtMap(XEnchantment.INFINITY), easyMap(XEnchantment.INFINITY), easyMap(XEnchantment.INFINITY)))
            .put(XEnchantment.KNOCKBACK, new EnchantConfig("Punch", "Make your arrows push your enemies backwards.", easyTxtMap(XEnchantment.KNOCKBACK), easyMap(XEnchantment.KNOCKBACK), easyMap(XEnchantment.KNOCKBACK)))
            .put(XEnchantment.BINDING_CURSE, new EnchantConfig("Power", "Prevent your armors to be removed.", easyTxtMap(XEnchantment.BINDING_CURSE), easyMap(XEnchantment.BINDING_CURSE), easyMap(XEnchantment.BINDING_CURSE)))
            .put(XEnchantment.CHANNELING, new EnchantConfig("Channelling", "Make your trident strike a magic lightnings.", easyTxtMap(XEnchantment.CHANNELING), easyMap(XEnchantment.CHANNELING), easyMap(XEnchantment.CHANNELING)))
            .put(XEnchantment.SHARPNESS, new EnchantConfig("Sharpness", "Increase damage against all mobs.", easyTxtMap(XEnchantment.SHARPNESS), easyMap(XEnchantment.SHARPNESS), easyMap(XEnchantment.SHARPNESS)))
            .put(XEnchantment.BANE_OF_ARTHROPODS, new EnchantConfig("Bane of Arthropods", "Increase damage against arthropods.", easyTxtMap(XEnchantment.BANE_OF_ARTHROPODS), easyMap(XEnchantment.BANE_OF_ARTHROPODS), easyMap(XEnchantment.BANE_OF_ARTHROPODS)))
            .put(XEnchantment.SMITE, new EnchantConfig("Smite", "Increase damage against undead mobs.", easyTxtMap(XEnchantment.SMITE), easyMap(XEnchantment.SMITE), easyMap(XEnchantment.SMITE)))
            .put(XEnchantment.DEPTH_STRIDER, new EnchantConfig("Depth", "Speeds up how fast you can move underwater.", easyTxtMap(XEnchantment.DEPTH_STRIDER), easyMap(XEnchantment.DEPTH_STRIDER), easyMap(XEnchantment.DEPTH_STRIDER)))
            .put(XEnchantment.EFFICIENCY, new EnchantConfig("Efficiency", "Speeds up how fast you can break ores.", easyTxtMap(XEnchantment.EFFICIENCY), easyMap(XEnchantment.EFFICIENCY), easyMap(XEnchantment.EFFICIENCY)))
            .put(XEnchantment.UNBREAKING, new EnchantConfig("Durability", "Increases durations of items.", easyTxtMap(XEnchantment.UNBREAKING), easyMap(XEnchantment.UNBREAKING), easyMap(XEnchantment.UNBREAKING)))
            .put(XEnchantment.FIRE_ASPECT, new EnchantConfig("Fire Aspect", "Increases durations of items.", easyTxtMap(XEnchantment.FIRE_ASPECT), easyMap(XEnchantment.FIRE_ASPECT), easyMap(XEnchantment.FIRE_ASPECT)))
            .put(XEnchantment.AQUA_AFFINITY, new EnchantConfig("Aqua Affinity", "Increases underwater mining speed.", easyTxtMap(XEnchantment.AQUA_AFFINITY), easyMap(XEnchantment.AQUA_AFFINITY), easyMap(XEnchantment.AQUA_AFFINITY)))
            .put(XEnchantment.VANISHING_CURSE, new EnchantConfig("Curse of vanishing", "Causes the item to disappear on death.", easyTxtMap(XEnchantment.VANISHING_CURSE), easyMap(XEnchantment.VANISHING_CURSE), easyMap(XEnchantment.VANISHING_CURSE)))
            .put(XEnchantment.THORNS, new EnchantConfig("Thorns", "Become your armor a cactus.", easyTxtMap(XEnchantment.THORNS), easyMap(XEnchantment.THORNS), easyMap(XEnchantment.THORNS)))
            .put(XEnchantment.SWEEPING_EDGE, new EnchantConfig("Sweeping", "Increases damage to mobs from a sweep attack.", easyTxtMap(XEnchantment.SWEEPING_EDGE), easyMap(XEnchantment.SWEEPING_EDGE), easyMap(XEnchantment.SWEEPING_EDGE)))
            .put(XEnchantment.SILK_TOUCH, new EnchantConfig("Silk Touch", "Get the ore instead of ingot when mine.", easyTxtMap(XEnchantment.SILK_TOUCH), easyMap(XEnchantment.SILK_TOUCH), easyMap(XEnchantment.SILK_TOUCH)))
            .put(XEnchantment.RIPTIDE, new EnchantConfig("Rip", "Your trident will propel you forward.", easyTxtMap(XEnchantment.RIPTIDE), easyMap(XEnchantment.RIPTIDE), easyMap(XEnchantment.RIPTIDE)))
            .put(XEnchantment.QUICK_CHARGE, new EnchantConfig("Quick Charge", "Reload Quickly your crossbow.", easyTxtMap(XEnchantment.QUICK_CHARGE), easyMap(XEnchantment.QUICK_CHARGE), easyMap(XEnchantment.QUICK_CHARGE)))
            .put(XEnchantment.PROJECTILE_PROTECTION, new EnchantConfig("Projectile Protection", "Reduces damage from projectiles.", easyTxtMap(XEnchantment.PROJECTILE_PROTECTION), easyMap(XEnchantment.PROJECTILE_PROTECTION), easyMap(XEnchantment.PROJECTILE_PROTECTION)))
            .put(XEnchantment.FIRE_PROTECTION, new EnchantConfig("Fire Protection", "Reduces damage from fire.", easyTxtMap(XEnchantment.FIRE_PROTECTION), easyMap(XEnchantment.FIRE_PROTECTION), easyMap(XEnchantment.FIRE_PROTECTION)))
            .put(XEnchantment.FEATHER_FALLING, new EnchantConfig("Feather Falling", "Reduces damage when fall.", easyTxtMap(XEnchantment.FEATHER_FALLING), easyMap(XEnchantment.FEATHER_FALLING), easyMap(XEnchantment.FEATHER_FALLING)))
            .put(XEnchantment.BLAST_PROTECTION, new EnchantConfig("Blast Protection", "Reduces damage from explosions.", easyTxtMap(XEnchantment.BLAST_PROTECTION), easyMap(XEnchantment.BLAST_PROTECTION), easyMap(XEnchantment.BLAST_PROTECTION)))
            .put(XEnchantment.PROTECTION, new EnchantConfig("Protection", "Reduces general damage.", easyTxtMap(XEnchantment.PROTECTION), easyMap(XEnchantment.PROTECTION), easyMap(XEnchantment.PROTECTION)))
            .put(XEnchantment.PIERCING, new EnchantConfig("Piercing", "Pierce enemies with crossbow.", easyTxtMap(XEnchantment.PIERCING), easyMap(XEnchantment.PIERCING), easyMap(XEnchantment.PIERCING)))
            .put(XEnchantment.RESPIRATION, new EnchantConfig("Respiration", "Increases breathe underwater.", easyTxtMap(XEnchantment.RESPIRATION), easyMap(XEnchantment.RESPIRATION), easyMap(XEnchantment.RESPIRATION)))
            .put(XEnchantment.MULTISHOT, new EnchantConfig("Triple Shoot", "Throws triple arrows from crossbow.", easyTxtMap(XEnchantment.MULTISHOT), easyMap(XEnchantment.MULTISHOT), easyMap(XEnchantment.MULTISHOT)))
            .put(XEnchantment.MENDING, new EnchantConfig("Mending", "Mend your equipped tools.", easyTxtMap(XEnchantment.MENDING), easyMap(XEnchantment.MENDING), easyMap(XEnchantment.MENDING)))
            .put(XEnchantment.LURE, new EnchantConfig("Lure", "Decreases wait time while fishing.", easyTxtMap(XEnchantment.LURE), easyMap(XEnchantment.LURE), easyMap(XEnchantment.LURE)))
            .put(XEnchantment.LUCK_OF_THE_SEA, new EnchantConfig("Luck Of Sea", "Increases luck while fishing.", easyTxtMap(XEnchantment.LUCK_OF_THE_SEA), easyMap(XEnchantment.LUCK_OF_THE_SEA), easyMap(XEnchantment.LUCK_OF_THE_SEA)))
            .put(XEnchantment.LOYALTY, new EnchantConfig("Loyal", "Return trident to your hand when thrown.", easyTxtMap(XEnchantment.LOYALTY), easyMap(XEnchantment.LOYALTY), easyMap(XEnchantment.LOYALTY)))
            .put(XEnchantment.LOOTING, new EnchantConfig("Looting", "Increases the chance of gain extra mob loot.", easyTxtMap(XEnchantment.LOOTING), easyMap(XEnchantment.LOOTING), easyMap(XEnchantment.LOOTING)))
            .put(XEnchantment.FORTUNE, new EnchantConfig("Fortune", "Increases the chance of gain extra block loot.", easyTxtMap(XEnchantment.FORTUNE), easyMap(XEnchantment.FORTUNE), easyMap(XEnchantment.FORTUNE)))
            .put(XEnchantment.KNOCKBACK, new EnchantConfig("KnockBack", "Make your weapons push your enemies backwards.", easyTxtMap(XEnchantment.KNOCKBACK), easyMap(XEnchantment.KNOCKBACK), easyMap(XEnchantment.KNOCKBACK)))
            .put(XEnchantment.SOUL_SPEED, new EnchantConfig("Soul Speed", "Make your boots faster.", easyTxtMap(XEnchantment.SOUL_SPEED), easyMap(XEnchantment.SOUL_SPEED), easyMap(XEnchantment.SOUL_SPEED)))
            .put(XEnchantment.IMPALING, new EnchantConfig("Impale", "Increase your trident damage.", easyTxtMap(XEnchantment.IMPALING), easyMap(XEnchantment.IMPALING), easyMap(XEnchantment.IMPALING)))
            .put(XEnchantment.FROST_WALKER, new EnchantConfig("Frost", "Create ice when walking over water.", easyTxtMap(XEnchantment.FROST_WALKER), easyMap(XEnchantment.FROST_WALKER), easyMap(XEnchantment.FROST_WALKER)))
            .build();*/
    public Map<XEnchantment, EnchantConfig> vanillaEnchantments = ImmutableMap.<XEnchantment, EnchantConfig>builder()
            .put(XEnchantment.ARROW_DAMAGE, new EnchantConfig("Power", "Makes your arrows stronger.", easyTxtMap(XEnchantment.ARROW_DAMAGE), easyMap(XEnchantment.ARROW_DAMAGE), easyMap(XEnchantment.ARROW_DAMAGE)))
            .put(XEnchantment.ARROW_FIRE, new EnchantConfig("Flame", "Ignite your arrows to burn your enemies.", easyTxtMap(XEnchantment.ARROW_FIRE), easyMap(XEnchantment.ARROW_FIRE), easyMap(XEnchantment.ARROW_FIRE)))
            .put(XEnchantment.ARROW_INFINITE, new EnchantConfig("Infinity", "Get an infinity Arrow supply.", easyTxtMap(XEnchantment.ARROW_INFINITE), easyMap(XEnchantment.ARROW_INFINITE), easyMap(XEnchantment.ARROW_INFINITE)))
            .put(XEnchantment.ARROW_KNOCKBACK, new EnchantConfig("Punch", "Make your arrows push your enemies backwards.", easyTxtMap(XEnchantment.ARROW_KNOCKBACK), easyMap(XEnchantment.ARROW_KNOCKBACK), easyMap(XEnchantment.ARROW_KNOCKBACK)))
            .put(XEnchantment.BINDING_CURSE, new EnchantConfig("Power", "Prevent your armors to be removed.", easyTxtMap(XEnchantment.BINDING_CURSE), easyMap(XEnchantment.BINDING_CURSE), easyMap(XEnchantment.BINDING_CURSE)))
            .put(XEnchantment.CHANNELING, new EnchantConfig("Channelling", "Make your trident strike a magic lightnings.", easyTxtMap(XEnchantment.CHANNELING), easyMap(XEnchantment.CHANNELING), easyMap(XEnchantment.CHANNELING)))
            .put(XEnchantment.DAMAGE_ALL, new EnchantConfig("Sharpness", "Increase damage against all mobs.", easyTxtMap(XEnchantment.DAMAGE_ALL), easyMap(XEnchantment.DAMAGE_ALL), easyMap(XEnchantment.DAMAGE_ALL)))
            .put(XEnchantment.DAMAGE_ARTHROPODS, new EnchantConfig("Bane of Arthropods", "Increase damage against arthropods.", easyTxtMap(XEnchantment.DAMAGE_ARTHROPODS), easyMap(XEnchantment.DAMAGE_ARTHROPODS), easyMap(XEnchantment.DAMAGE_ARTHROPODS)))
            .put(XEnchantment.DAMAGE_UNDEAD, new EnchantConfig("Smite", "Increase damage against undead mobs.", easyTxtMap(XEnchantment.DAMAGE_UNDEAD), easyMap(XEnchantment.DAMAGE_UNDEAD), easyMap(XEnchantment.DAMAGE_UNDEAD)))
            .put(XEnchantment.DEPTH_STRIDER, new EnchantConfig("Depth", "Speeds up how fast you can move underwater.", easyTxtMap(XEnchantment.DEPTH_STRIDER), easyMap(XEnchantment.DEPTH_STRIDER), easyMap(XEnchantment.DEPTH_STRIDER)))
            .put(XEnchantment.DIG_SPEED, new EnchantConfig("Efficiency", "Speeds up how fast you can break ores.", easyTxtMap(XEnchantment.DIG_SPEED), easyMap(XEnchantment.DIG_SPEED), easyMap(XEnchantment.DIG_SPEED)))
            .put(XEnchantment.DURABILITY, new EnchantConfig("Durability", "Increases durations of items.", easyTxtMap(XEnchantment.DURABILITY), easyMap(XEnchantment.DURABILITY), easyMap(XEnchantment.DURABILITY)))
            .put(XEnchantment.FIRE_ASPECT, new EnchantConfig("Fire Aspect", "Increases durations of items.", easyTxtMap(XEnchantment.FIRE_ASPECT), easyMap(XEnchantment.FIRE_ASPECT), easyMap(XEnchantment.FIRE_ASPECT)))
            .put(XEnchantment.WATER_WORKER, new EnchantConfig("Aqua Affinity", "Increases underwater mining speed.", easyTxtMap(XEnchantment.WATER_WORKER), easyMap(XEnchantment.WATER_WORKER), easyMap(XEnchantment.WATER_WORKER)))
            .put(XEnchantment.VANISHING_CURSE, new EnchantConfig("Curse of vanishing", "Causes the item to disappear on death.", easyTxtMap(XEnchantment.VANISHING_CURSE), easyMap(XEnchantment.VANISHING_CURSE), easyMap(XEnchantment.VANISHING_CURSE)))
            .put(XEnchantment.THORNS, new EnchantConfig("Thorns", "Become your armor a cactus.", easyTxtMap(XEnchantment.THORNS), easyMap(XEnchantment.THORNS), easyMap(XEnchantment.THORNS)))
            .put(XEnchantment.SWEEPING_EDGE, new EnchantConfig("Sweeping", "Increases damage to mobs from a sweep attack.", easyTxtMap(XEnchantment.SWEEPING_EDGE), easyMap(XEnchantment.SWEEPING_EDGE), easyMap(XEnchantment.SWEEPING_EDGE)))
            .put(XEnchantment.SILK_TOUCH, new EnchantConfig("Silk Touch", "Get the ore instead of ingot when mine.", easyTxtMap(XEnchantment.SILK_TOUCH), easyMap(XEnchantment.SILK_TOUCH), easyMap(XEnchantment.SILK_TOUCH)))
            .put(XEnchantment.RIPTIDE, new EnchantConfig("Rip", "Your trident will propel you forward.", easyTxtMap(XEnchantment.RIPTIDE), easyMap(XEnchantment.RIPTIDE), easyMap(XEnchantment.RIPTIDE)))
            .put(XEnchantment.QUICK_CHARGE, new EnchantConfig("Quick Charge", "Reload Quickly your crossbow.", easyTxtMap(XEnchantment.QUICK_CHARGE), easyMap(XEnchantment.QUICK_CHARGE), easyMap(XEnchantment.QUICK_CHARGE)))
            .put(XEnchantment.PROTECTION_PROJECTILE, new EnchantConfig("Projectile Protection", "Reduces damage from projectiles.", easyTxtMap(XEnchantment.PROTECTION_PROJECTILE), easyMap(XEnchantment.PROTECTION_PROJECTILE), easyMap(XEnchantment.PROTECTION_PROJECTILE)))
            .put(XEnchantment.PROTECTION_FIRE, new EnchantConfig("Fire Protection", "Reduces damage from fire.", easyTxtMap(XEnchantment.PROTECTION_FIRE), easyMap(XEnchantment.PROTECTION_FIRE), easyMap(XEnchantment.PROTECTION_FIRE)))
            .put(XEnchantment.PROTECTION_FALL, new EnchantConfig("Feather Falling", "Reduces damage when fall.", easyTxtMap(XEnchantment.PROTECTION_FALL), easyMap(XEnchantment.PROTECTION_FALL), easyMap(XEnchantment.PROTECTION_FALL)))
            .put(XEnchantment.PROTECTION_EXPLOSIONS, new EnchantConfig("Blast Protection", "Reduces damage from explosions.", easyTxtMap(XEnchantment.PROTECTION_EXPLOSIONS), easyMap(XEnchantment.PROTECTION_EXPLOSIONS), easyMap(XEnchantment.PROTECTION_EXPLOSIONS)))
            .put(XEnchantment.PROTECTION_ENVIRONMENTAL, new EnchantConfig("Protection", "Reduces general damage.", easyTxtMap(XEnchantment.PROTECTION_ENVIRONMENTAL), easyMap(XEnchantment.PROTECTION_ENVIRONMENTAL), easyMap(XEnchantment.PROTECTION_ENVIRONMENTAL)))
            .put(XEnchantment.PIERCING, new EnchantConfig("Piercing", "Pierce enemies with crossbow.", easyTxtMap(XEnchantment.PIERCING), easyMap(XEnchantment.PIERCING), easyMap(XEnchantment.PIERCING)))
            .put(XEnchantment.OXYGEN, new EnchantConfig("Respiration", "Increases breathe underwater.", easyTxtMap(XEnchantment.OXYGEN), easyMap(XEnchantment.OXYGEN), easyMap(XEnchantment.OXYGEN)))
            .put(XEnchantment.MULTISHOT, new EnchantConfig("Triple Shoot", "Throws triple arrows from crossbow.", easyTxtMap(XEnchantment.MULTISHOT), easyMap(XEnchantment.MULTISHOT), easyMap(XEnchantment.MULTISHOT)))
            .put(XEnchantment.MENDING, new EnchantConfig("Mending", "Mend your equipped tools.", easyTxtMap(XEnchantment.MENDING), easyMap(XEnchantment.MENDING), easyMap(XEnchantment.MENDING)))
            .put(XEnchantment.LURE, new EnchantConfig("Lure", "Decreases wait time while fishing.", easyTxtMap(XEnchantment.LURE), easyMap(XEnchantment.LURE), easyMap(XEnchantment.LURE)))
            .put(XEnchantment.LUCK, new EnchantConfig("Luck Of Sea", "Increases luck while fishing.", easyTxtMap(XEnchantment.LUCK), easyMap(XEnchantment.LUCK), easyMap(XEnchantment.LUCK)))
            .put(XEnchantment.LOYALTY, new EnchantConfig("Loyal", "Return trident to your hand when thrown.", easyTxtMap(XEnchantment.LOYALTY), easyMap(XEnchantment.LOYALTY), easyMap(XEnchantment.LOYALTY)))
            .put(XEnchantment.LOOT_BONUS_MOBS, new EnchantConfig("Looting", "Increases the chance of gain extra mob loot.", easyTxtMap(XEnchantment.LOOT_BONUS_MOBS), easyMap(XEnchantment.LOOT_BONUS_MOBS), easyMap(XEnchantment.LOOT_BONUS_MOBS)))
            .put(XEnchantment.LOOT_BONUS_BLOCKS, new EnchantConfig("Fortune", "Increases the chance of gain extra block loot.", easyTxtMap(XEnchantment.LOOT_BONUS_BLOCKS), easyMap(XEnchantment.LOOT_BONUS_BLOCKS), easyMap(XEnchantment.LOOT_BONUS_BLOCKS)))
            .put(XEnchantment.KNOCKBACK, new EnchantConfig("KnockBack", "Make your weapons push your enemies backwards.", easyTxtMap(XEnchantment.KNOCKBACK), easyMap(XEnchantment.KNOCKBACK), easyMap(XEnchantment.KNOCKBACK)))
            .put(XEnchantment.SOUL_SPEED, new EnchantConfig("Soul Speed", "Make your boots faster.", easyTxtMap(XEnchantment.SOUL_SPEED), easyMap(XEnchantment.SOUL_SPEED), easyMap(XEnchantment.SOUL_SPEED)))
            .put(XEnchantment.IMPALING, new EnchantConfig("Impale", "Increase your trident damage.", easyTxtMap(XEnchantment.IMPALING), easyMap(XEnchantment.IMPALING), easyMap(XEnchantment.IMPALING)))
            .put(XEnchantment.FROST_WALKER, new EnchantConfig("Frost", "Create ice when walking over water.", easyTxtMap(XEnchantment.FROST_WALKER), easyMap(XEnchantment.FROST_WALKER), easyMap(XEnchantment.FROST_WALKER)))
            .build();


    @Delayed(time = 1)
    public void reload() {
        vanillaEnchantments.entrySet().stream()
                .filter(this::isCompatible)
                .map(this::buildEnchantment)
                .forEach(CoreEnchants::registerNewEnchantment);
    }
    private ICoreEnchantment buildEnchantment(Map.Entry<XEnchantment, EnchantConfig> entry) {

        int maxLevel = maxLevel(entry.getKey());

        return VanillaEnchantmentFactory.builder()
                .identifier(key(entry.getKey()))
                .maxLevel(maxLevel)
                .enabled(entry.getValue().isEnabled())
                .requiredBookShelf(entry.getValue().getRequiredBookShelf())

                .requiredMoneyToEnchant(MapUtils.check(entry.getValue().getRequiredMoneyToEnchant()))
                .requiredXpLevelToEnchant(MapUtils.check(entry.getValue().getRequiredXpLevelToEnchant()))
                .requiredPermissionsToEnchant(MapUtils.check(entry.getValue().getRequiredPermissionsToEnchant()))

                .displayName(entry.getValue().getDisplayName())
                .description(entry.getValue().getDescription())
                .enchantment(entry.getKey().getEnchant())
                .build()
                .build(ProviderType.VANILLA_ENCHANT);
    }

    private int maxLevel(XEnchantment xEnchantment) {
        Optional<Enchantment> enchantment = Optional.ofNullable(xEnchantment.getEnchant());
        return enchantment.map(Enchantment::getMaxLevel).orElse(1);
    }

    private String key(XEnchantment enchantment) {
        return enchantment.toString().replace(" ", "_").toLowerCase();
    }

    private Map<Integer, Double> easyMap(XEnchantment xEnchantment) {
        return new HashMap<Integer, Double>() {{
            for (int i = 1; i<=maxLevel(xEnchantment); i++) put(i, 15d * i);
        }};
    }

    private Map<Integer, String> easyTxtMap(XEnchantment xEnchantment) {
        return new HashMap<Integer, String>() {{
            for (int i = 1; i<=maxLevel(xEnchantment); i++) put(i, "ench."+ key(xEnchantment) +".level." + i);
        }};
    }

    private boolean isCompatible(Map.Entry<XEnchantment, EnchantConfig> entry) {
        try {
            Enchantment enchantment = entry.getKey().getEnchant();

            return enchantment != null;
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&c" + entry.getKey().toString() + " is not compatible with the current version!"));
            return false;
        }
    }

}
