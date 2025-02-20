package com.qualityplus.dragon.base.game.structure;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.addons.PasterAddon;
import com.qualityplus.assistant.api.addons.paster.schematic.Schematic;
import com.qualityplus.assistant.api.addons.paster.session.PasterSession;
import com.qualityplus.assistant.base.addons.paster.session.DefaultSession;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.dragon.api.exception.InvalidSpawnException;
import com.qualityplus.dragon.api.game.structure.GameStructure;
import com.qualityplus.dragon.api.game.structure.GameStructures;
import com.qualityplus.dragon.api.game.structure.type.DragonAltar;
import com.qualityplus.dragon.api.game.structure.type.DragonCrystal;
import com.qualityplus.dragon.api.game.structure.type.DragonSpawn;
import com.qualityplus.dragon.api.service.SchematicService;
import com.qualityplus.dragon.base.configs.Config;
import com.qualityplus.dragon.base.configs.StructuresFile;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public final class GameStructuresImpl implements GameStructures {
    private @Inject SchematicService schematicService;
    private @Inject StructuresFile structures;
    private @Inject Config config;

    @Override
    public void clearAltars() {
        getAltars().forEach(dragonAltar -> dragonAltar.setEnderKey(false));
    }

    @Override
    public void spawnCrystals() {
        getSpawn().ifPresent(spawn -> getCrystals().forEach(DragonCrystal::spawn));
    }

    @Override
    public void deSpawnCrystals() {
        getCrystals().forEach(GameStructure::removeStructure);
    }

    @Override
    public CompletableFuture<PasterSession> pasteSchematic() throws InvalidSpawnException {
        Optional<Schematic> schematic = schematicService.getSchematic(config.eventSettings.schematicSettings.id);

        PasterAddon worldEditPlugin = TheAssistantPlugin.getAPI().getAddons().getPaster();

        CompletableFuture<PasterSession> future = new CompletableFuture<>();

        Optional<DragonSpawn> spawn = getSpawn();

        if (spawn.isPresent())
            if (schematic.isPresent())
                return worldEditPlugin.pasteSchematic(spawn.get().getLocation(), schematic.get());
            else
                future.complete(new DefaultSession());
        else
            throw new InvalidSpawnException();

        return future;
    }

    @Override
    public boolean schematicExists() {
        return schematicService.schematicExist();
    }

    @Override
    public List<GameStructure> getStructures() {
        return structures.getStructures();
    }

    @Override
    public List<DragonCrystal> getCrystals() {
        return structures.crystals.stream().map(crystal -> (DragonCrystal) crystal).collect(Collectors.toList());
    }

    @Override
    public List<DragonAltar> getAltars() {
        return structures.altars.stream().map(altar -> (DragonAltar) altar).collect(Collectors.toList());
    }

    @Override
    public Optional<DragonCrystal> getCrystal(Location location) {
        return structures.crystals.stream()
                .filter(structure -> structure.getLocation() != null && structure.getLocation().equals(location))
                .map(structure -> (DragonCrystal) structure).findFirst();
    }

    @Override
    public Optional<DragonAltar> getAltar(Location location) {
        return structures.altars.stream()
                .filter(structure -> structure.getLocation() != null && structure.getLocation().equals(location))
                .map(structure -> (DragonAltar) structure).findFirst();
    }

    @Override
    public Optional<DragonSpawn> getSpawn() {
        return Optional.ofNullable(structures.dragonSpawn);
    }

    @Override
    public boolean isCrystal(Entity entity) {
        return entity.hasMetadata("theDragonCrystal");
    }

    @Override
    public boolean isAltar(Entity entity) {
        return entity.hasMetadata("theDragonAltar");
    }

    @Override
    public void addStructure(GameStructure structure) {
        structures.addStructure(structure);
    }

    @Override
    public void removeStructure(GameStructure structure) {
        structures.removeStructure(structure);
    }
}
