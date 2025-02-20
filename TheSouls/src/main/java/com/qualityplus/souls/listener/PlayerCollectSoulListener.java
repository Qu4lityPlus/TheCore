package com.qualityplus.souls.listener;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.sound.SoundUtils;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.souls.base.soul.Soul;
import com.qualityplus.souls.persistance.data.SoulsData;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class PlayerCollectSoulListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void onPlace(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();

        if (!(e.getRightClicked() instanceof ArmorStand)) return;

        ArmorStand armorStand = (ArmorStand) e.getRightClicked();

        if (!armorStand.hasMetadata("soulData")) return;

        Optional<Soul> soul = box.files().souls().getByLocation(armorStand.getLocation());

        if (!soul.isPresent()) return;

        e.setCancelled(true);

        Optional<SoulsData> data = box.service().getData(player.getUniqueId());

        if (!data.isPresent()) return;

        if (data.get().getSoulsCollected().contains(soul.get().getUuid())) {
            player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.soulAlreadyFound));
            SoundUtils.playSound(player, box.files().config().soulAlreadyFoundSound);
            return;
        }

        checkIfItsFirst(player, data.get());

        player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.soulFound));

        data.get().getTiaSoulsCollected().add(soul.get().getUuid());
        data.get().getSoulsCollected().add(soul.get().getUuid());

        box.files().config().soulFoundCommands.stream()
                .map(command -> command.replaceAll("%player%", player.getName()))
                .forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command));

        soul.get().executeCommands(player);
        soul.get().sendMessages(player);

        SoundUtils.playSound(player, box.files().config().soulFoundSound);

        checkIfCollectedAll(player, data.get());

    }

    private void checkIfItsFirst(Player player, SoulsData data) {
        if (data.getSoulsCollected().size() >= 1) return;

        box.files().config().firstSoulFoundCommands.stream()
                .map(command -> command.replaceAll("%player%", player.getName()))
                .forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command));

        player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.firstSoulsFound));
    }

    private void checkIfCollectedAll(Player player, SoulsData data) {
        final List<UUID> collectedSouls = new ArrayList<>();

        for (UUID uuid : data.getSoulsCollected()) {
            if (box.files().souls().soulList.stream().anyMatch(soul -> soul.getUuid() == uuid)) {
                collectedSouls.add(uuid);
            }
        }

        data.setSoulsCollected(collectedSouls);

        if (data.getSoulsCollected().size() != box.files().souls().soulList.size()) return;

        box.files().config().allSoulsFoundCommands.stream()
                .map(command -> command.replaceAll("%player%", player.getName()))
                .forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command));

        player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.allSoulsFound));

        SoundUtils.playSound(player, box.files().config().allSoulsFoundSound);
    }
}
