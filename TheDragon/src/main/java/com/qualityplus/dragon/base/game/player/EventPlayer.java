package com.qualityplus.dragon.base.game.player;

import com.qualityplus.dragon.api.game.player.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public final class EventPlayer implements GamePlayer {
    private PlayerStatus status;
    private final String name;
    private final UUID uuid;
    private double damage;

    public EventPlayer(final UUID uuid, final double damage, final PlayerStatus status) {
        this.uuid = uuid;
        this.status = status;
        this.damage = damage;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addDamage(double damage) {
        this.damage+=damage;
    }

    @Override
    public void removeDamage(double damage) {
        this.damage-=damage;
    }

    @Override
    public void sendMessage(String message) {
        Optional.ofNullable(getPlayer()).ifPresent(player -> player.sendMessage(message));
    }

    @Override
    public void sendMessage(List<String> message) {
        message.forEach(this::sendMessage);
    }

    public void switchStatus() {
        if (this.status == PlayerStatus.ACTIVE) {
            this.status = PlayerStatus.INACTIVE;
        } else {
            this.status = PlayerStatus.ACTIVE;
        }
    }

    public boolean isActive() {
        return this.status == PlayerStatus.ACTIVE;
    }
}
