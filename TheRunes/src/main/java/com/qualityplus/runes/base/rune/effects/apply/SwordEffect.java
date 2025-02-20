package com.qualityplus.runes.base.rune.effects.apply;

import com.qualityplus.runes.base.rune.Rune;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Optional;

public final class SwordEffect implements ChainEffect{
    @Override
    public void execute(Player player, Entity entity, Rune rune) {
        Location location = entity.getLocation();

        displayEffect(location, rune);

        displayFakeItems(location, rune);

        if (!rune.getEffect().isStrikeLightning()) return;

        Optional.ofNullable(location.getWorld()).ifPresent(world -> world.strikeLightningEffect(location));
    }
}
