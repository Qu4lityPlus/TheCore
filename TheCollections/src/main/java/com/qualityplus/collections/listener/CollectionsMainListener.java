package com.qualityplus.collections.listener;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.collections.TheCollections;
import com.qualityplus.collections.api.service.AntiExploitService;
import com.qualityplus.collections.api.service.CollectionsService;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.collection.registry.CollectionsRegistry;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.collections.base.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Optional;

@Component
public final class CollectionsMainListener implements Listener {
    private @Inject AntiExploitService antiExploitService;
    private @Inject CollectionsService collectionsService;
    private @Inject Config config;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onExtractFromFurnace(FurnaceExtractEvent e) {
        XMaterial pickUpMaterial = XMaterial.matchXMaterial(e.getItemType());

        Optional<Collection> collection = CollectionsRegistry.getByItem(pickUpMaterial.parseItem());

        if (!collection.isPresent()) return;

        collectionsService.addXp(e.getPlayer(), collection.get(), e.getItemAmount());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPickUpMaterial(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();

        ItemStack pickUpItem = e.getItem().getItemStack();

        if (antiExploitService.hasMetadata(e.getItem())) return;

        if (isOnTimer(player, e)) return;

        //XMaterial pickUpMaterial = XMaterial.matchXMaterial(pickUpItem);

        Optional<Collection> collection = CollectionsRegistry.getByItem(pickUpItem);

        if (!collection.isPresent()) return;

        antiExploitService.setPlayerTimer(player, -1);

        collectionsService.addXp(player, collection.get(), pickUpItem.getAmount());
    }

    @EventHandler(ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent e) {
        antiExploitService.addMetadata(e.getItemDrop());
    }


    @EventHandler(ignoreCancelled = true)
    public void oPlaceBlock(BlockPlaceEvent e) {
        antiExploitService.addMetadata(e.getBlock());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreakInventoryHolders(BlockBreakEvent e) {
        Player player = e.getPlayer();

        final Block block = e.getBlock();
        if (block.getBlockData() instanceof Ageable ageable && ageable.getAge() != ageable.getMaximumAge()) {
            addMetadataToNearEntities(player.getLocation(), 5);
        } else if (antiExploitService.hasMetadata(block)) {
            addMetadataToNearEntities(player.getLocation(), 5);
        }

        if (block.getState() instanceof org.bukkit.inventory.InventoryHolder) {
            addMetadataToNearEntities(player.getLocation(), 5);
            antiExploitService.setPlayerTimer(player, System.currentTimeMillis() + 140L);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(final EntityExplodeEvent e) {
        if (this.config.allowXPFromTNTExplosions) {
            return;
        }

        for (final Block block : e.blockList()) {
            if (block.getBlockData() instanceof Ageable ageable && ageable.getAge() == ageable.getMaximumAge()) {
                addMetadataToNearEntities(block.getLocation(), 5);
            } else if (antiExploitService.hasMetadata(block)) {
                addMetadataToNearEntities(block.getLocation(), 5);
            }

            if (block.getState() instanceof org.bukkit.inventory.InventoryHolder) {
                addMetadataToNearEntities(block.getLocation(), 5);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDispenseEvent(BlockDispenseEvent e) {
        addMetadataToNearPlayers(e.getBlock().getLocation(), 3);
        addMetadataToNearEntities(e.getBlock().getLocation(), 3);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent e) {
        addMetadataToNearPlayers(e.getBlock().getLocation(), 3);
        addMetadataToNearEntities(e.getBlock().getLocation(), 3);
    }

    private void addMetadataToNearPlayers(Location location, int radius) {
        Optional.ofNullable(location.getWorld())
                .ifPresent(world -> world.getNearbyEntities(location, radius,radius,radius)
                        .stream()
                        .filter(entity -> entity instanceof Player)
                        .map(entity -> (Player) entity)
                        .forEach(player -> antiExploitService.setPlayerTimer(player, System.currentTimeMillis() + 150L)));
    }

    private void addMetadataToNearEntities(Location location, int radius) {
        Bukkit.getScheduler().runTaskLater(TheCollections.getApi().getPlugin(), () -> Optional.ofNullable(location.getWorld())
                .ifPresent(world -> world.getNearbyEntities(location, radius,radius,radius)
                .stream()
                .filter(entity -> entity instanceof Item)
                .map(entity -> (Item) entity)
                .forEach(antiExploitService::addMetadata)), 1);
    }

    private boolean isOnTimer(Player player, Cancellable cancellable) {
        if (System.currentTimeMillis() < antiExploitService.getPlayerTimer(player)) {
            cancellable.setCancelled(true);
            return true;
        } else
            return false;
    }
}
