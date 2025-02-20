package com.qualityplus.dragon.base.service;

import com.qualityplus.assistant.api.addons.paster.schematic.Schematic;
import com.qualityplus.assistant.base.addons.paster.schematic.SchematicImpl;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.dragon.api.service.SchematicService;
import com.qualityplus.dragon.base.configs.Config;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public final class SchematicServiceImpl implements SchematicService {
    private final List<Schematic> schematics = new ArrayList<>();
    private @Inject Config config;
    private @Inject Plugin plugin;
    private @Inject Logger logger;

    @Override
    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void load() {
        Arrays.stream(Objects.requireNonNull(getSchematicsFolder().listFiles()))
                .collect(Collectors.toList())
                .forEach(this::loadSchematic);

        if (schematicExist()) {
            logger.info(String.format("Schematic %s was found in plugins/TheDragon/schematics!", config.eventSettings.schematicSettings.id));
            return;
        }

        logger.warning(String.format("Schematic %s wasn't found in plugins/TheDragon/schematics!", config.eventSettings.schematicSettings.id));
    }

    private File getSchematicsFolder() {
        JavaPlugin javaPlugin = (JavaPlugin) plugin;

        File schematicsFolder = new File(javaPlugin.getDataFolder() + "/schematics", "");

        schematicsFolder.mkdirs();

        return schematicsFolder;
    }

    private void loadSchematic(File file) {
        String fileName = file.getName();

        if (fileName.contains(".schematic") || fileName.contains(".schem")) {
            String name = fileName.replace(".schematic", "").replace(".schem", "");

            schematics.add(new SchematicImpl(name, file));

            //logger.info(String.format("Successfully found %s schematic!", name));
        }
    }

    @Override
    public Optional<Schematic> getSchematic(String name) {
        return schematics.stream().filter(schematic -> schematic.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public boolean schematicExist() {
        return getSchematic(config.eventSettings.schematicSettings.id).isPresent();
    }
}
