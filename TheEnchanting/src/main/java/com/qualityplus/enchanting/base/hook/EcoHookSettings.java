package com.qualityplus.enchanting.base.hook;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.enchanting.api.settings.EcoWarning;
import com.qualityplus.enchanting.base.config.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

public final class EcoHookSettings implements EcoWarning {
    @Inject
    private Plugin plugin;
    @Inject
    private Logger logger;
    @Inject
    private Config config;

    @Override
    public void sendWarning() {
        boolean addDescriptionsFromEcoEnchants = false;
        boolean addDescriptionsFromTheEnchanting = config.enchantmentsDisplay.displayEnchantmentsInLore;

        try {

            String path = plugin.getDataFolder().getAbsolutePath() + "/../EcoEnchants/config.yml";

            File file = new File(path);

            if (file.exists()) {
                FileConfiguration customConfig = new YamlConfiguration();

                customConfig.load(file);

                addDescriptionsFromEcoEnchants = customConfig.getBoolean("display.descriptions.enabled");
            }
        } catch (Exception ignored) {
        }


        if (addDescriptionsFromEcoEnchants && addDescriptionsFromTheEnchanting) {
            logger.warning("Enchantments descriptions in lore are enabled in EcoEnchants and TheEnchanting!");
            logger.warning("This will produce bugs with item's lore please disable it in TheEnchanting or in EcoEnchants and start server again!");

            //Bukkit.getServer().getPluginManager().disablePlugin(plugin);
        } else {
            String using = addDescriptionsFromEcoEnchants ? "EcoEnchants" : "TheEnchanting";

            logger.info(String.format("Plugin is using Lore Enchantments from %s!", using));
        }

    }
}
