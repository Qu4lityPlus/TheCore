package com.qualityplus.souls.base.soul;

import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.location.ALocation;
import com.qualityplus.souls.TheSouls;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Exclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class Soul extends OkaeriConfig {
    private @Getter @Setter List<String> commands;
    private @Getter @Setter List<String> messages;
    private @Getter @Setter ALocation location;
    private @Exclude ArmorStand soulEntity;
    private @Getter @Setter UUID uuid;

    @Builder
    public Soul(UUID uuid, List<String> commands, List<String> messages, ALocation location) {
        this.commands = commands;
        this.location = location;
        this.messages = messages;
        this.uuid = uuid;
    }

    public void executeCommands(Player player) {
        commands.stream()
                .map(command -> command.replaceAll("%player%", player.getName()))
                .forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    public void sendMessages(Player player) {
        messages.stream()
                .map(message -> message.replaceAll("%player%", player.getName()))
                .forEach(message -> player.sendMessage(StringUtils.color(message)));
    }

    public void enable(Box box, boolean serverStart) {
        if (soulEntity != null) {
            soulEntity.remove();
        }

        final boolean loadAsync = Bukkit.getPluginManager().isPluginEnabled("BentoBox") || Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2");

        if (loadAsync && serverStart) {
            spawnSoul(box, 20 * 15);
            return;
        }

        spawnSoul(box, 1);
    }

    private void spawnSoul(final Box box, final int time) {
        Bukkit.getScheduler().runTaskLater(box.plugin(), () -> {
            Location loc = location.getLocation();

            soulEntity = loc.getWorld().spawn(loc, ArmorStand.class);

            soulEntity.setVisible(false);

            soulEntity.setGravity(false);

            soulEntity.setHelmet(ItemStackUtils.makeItem(box.files().config().soulItem));

            soulEntity.setInvulnerable(true);

            soulEntity.setCustomName("theSouls");

            soulEntity.setCustomNameVisible(false);

            soulEntity.setMetadata("soulData", new FixedMetadataValue(box.plugin(), "soulData"));
        }, time);
    }

    public void disable() {
        Optional.ofNullable(soulEntity).ifPresent(Entity::remove);
    }
}
