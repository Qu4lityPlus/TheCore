package com.qualityplus.minions.base.factory;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.lib.eu.okaeri.injector.OkaeriInjector;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.minions.api.recipe.provider.RecipeProvider;
import com.qualityplus.minions.base.recipe.provider.EmptyRecipeProviderImpl;
import com.qualityplus.minions.base.recipe.provider.TheCraftingRecipeProvider;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Bean;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;

@Component
public final class RecipeProviderFactory {
    private @Inject("injector") OkaeriInjector injector;

    @Bean
    public RecipeProvider createRecipeProvider() {
        if (TheAssistantPlugin.getAPI().getDependencyResolver().isPlugin("TheCrafting")) {
            return injector.createInstance(TheCraftingRecipeProvider.class);
        } else
            return injector.createInstance(EmptyRecipeProviderImpl.class);
    }
}
