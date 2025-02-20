package com.qualityplus.dragon.base.configs;

import com.qualityplus.dragon.api.game.guardian.Guardian;
import com.qualityplus.dragon.base.game.guardian.DragonGuardian;
import com.qualityplus.dragon.base.game.guardian.GuardianArmor;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration(path = "guardians.yml")
@Header("================================")
@Header("       Dragon Guardians      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class DragonGuardiansFile extends OkaeriConfig {
    public Map<String, DragonGuardian> guardianMap = new HashMap<>();
    public List<Location> guardianSpawns = new ArrayList<>();

    public void addSpawn(Location location) {
        initSpawns();
        guardianSpawns.add(location);
    }

    public void removeSpawn(Location location) {
        initSpawns();
        guardianSpawns.remove(location);
    }

    private void initSpawns() {
        if (guardianSpawns == null) guardianSpawns = new ArrayList<>();
    }

    public Guardian getGuardianById(String name) {
        return guardianMap.getOrDefault(name, null);
    }

    public void addGuardian(String name) {
        guardianMap.put(name, new DragonGuardian(name, new GuardianArmor()));
    }

    public void remove(String name) {
        guardianMap.remove(name);
    }
}