package com.qualityplus.crafting.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.api.edition.RecipeEdition;
import com.qualityplus.crafting.api.recipes.Recipes;
import com.qualityplus.crafting.base.gui.modify.ModifyRecipeGUI;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@Component
public final class CreateRecipeCommand extends AssistantCommand {
    private @Inject RecipeEdition edition;
    private @Inject Box box;

    @Override
    public boolean execute(final CommandSender sender, final String[] args) {
        final Player player = (Player) sender;

        if (args.length == 2) {
            final CustomRecipe exist = Recipes.getByID(args[1]);

            if (exist != null) {
                player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.recipeAlreadyExist));
                return false;
            }

            player.openInventory(new ModifyRecipeGUI(box, CustomRecipe.builder()
                    .id(args[1])
                    .displayName(args[1])
                    .recipePermission(args[1] + ".permission")
                    .build(), edition).getInventory());
        } else {
            player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().createCommand));
    }
}