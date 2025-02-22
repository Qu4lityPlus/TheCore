package com.qualityplus.runes.base.rune.effects.apply;

import com.qualityplus.runes.TheRunes;
import com.qualityplus.runes.base.rune.Rune;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class BowEffect implements ChainEffect{
    private final Map<UUID, Integer> taskMap = new HashMap<>();

    @Override
    public void execute(Player player, Entity entity, Rune rune) {
        UUID random = UUID.randomUUID();

        taskMap.put(random, Bukkit.getScheduler().runTaskTimer(TheRunes.getApi().getBox().plugin(), () -> {

            Location location =  entity.getLocation();

            if (!arrowIsDead(entity)) {

                displayEffect(location, rune);

                displayFakeItems(location, rune);

                return;
            }

            cancel(random);


        }, 0, 4).getTaskId());
    }

    private boolean arrowIsDead(Entity entity) {
        return entity == null || entity.isDead() || entity.isOnGround();
    }

    private void cancel(UUID uuid) {
        Optional.ofNullable(taskMap.getOrDefault(uuid, null)).ifPresent(id -> Bukkit.getScheduler().cancelTask(id));

        taskMap.remove(uuid);
    }
}
