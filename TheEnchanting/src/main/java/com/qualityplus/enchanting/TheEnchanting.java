package com.qualityplus.enchanting;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.okaeri.OkaeriSilentPlugin;
import com.qualityplus.enchanting.api.TheEnchantingAPI;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Scan;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.ExecutionPhase;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.Planned;
import lombok.Getter;

import java.io.File;
import java.util.Optional;

@Scan(deep = true)
public final class TheEnchanting extends OkaeriSilentPlugin {
    private static @Inject
    @Getter TheEnchantingAPI api;

    @Planned(ExecutionPhase.PRE_SETUP)
    private void preSetupRecipes() {
        try {
            String path = this.getDataFolder().getAbsolutePath() + "/recipes.yml";

            File file = new File(path);

            if (!file.exists())
                return;

            Optional.ofNullable(file).ifPresent(File::delete);
        } catch (Exception ignored) {
        }
    }
}
