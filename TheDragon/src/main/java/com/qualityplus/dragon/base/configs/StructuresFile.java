package com.qualityplus.dragon.base.configs;

import com.qualityplus.assistant.util.list.ListBuilder;
import com.qualityplus.assistant.util.list.ListUtils;
import com.qualityplus.dragon.api.game.structure.GameStructure;
import com.qualityplus.dragon.api.game.structure.type.DragonAltar;
import com.qualityplus.dragon.api.game.structure.type.DragonCrystal;
import com.qualityplus.dragon.api.game.structure.type.DragonSpawn;
import com.qualityplus.dragon.base.game.structure.DragonAltarImpl;
import com.qualityplus.dragon.base.game.structure.DragonCrystalImpl;
import com.qualityplus.dragon.base.game.structure.DragonSpawnImpl;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration(path = "structures.yml")
@Header("================================")
@Header("       Dragon Structures      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class StructuresFile extends OkaeriConfig {
    public DragonSpawnImpl dragonSpawn = new DragonSpawnImpl(null);
    public List<DragonAltarImpl> altars = new ArrayList<>();
    public List<DragonCrystalImpl> crystals = new ArrayList<>();


    public List<GameStructure> getStructures() {
        return new ListBuilder<GameStructure>()
                .with(parseToSuperClass(altars))
                .with(parseToSuperClass(crystals))
                .with(parseToSuperClass(Collections.singletonList(dragonSpawn)))
                .get();
    }

    public void addStructure(GameStructure gameStructure) {
        if (gameStructure == null) return;

        if (gameStructure instanceof DragonSpawn)
            dragonSpawn = (DragonSpawnImpl) gameStructure;
        else if (gameStructure instanceof DragonAltar)
            altars.add((DragonAltarImpl) gameStructure);
        else if (gameStructure instanceof DragonCrystal)
            crystals.add((DragonCrystalImpl) gameStructure);
    }

    public void removeStructure(GameStructure gameStructure) {
        if (gameStructure == null) return;

        if (gameStructure instanceof DragonAltar)
            altars.remove((DragonAltarImpl) gameStructure);
        else if (gameStructure instanceof DragonCrystal)
            crystals.remove((DragonCrystalImpl) gameStructure);
    }

    private List<GameStructure> parseToSuperClass(List<? extends GameStructure> structures) {
        return structures.stream().map(structure -> (GameStructure) structure).collect(Collectors.toList());
    }
}
