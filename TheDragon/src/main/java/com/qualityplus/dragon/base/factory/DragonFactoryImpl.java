package com.qualityplus.dragon.base.factory;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.dragon.api.factory.DragonFactory;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.base.configs.Config;
import com.qualityplus.dragon.base.configs.Config.GameSettings;
import com.qualityplus.dragon.base.game.dragon.TheDragonEntityImpl;
import com.qualityplus.dragon.base.game.dragon.TheMythicDragonEntity;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

import java.util.*;

@Component
public final class DragonFactoryImpl implements DragonFactory {
    private @Inject Config configuration;

    @Override
    public TheDragonEntity getDragon(Map<String, TheDragonEntityImpl> map) {
        GameSettings settings = configuration.eventSettings;

        if (settings.dragonSettings.useRandomDragon) {
            return getRandomDragon(map);
        } else {
            if (settings.dragonSettings.takeFromMythicMobs)
                return new TheMythicDragonEntity(settings.dragonSettings.dragonId, 100, 100, settings.dragonSettings.mythicMobDragonLevel);
            else
                return map.get(settings.dragonSettings.dragonId);
        }
    }

    private TheDragonEntity getRandomDragon(Map<String, TheDragonEntityImpl> dragonMap) {
        if (dragonMap.size() == 0)
            return null;

        List<TheDragonEntity> dragonList = new ArrayList<>(dragonMap.values());
        dragonList.sort(Comparator.comparingDouble(TheDragonEntity::getChance));
        int random = new Random().nextInt(100);
        TheDragonEntity randomDragon = null;
        for (TheDragonEntity dragon : dragonList)
            if (random < dragon.getChance()) {
                randomDragon = dragon;
                break;
            }
        return randomDragon == null ? dragonList.get(0) : randomDragon;
    }
}
