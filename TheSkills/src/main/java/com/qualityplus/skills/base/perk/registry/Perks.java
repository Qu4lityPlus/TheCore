package com.qualityplus.skills.base.perk.registry;

import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.api.effect.CommonObject;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public final class Perks {
    private static final Map<String, Perk> PERK_REGISTRY = new HashMap<>();

    @ApiStatus.Internal
    public static void registerNewPerk(@NotNull final Perk perk) {
        PERK_REGISTRY.put(perk.getId().toLowerCase(), perk);
    }

    @Nullable
    public static Perk getByID(@NotNull final String id) {
        return PERK_REGISTRY.get(id.toLowerCase());
    }

    @Nullable
    public static Perk getByKey(@NotNull final NamespacedKey key) {
        return PERK_REGISTRY.get(key.getKey());
    }

    public static Set<Perk> values() {
        return ImmutableSet.copyOf(PERK_REGISTRY.values());
    }

    public static Set<Perk> values(Predicate<Perk> filter) {
        return ImmutableSet.copyOf(PERK_REGISTRY.values().stream().filter(filter).collect(Collectors.toList()));
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND, async = true)
    public static void reloadPerks(@Inject Box box) {
        values().forEach(Perk::unregisterListeners);

        Stream.of(box.perkFiles().abilityDamage().getPerk(),
                        box.perkFiles().bonusAttack().getPerk(),
                        box.perkFiles().cactusSkin().getPerk(),
                        box.perkFiles().eagleEyes().getPerk(),
                        box.perkFiles().farmingFortune().getPerk(),
                        box.perkFiles().foragingFortune().getPerk(),
                        box.perkFiles().ironLungs().getPerk(),
                        box.perkFiles().leavesMaster().getPerk(),
                        box.perkFiles().lightningPunch().getPerk(),
                        box.perkFiles().medicineMan().getPerk(),
                        box.perkFiles().miningFortune().getPerk(),
                        box.perkFiles().miningSpeed().getPerk(),
                        box.perkFiles().onePunchMan().getPerk(),
                        box.perkFiles().projectileMaster().getPerk(),
                        box.perkFiles().refurbished().getPerk(),
                        box.perkFiles().spiderman().getPerk(),
                        box.perkFiles().steelSkin().getPerk(),
                        box.perkFiles().wizard().getPerk(),
                        box.perkFiles().seaFortune().getPerk(),
                        box.perkFiles().potionMaster().getPerk(),
                        box.perkFiles().brewChance().getPerk(),
                        box.perkFiles().orbMaster().getPerk())
                .filter(CommonObject::isEnabled)
                .forEach(perk -> {
                    perk.register();
                    perk.registerListeners(box);
                });

        box.log().info("Registered " + values().size() + " Perks!");

    }
}
