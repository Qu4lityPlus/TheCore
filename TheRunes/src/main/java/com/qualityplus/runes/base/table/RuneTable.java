package com.qualityplus.runes.base.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public final class RuneTable {
    private @Getter Location spawnLocation;
    private List<UUID> entities = new ArrayList<>();

    public void removeTable() {
        entities.stream()
                .map(Bukkit::getEntity)
                .filter(Objects::nonNull)
                .forEach(Entity::remove);
    }
}
