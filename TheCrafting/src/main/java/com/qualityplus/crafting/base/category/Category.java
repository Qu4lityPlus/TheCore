package com.qualityplus.crafting.base.category;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.crafting.api.recipes.Recipes;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public final class Category extends OkaeriConfig {
    private String id;
    private String displayName;
    private XMaterial icon;
    private String iconTexture;
    private int slot;

    public List<CustomRecipe> getRecipes() {
        return Recipes.values().stream()
                .filter(recipe -> recipe.getCategory() != null && recipe.getCategory().equals(id))
                .collect(Collectors.toList());
    }
}
