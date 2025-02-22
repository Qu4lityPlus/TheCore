package com.qualityplus.crafting;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.okaeri.OkaeriSilentPlugin;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.crafting.api.TheCraftingAPI;
import com.qualityplus.crafting.base.config.Inventories;
import com.qualityplus.crafting.base.config.RecipesFile;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Scan;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.ExecutionPhase;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.Planned;
import lombok.Getter;

import java.util.Arrays;
import java.util.logging.Logger;

@Scan(deep = true)
public final class TheCrafting extends OkaeriSilentPlugin {
    private static @Inject
    @Getter TheCraftingAPI api;

    @Planned(ExecutionPhase.PRE_SHUTDOWN)
    private void saveOnShutdown(final @Inject RecipesFile recipesFile) {
        recipesFile.saveRecipes();
    }

    @Planned(ExecutionPhase.POST_SETUP)
    private void fixInventories(final @Inject Logger logger, final @Inject Inventories inventories) {

        try {
            if (inventories.recipeBookSubGUI.getUnlockedItem() == null) {
                inventories.recipeBookSubGUI.setUnlockedItem(ItemBuilder.of(XMaterial.GRAY_DYE,  4, 1, "%crafting_recipe_result_item_displayname%", Arrays.asList("%crafting_recipe_result_item_lore%", "", "&eClick to view recipe")).build());
                inventories.save();
                logger.info("Successfully fixed problem with recipe book gui!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
