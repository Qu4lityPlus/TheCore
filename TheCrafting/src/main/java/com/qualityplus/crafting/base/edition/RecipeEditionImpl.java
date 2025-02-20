package com.qualityplus.crafting.base.edition;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.api.edition.EditionObject;
import com.qualityplus.crafting.api.edition.RecipeEdition;
import com.qualityplus.crafting.base.gui.individual.RecipeIndividualGUI;
import com.qualityplus.crafting.util.CraftingPlaceholderUtils;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public final class RecipeEditionImpl implements RecipeEdition, Listener {
    private final Map<UUID, EditionObject> editMap = new HashMap<>();
    private @Inject Box box;

    @Override
    public void setEditMode(UUID uuid, EditionObject type) {
        editMap.put(uuid, type);
    }

    @Override
    public void removeEditMode(UUID uuid) {
        editMap.remove(uuid);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        EditionObject editionObject = editMap.getOrDefault(player.getUniqueId(), null);

        if (editionObject == null) return;

        String message = event.getMessage();

        if (message == null) return;

        event.setCancelled(true);

        if (!message.equalsIgnoreCase("cancel") && !message.equalsIgnoreCase("exit")) {
            List<IPlaceholder> placeholder = CraftingPlaceholderUtils.getRecipePlaceholders(editionObject.getRecipe());

            if (editionObject.getType() == EditionType.PERMISSION)
                editionObject.getRecipe().setRecipePermission(message);
            else if (editionObject.getType() == EditionType.CATEGORY)
                editionObject.getRecipe().setCategory(message);
            else if (editionObject.getType() == EditionType.SLOT)
                editionObject.getRecipe().setSlot(NumberUtil.intOrZero(message));
            else if (editionObject.getType() == EditionType.PAGE)
                editionObject.getRecipe().setPage(NumberUtil.intOrZero(message));
            else
                editionObject.getRecipe().setDisplayName(message);

            String toSend = editionObject.getType() == EditionType.PERMISSION ? box.files().messages().recipeMessages.successfullySetPermission :
                    editionObject.getType() == EditionType.DISPLAY_NAME ? box.files().messages().recipeMessages.successfullySetDisplayName :
                            editionObject.getType() == EditionType.SLOT ? box.files().messages().recipeMessages.successfullySetSlot :
                                    editionObject.getType() == EditionType.PAGE ? box.files().messages().recipeMessages.successfullySetPage :
                                            box.files().messages().recipeMessages.successfullySetCategory;

            player.sendMessage(StringUtils.processMulti(toSend, placeholder));
        }

        removeEditMode(player.getUniqueId());

        Bukkit.getScheduler().runTask(box.plugin(), () -> player.openInventory(new RecipeIndividualGUI(box, editionObject.getRecipe(), this).getInventory()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        editMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        editMap.remove(event.getPlayer().getUniqueId());
    }
}
