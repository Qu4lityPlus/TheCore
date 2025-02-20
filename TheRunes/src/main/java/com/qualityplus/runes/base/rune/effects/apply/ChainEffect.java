package com.qualityplus.runes.base.rune.effects.apply;

import com.destroystokyo.paper.ParticleBuilder;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.runes.TheRunes;
import com.qualityplus.runes.base.event.FakeEntitySpawnEvent;
import com.qualityplus.runes.base.rune.Rune;
import com.qualityplus.runes.base.rune.RuneEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public interface ChainEffect {
    void execute(Player player, Entity entity, Rune rune);

    default Color getColor(RuneEffect effect) {
        try {
            if (effect.getParticleColor() == null || effect.getParticleColor().getColor() == null) return null;

            return effect.getParticleColor().getColor();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    default void displayEffect(Location location, Rune rune) {
        RuneEffect effect = rune.getEffect();

        try {
            Color color = getColor(effect);

            if (color == null) {
                new ParticleBuilder(effect.getParticle())
                        .count(effect.getParticleQuantity())
                        .allPlayers()
                        .spawn();
            } else {
                final org.bukkit.Color bukkitColor = org.bukkit.Color.fromRGB(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue()
                );
                new ParticleBuilder(effect.getParticle())
                        .data(new Particle.DustOptions(bukkitColor, 1))
                        .count(effect.getParticleQuantity())
                        .allPlayers()
                        .spawn();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    default void displayFakeItems(Location location, Rune rune) {
        RuneEffect effect = rune.getEffect();

        try {
            List<XMaterial> materials = effect.getFakeDropItems();

            if (materials == null || materials.isEmpty()) return;

            XMaterial material = RandomUtil.getRandom(
                    materials.stream()
                            .collect(Collectors
                            .toMap(m -> m, m -> RandomUtil.randomBetween(0, 100)))
            );


            spawnFakeItem(location, material);

        } catch (Exception ignored) {
        }
    }

    default void spawnFakeItem(Location location, XMaterial material) {
        ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);

        stand.setSmall(true);
        stand.setVisible(false);
        stand.setGravity(true);
        stand.setCollidable(true);
        stand.setMarker(true);
        stand.setHelmet(material.parseItem());

        FakeEntitySpawnEvent event = new FakeEntitySpawnEvent(stand);

        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            stand.remove();
            return;
        }

        Bukkit.getScheduler().runTaskLater(TheRunes.getApi().getBox().plugin(), stand::remove, 23L);
    }
}
