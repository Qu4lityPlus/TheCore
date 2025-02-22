package com.qualityplus.dragon.base.game.guardian;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.dragon.api.factory.GuardianFactory;
import com.qualityplus.dragon.api.game.guardian.Guardian;
import com.qualityplus.dragon.base.configs.DragonEventsFile.GuardianChanceConfig;
import com.qualityplus.dragon.base.configs.DragonGuardiansFile;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import lombok.AllArgsConstructor;

import java.util.*;

@Component
public final class GuardianFactoryImpl implements GuardianFactory {
    private @Inject DragonGuardiansFile guardians;

    @Override
    public Guardian getRandom(List<GuardianChanceConfig> guardianList) {

        int r = new Random().nextInt(100);

        List<PercentageGuardian> guardians = new ArrayList<>();

        translateGuardianList(guardianList).forEach((iGuardian, integer) -> guardians.add(new PercentageGuardian(iGuardian, integer)));

        guardians.sort((o1, o2) -> o2.percentage - o1.percentage);

        for (PercentageGuardian guardian : guardians)
            if (r < guardian.percentage)
                return guardian.iGuardian;
        return guardians.isEmpty() ? null : guardians.get(0).iGuardian;
    }

    private Map<Guardian, Integer> translateGuardianList(List<GuardianChanceConfig> guardianList) {
        Map<Guardian, Integer> guardianIntegerMap = new HashMap<>();

        for (GuardianChanceConfig config : guardianList) {
            try {
                if (config.isFromMythicMobs)
                    guardianIntegerMap.put(new MythicGuardian(config.id, config.mythicMobLevel), config.chance);
                else
                    guardianIntegerMap.put(guardians.getGuardianById(config.id), config.chance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return guardianIntegerMap;
    }

    @AllArgsConstructor
    private static class PercentageGuardian{
        private final Guardian iGuardian;
        private final int percentage;
    }
}
