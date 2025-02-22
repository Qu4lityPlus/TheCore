package com.qualityplus.crafting.base.config;

import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;

@Configuration(path = "messages.yml")
@Header("================================")
@Header("       Messages      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Messages extends OkaeriConfig {
    public PluginMessages pluginMessages = new PluginMessages();
    public RecipeMessages recipeMessages = new RecipeMessages();

    public class RecipeMessages extends OkaeriConfig{
        public String recipeAlreadyExist = "&cThat Recipe already exists!";
        public String recipeDoesntExist = "&cThat Recipe doesn't exists!";
        public String defaultDescription = "&7Default Description...";
        public String successfullyDeletedRecipe = "&7That recipe has been successfully deleted!";

        public String recipeNotFoundPlaceholder = "&cRecipe not found!";
        public String noPermissionPlaceholder = "&cYou don't have permission to use that recipe!";
        public String noItemsMessage = "&cYou don't have required items to craft that recipe!";
        public String blockedCraftingPlaceholder = "&cThis recipe is disabled!";
        public String recipeResultCantBeEmpty = "&cRecipe result can't be empty!";
        public String recipeIngredientsCantBeEmpty = "&cRecipe ingredients can't be empty!";
        public String recipeSuccessfullyCreated = "&aYou successfully created recipe %crafting_recipe_id%!";


        public String typeDisplayName = "&7Type the &anew displayname &7in the chat to set or type &ccancel &7to exit from edit mode!";
        public String typePermission = "&7Type the &anew permission &7in the chat to set or type &ccancel &7to exit from edit mode!";
        public String typeCategory = "&7Type the &acategory &7in the chat to set or type &ccancel &7to exit from edit mode!";
        public String typeSlot = "&7Type the &aslot &7in the chat to set or type &ccancel &7to exit from edit mode!";
        public String typePage = "&7Type the &apage &7in the chat to set or type &ccancel &7to exit from edit mode!";

        public String successfullySetDisplayName = "&aSuccessfully set displayname of %crafting_recipe_id% recipe to %crafting_recipe_displayname%!";
        public String successfullySetPermission = "&aSuccessfully set permission of %crafting_recipe_id% recipe to %crafting_recipe_permission%!";
        public String successfullySetCategory = "&aSuccessfully set category of %crafting_recipe_id% recipe to %crafting_recipe_category%!";
        public String successfullySetPage = "&aSuccessfully set page of %crafting_recipe_id% recipe to %crafting_recipe_page%!";
        public String successfullySetSlot = "&aSuccessfully set slot of %crafting_recipe_id% recipe to %crafting_recipe_slot%!";
        public String notUnlockedYet = "&cYou haven't unlocked that recipe yet!";
        public String recipeDontBelongAnyCategory = "&cThat recipe don't belong to any category!";

    }

    public class PluginMessages extends OkaeriConfig {
        public String successfullyReloaded = "&aPlugin has been reloaded successfully!";

        public String invalidPlayer = "&cInvalid Player!";

        public String useSyntax = "&cUsage: %usage%!";

        public String noPermission = "&eYou don't have permission to do that!";
        public String unknownCommand = "&cUnknown command!";
        public String mustBeAPlayer = "&cYou must be a player to do that!";
        public String invalidArguments = "&cInvalid Arguments!";
        public String invalidAmount = "&cInvalid Amount!";
        public String useHelp = "&cUse: /crafting help";

        public String helpMessage = "&7%command% - &e%description%";
        public String helpHeader = "      &6&lTheCrafting   ";
        public String helpfooter = "&e<< &6Page %page% of %maxpage% &e>>";
        public String previousPage = "<<";
        public String nextPage = ">>";
        public String helpPageHoverMessage = "Click to go to page %page%";

    }
}
