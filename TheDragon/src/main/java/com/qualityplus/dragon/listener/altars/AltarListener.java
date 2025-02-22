package com.qualityplus.dragon.listener.altars;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.exception.check.NoSpawnException;
import com.qualityplus.dragon.api.exception.check.NoStructureException;
import com.qualityplus.dragon.api.game.structure.type.DragonAltar;
import com.qualityplus.dragon.api.service.AltarSetupService;
import com.qualityplus.dragon.util.DragonItemStackUtil;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Handles the Process to start the game
 */
@Component
public final class AltarListener implements Listener {
    private @Inject AltarSetupService setupService;
    private @Inject Logger logger;
    private @Inject Box box;

    @EventHandler
    public void onUseTool(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof EnderCrystal)) return;
        ItemStack itemStack = e.getPlayer().getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasKey("dragonTool")) return;
        e.getRightClicked().remove();
    }

    /**
     * Listen for players who click in Altars
     *
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack itemStack = player.getItemInHand();

        if (!DragonItemStackUtil.isEnderKey(itemStack)) return;

        event.setCancelled(true);

        if (box.game().isActive()) {
            player.sendMessage(StringUtils.color(box.files().messages().setupMessages.errorDragonEventInProgress.replace("%prefix%", box.files().config().prefix)));
            return;
        }

        if (setupService.playerIsInEditMode(uuid)) {
            player.sendMessage(StringUtils.color(box.files().messages().setupMessages.errorInSetupMode.replace("%prefix%", box.files().config().prefix)));
            return;
        }

        manageAltars(player, event, itemStack);
    }

    /**
     * Handles the process when a player put an eye in
     * an altar
     *
     * @param player Player
     * @param event PlayerInteractEvent
     * @param itemStack ItemStack in player's hand
     */
    private void manageAltars(Player player, PlayerInteractEvent event, ItemStack itemStack) {
        Block block = event.getClickedBlock();

        if (block == null) return;

        Optional<DragonAltar> altar = box.structures().getAltar(block.getLocation());

        if (!altar.isPresent()) {
            player.sendMessage(StringUtils.color(box.files().messages().gameMessages.thatBlockIsNotAnAltar.replace("%prefix%", box.files().config().prefix)));
            return;
        }

        if (altar.get().isEnderKey()) {
            player.sendMessage(StringUtils.color(box.files().messages().gameMessages.alreadyPlacedAltar.replace("%prefix%", box.files().config().prefix)));
            return;
        }

        if (!gameCanStart(player))
            return;

        player.setItemInHand(BukkitItemUtil.getItemWithout(itemStack, 1));

        altar.ifPresent(dragonAltar -> dragonAltar.setEnderKey(true));

        sendAltarPlacedMessage(player);

        sendAltarsFilledMessage(player);
    }

    private boolean gameCanStart(Player player) {
        try {
            return box.game().canStart();
        } catch (NoSpawnException e) {
            player.sendMessage(StringUtils.color(box.files().messages().gameMessages.cantPlaceSpawn
                    .replace("%prefix%", box.files().config().prefix)));
            return false;
        } catch (NoStructureException e) {
            player.sendMessage(StringUtils.color(box.files().messages().gameMessages.cantPlaceStructure
                    .replace("%prefix%", box.files().config().prefix)));
            return false;
        }
    }

    private void sendAltarPlacedMessage(Player player) {
        List<DragonAltar> altarList = box.structures().getAltars();

        String message = StringUtils.processMulti(box.files().messages().gameMessages.placedEnderKey, getPlaceholders(altarList));

        player.sendMessage(StringUtils.color(message));
    }

    private void sendAltarsFilledMessage(Player player) {
        List<DragonAltar> altarList = box.structures().getAltars();

        int current = (int) altarList.stream().filter(DragonAltar::isEnderKey).count();
        int total = altarList.size();

        if (current < total) return;

        altarList.forEach(dragonAltar1 -> dragonAltar1.setEnderKey(false));

        String message = StringUtils.processMulti(box.files().messages().gameMessages.altarsFilled, getPlaceholders(altarList));

        player.sendMessage(StringUtils.color(message));

        box.game().start();
    }

    private List<IPlaceholder> getPlaceholders(List<DragonAltar> altarList) {
        int current = (int) altarList.stream().filter(DragonAltar::isEnderKey).count();
        int total = altarList.size();

        return Arrays.asList(
                new Placeholder("thedragon_ender_key_current", current),
                new Placeholder("thedragon_ender_key_total", total),
                new Placeholder("prefix", box.files().config().prefix));
    }
}