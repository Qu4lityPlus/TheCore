package com.qualityplus.souls.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.qualityplus.assistant.base.event.EntityDamagedByPlayerEvent;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.souls.api.edition.SoulEdition;
import com.qualityplus.souls.base.gui.editgui.SoulsEditGUI;
import com.qualityplus.souls.base.soul.Soul;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public final class SoulsDeleteListener implements Listener {
    private final Cache<UUID, String> clickCache = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build();
    private @Inject SoulEdition edition;
    private @Inject Box box;

    @EventHandler
    public void onPlace(EntityDamagedByPlayerEvent e) {
        Player player = e.getPlayer();

        if (!player.isOp() && !player.hasPermission("souls.delete")) return;

        if (!(e.getEntity() instanceof ArmorStand)) return;

        ArmorStand armorStand = (ArmorStand) e.getEntity();

        if (!armorStand.hasMetadata("soulData")) return;

        Optional<Soul> soul = box.files().souls().getByLocation(armorStand.getLocation());

        if (!soul.isPresent()) return;

        e.setCancelled(true);

        String str = clickCache.getIfPresent(player.getUniqueId());

        if (str == null) {
            clickCache.put(player.getUniqueId(), "");
            return;
        }

        player.openInventory(new SoulsEditGUI(box, soul.get(), edition).getInventory());
    }
}
