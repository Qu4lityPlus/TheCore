package com.qualityplus.minions.base.recipe.provider;

import com.qualityplus.crafting.api.recipes.Recipes;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.minions.api.recipe.Recipe;
import com.qualityplus.minions.api.recipe.provider.RecipeProvider;
import com.qualityplus.minions.base.recipe.MinionRecipe;

public final class TheCraftingRecipeProvider implements RecipeProvider {
    @Override
    public Recipe getRecipe(String id) {
        CustomRecipe customRecipe = Recipes.getByID(id);

        if (customRecipe == null) return null;

        return new MinionRecipe(customRecipe.getIngredients(), customRecipe.getResult());
    }
}
