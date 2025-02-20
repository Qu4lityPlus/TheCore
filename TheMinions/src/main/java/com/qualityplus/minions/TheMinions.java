package com.qualityplus.minions;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.addons.WorldManagerAddon;
import com.qualityplus.assistant.lib.eu.okaeri.injector.OkaeriInjector;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.okaeri.OkaeriSilentPlugin;
import com.qualityplus.minions.api.TheMinionsAPI;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.service.MinionsService;
import com.qualityplus.minions.base.config.Messages;
import com.qualityplus.minions.base.gui.main.MainMinionGUI;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.entity.factory.MinionEntityFactory;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.base.minions.entity.tracker.MinionEntityTracker;
import com.qualityplus.minions.listener.chunk.ChunkListenerLegacy;
import com.qualityplus.minions.listener.chunk.ChunkListenerNewest;
import com.qualityplus.minions.persistance.MinionsRepository;
import com.qualityplus.minions.persistance.UserRepository;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.UserData;
import com.qualityplus.minions.util.MinionAnimationUtil;

import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Scan;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.ExecutionPhase;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.Planned;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Scan(deep = true)
public final class TheMinions extends OkaeriSilentPlugin {
    @Inject @Getter
    private static TheMinionsAPI api;
    private static TheMinions INSTANCE;

    public static TheMinions getInstance() {
        return INSTANCE;
    }

    @Planned(ExecutionPhase.POST_STARTUP)
    private void whenStart() {
        INSTANCE = this;

        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(player ->
                Optional.ofNullable(player.getOpenInventory().getTopInventory().getHolder())
                .filter(inventoryHolder -> inventoryHolder instanceof MainMinionGUI)
                .ifPresent(inventoryHolder -> ((MainMinionGUI) inventoryHolder).addItems())
        ),0, 1);
    }

    @Planned(ExecutionPhase.PRE_STARTUP)
    private void preStartup(@Inject("injector") OkaeriInjector injector) {
        if (XMaterial.getVersion() >= 17) {
            Bukkit.getPluginManager().registerEvents(injector.createInstance(ChunkListenerNewest.class), this);
        } else {
            Bukkit.getPluginManager().registerEvents(injector.createInstance(ChunkListenerLegacy.class), this);
        }
    }

    @Planned(ExecutionPhase.PRE_SHUTDOWN)
    private void whenStopSaveUsers(Box box) {
        Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getUniqueId)
                .forEach(uuid -> box.getUserService().getData(uuid).ifPresent(UserData::save));
    }

    @Planned(ExecutionPhase.PRE_SHUTDOWN)
    private void whenStopSaveMinions(final @Inject Logger logger) {
        final AtomicInteger countDown = new AtomicInteger(0);

        for (final MinionEntity minionEntity : MinionEntityTracker.values()) {
            minionEntity.unloadMinion(MinionEntity.DeSpawnReason.SERVER_TURNED_OFF, false);

            countDown.getAndIncrement();
        }

        logger.info(String.format("Plugin has saved %s minions to database!", countDown.get()));
    }

    @Planned(ExecutionPhase.POST_STARTUP)
    private void whenStartFixMessages(final @Inject Messages messages) {
        boolean save = false;
        if (messages.minionMessages.pickUpMinion == null) {
            save = true;
            messages.minionMessages.pickUpMinion = "&aYou picked up a minion! You currently have %minions_placed_amount% out of a maximum of %minions_max_amount_to_place% minions placed.";
        }
        if (messages.minionMessages.youPlacedAMinion == null) {
            save = true;
            messages.minionMessages.youPlacedAMinion = "&bYou placed a minion! (%minions_placed_amount%/%minions_max_amount_to_place%)";
        }
        if (messages.minionMessages.youCanOnlyPlaceAMaxOf == null) {
            save = true;
            messages.minionMessages.youCanOnlyPlaceAMaxOf = "&cYou can only can place a max of %minions_max_amount_to_place% minions!";
        }

        if (save)
            messages.save();
    }



    @Delayed(time = 5)
    private void whenStart(@Inject Logger logger, @Inject MinionsRepository repository, @Inject MinionsService service, @Inject UserRepository userRepository) {
        Collection<MinionData> allData = repository.findAll();

        logger.info(String.format("Plugin has loaded %s minions from database!", allData.size()));

        allData.forEach(data -> {

            if (data.getOwner() == null) {
                userRepository.findAll().stream().filter(userData -> userData.getMinionsPlaced()
                                .contains(data.getUuid()))
                        .findFirst()
                        .ifPresent(user -> data.setOwner(user.getUuid()));
            }

            if (data.getLocation() != null) {

                loadChunk(data.getLocation()).thenRun(() -> {
                    final Minion minion = Minions.getByID(data.getMinionId());

                    if (minion == null) {
                        logger.warning("Failed to load minion " + data.getMinionId());
                        return;
                    }

                    logger.warning("Loaded Minion with id " + data.getMinionId() + " and uuid " + data.getUuid() +  " and owner " + data.getOwner());

                    service.addData(data);

                    final MinionEntity entity = MinionEntityFactory.create(data.getUuid(), data.getOwner(), minion, false);

                    entity.spawnMinion(data.getLocation(), true);
                });
                return;
            }
            service.addData(data);

        });
    }

    private CompletableFuture<Void> loadChunk(final Location spawn) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            if (spawn == null || spawn.getWorld() == null) {
                future.complete(null);
                return future;
            }

            final WorldManagerAddon addon = TheAssistantPlugin.getAPI().getAddons().getWorldManager();
            Bukkit.getScheduler().runTask(this, () -> {
                try {
                    addon.chunksAreLoaded(spawn).thenAccept(response -> {

                        if (!response.isCanBeLoaded()) return;

                        if (!response.isAreLoaded()) {
                            addon.loadChunks(spawn);
                        }

                        List<Vector> vectors =  MinionAnimationUtil.getThree();

                        final Location location = spawn.clone()
                                .subtract(new Vector(0, 1, 0));

                        for (Vector vector : vectors) {
                            Location newLocation = location.clone().add(vector);

                            if (newLocation.getChunk().isLoaded()) continue;

                            newLocation.getChunk().load();
                        }
                        future.complete(null);
                    });
                } catch (Exception e) {
                    future.complete(null);
                }
            });

        } catch (Exception e) {
            future.complete(null);
        }

        return future;
    }
}
