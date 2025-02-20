package com.qualityplus.souls.base.edition;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.souls.api.edition.EditionObject;
import com.qualityplus.souls.api.edition.SoulEdition;
import com.qualityplus.souls.base.gui.SoulsGUI;
import com.qualityplus.souls.base.gui.addcommands.AddCommandsGUI;
import com.qualityplus.souls.base.gui.addmessages.AddMessagesGUI;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public final class SoulEditionImpl implements SoulEdition, Listener {
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

        EditionType type = editionObject.getType();

        SoulsGUI gui = type == EditionType.ADD_COMMAND ? new AddCommandsGUI(box, editionObject.getSoul(), 1, this) : new AddMessagesGUI(box, editionObject.getSoul(), 1, this);

        if (!message.equalsIgnoreCase("cancel") && !message.equalsIgnoreCase("exit")) {

            if (type == EditionType.ADD_COMMAND)
                editionObject.getSoul().getCommands().add(message);
            else if (type == EditionType.ADD_MESSAGE)
                editionObject.getSoul().getMessages().add(message);

            String toSend = type == EditionType.ADD_COMMAND ?
                            box.files().messages().soulsMessages.successfullyAddedCommand :
                            box.files().messages().soulsMessages.successfullyAddedMessage;

            player.sendMessage(StringUtils.color(toSend));
        }

        Bukkit.getScheduler().runTask(box.plugin(), () -> player.openInventory(gui.getInventory()));
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
