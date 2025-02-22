package com.qualityplus.dragon.listener;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.scheduler.PlatformScheduler;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.service.GuardianEditService;
import com.qualityplus.dragon.base.editmode.GuardianEditMode;
import com.qualityplus.dragon.base.game.guardian.DragonGuardian;
import com.qualityplus.dragon.base.service.GuardianEditServiceImpl.EditType;
import com.qualityplus.dragon.gui.guardian.GuardianGUI;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * Handles the Setup
 */
@Component
public final class GuardianEditListener implements Listener {
    private @Inject("scheduler") PlatformScheduler scheduler;
    private @Inject GuardianEditService setupService;
    private @Inject Box box;

    @EventHandler
    public void checkChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        final UUID uuid = player.getUniqueId();

        final GuardianEditMode editMode = setupService.getByPlayer(uuid);

        if (editMode == null ) return;

        DragonGuardian guardian = editMode.getDragonGuardian();

        EditType type = editMode.getEditType();

        String message = StringUtils.unColor(event.getMessage());

        event.setCancelled(true);

        if (message.contains("stop") || message.contains("exit")) {
            openGUIAsync(player, guardian);
        } else {
            manageEditMode(player, type, guardian, message);
        }
    }

    private void manageEditMode(Player player, EditType type, DragonGuardian guardian, String message) {
        try {
            switch (type) {
                case MOB:
                    EntityType.valueOf(message);

                    guardian.setEntity(message);
                    break;
                case HEALTH:
                    double manaCost = Double.parseDouble(message);

                    guardian.setHealth(manaCost);
                    break;
                case DISPLAYNAME:
                    guardian.setDisplayName(message);
                    break;

            }

            setupService.removeByPlayer(player.getUniqueId());

            openGUIAsync(player, guardian);
        } catch (NumberFormatException e) {
            player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidAmount
                    .replace("%prefix%", box.files().config().prefix)));
        } catch (IllegalArgumentException e) {
            player.sendMessage(StringUtils.color(box.files().messages().setupMessages.guardianMobTypeDoesntMatch
                    .replace("%prefix%", box.files().config().prefix)));
        }
    }

    public void openGUIAsync(Player player, DragonGuardian guardian) {
        scheduler.runLaterSync(() -> player.openInventory(new GuardianGUI(box, guardian).getInventory()),3L);
    }
}
