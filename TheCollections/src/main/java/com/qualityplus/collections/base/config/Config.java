package com.qualityplus.collections.base.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XSound;
import com.qualityplus.assistant.api.config.ConfigDatabase;
import com.qualityplus.assistant.api.config.ConfigMessages;
import com.qualityplus.assistant.api.config.ConfigSound;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Comment;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@Configuration()
@Header("================================")
@Header("       Config      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Config extends OkaeriConfig {
    public String prefix = "[TheCollections] ";

    @Comment("Database Configuration")
    @Comment("Allowed Database Types")
    @Comment("- H2")
    @Comment("- FLAT")
    @Comment("- MYSQL")
    @Comment("- REDIS")
    public ConfigDatabase configDatabase = new ConfigDatabase();
    public GainXPSettings gainXPSettings = new GainXPSettings();
    public LevelUpSettings levelUpSettings = new LevelUpSettings();
    public UnlockSettings unlockSettings = new UnlockSettings();
    @Comment("Set this in 'true' if you want players to get XP from Drops generated from TNT explosions")
    public boolean allowXPFromTNTExplosions = false;
    public static class UnlockSettings extends OkaeriConfig{
        public ConfigSound sound = new ConfigSound(XSound.ENTITY_EXPERIENCE_BOTTLE_THROW, true, 0.2f, 1f);
    }

    public static class GainXPSettings extends OkaeriConfig{
        public ConfigSound sound = new ConfigSound(XSound.ENTITY_EXPERIENCE_ORB_PICKUP, true, 0.2f, 1f);
    }

    @Getter
    public static class LevelUpSettings extends OkaeriConfig{
        public ConfigSound sound = new ConfigSound(XSound.ENTITY_PLAYER_LEVELUP, true, 0.2f, 1f);
        public ConfigMessages message = new ConfigMessages(Arrays.asList(
                "&e&l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "&6&lCOLLECTION LEVEL UP &e%collection_displayname% %collection_level_roman%",
                "&c  ",
                "&a&a&lREWARDS",
                "&c%collection_info_message%",
                "&e&l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"


                ), true);
    }
}
