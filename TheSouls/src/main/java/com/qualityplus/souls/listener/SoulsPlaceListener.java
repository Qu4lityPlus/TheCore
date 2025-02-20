package com.qualityplus.souls.listener;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.location.ALocation;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.souls.base.soul.Soul;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

@Component
public final class SoulsPlaceListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void onPlace(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Player player = e.getPlayer();

        ItemStack inHand = player.getItemInHand();

        if (BukkitItemUtil.isNull(inHand)) return;

        if (!isSoul(inHand)) return;

        e.setCancelled(true);

        Block block = e.getClickedBlock();

        spawnSoul(player, block);

    }

    private void spawnSoul(Player player, @Nullable Block block) {
        if (block == null) return;

        Location location = block.getLocation().clone().subtract(0,0.43,0).add(0.5,0,0.5);

        if (box.files().souls().getByLocation(location).isPresent()) {
            player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.soulAlreadyPlaced));
            return;
        }

        Soul soul = Soul.builder()
                .uuid(UUID.randomUUID())
                .messages(new ArrayList<>())
                .commands(new ArrayList<>())
                .location(new ALocation(location))
                .build();

        soul.enable(box, false);

        box.files().souls().soulList.add(soul);

        player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.soulPlaced.replace("%souls_total%", String.valueOf(box.files().souls().soulList.size()))));
    }

    private boolean isSoul(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);

        return nbtItem.hasKey("soulItem");
    }
}
