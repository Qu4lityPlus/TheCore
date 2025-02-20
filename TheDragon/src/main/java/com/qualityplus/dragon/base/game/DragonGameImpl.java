package com.qualityplus.dragon.base.game;

import com.qualityplus.assistant.api.addons.paster.session.PasterSession;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.TheDragonAPI;
import com.qualityplus.dragon.api.exception.InvalidSpawnException;
import com.qualityplus.dragon.api.exception.check.NoSpawnException;
import com.qualityplus.dragon.api.exception.check.NoStructureException;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.game.structure.type.DragonSpawn;
import com.qualityplus.dragon.api.service.StructureService;
import com.qualityplus.dragon.base.game.player.EventPlayer;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.lib.eu.okaeri.tasker.core.Tasker;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Handles the whole Dragon Game
 */

@Component
public final class DragonGameImpl implements DragonGame {
    @Getter
    private boolean active = false;
    private @Inject Logger logger;
    private @Inject Tasker tasker;

    /**
     * Start Dragon Game
     */
    @Override
    public void start() {
        if (!active) {
            TheDragonAPI api = TheDragon.getApi();

            active = true;

            try {
                api.getStructureService()
                        .pasteSchematic()
                        .thenAccept(this::runGame);
            } catch (InvalidSpawnException e) {
                logger.warning("[TheDragon] Arena Spawn not found, cancelling game!");
                active = false;
            } catch (Exception e) {
                e.printStackTrace();
                active = false;
            }
        }
    }

    /**
     * Finish Dragon Game
     */
    @Override
    public void finish() {
        if (active) {
            final TheDragonAPI api = TheDragon.getApi();

            active = false;

            api.getDragonService().killDragon()
                    .thenRun(api.getStructureService()::deSpawnCrystals)
                    .thenRun(api.getUserService()::sendFinishMessage)
                    .thenRun(api.getBossBarService()::stopBossBar)
                    .thenRun(api.getUserService()::resetData)
                    .thenRun(api.getGameService()::stopSwitching)
                    .thenRun(api.getGamePlayerCheckService()::stopChecking);

        }
    }

    @Override
    public boolean canStart() throws NoSpawnException, NoStructureException {
        final StructureService service = TheDragon.getApi().getStructureService();

        final DragonSpawn spawn = service.getSpawn().orElse(null);

        if (spawn == null || spawn.getLocation() == null) {
            throw new NoSpawnException();
        }

        if (!service.schematicExist()) {
            throw new NoStructureException();
        }

        return true;
    }

    @Override
    public List<Player> getPlayers() {
        return getPlayers(player -> true);
    }

    @Override
    public List<Player> getPlayers(Predicate<EventPlayer> filter) {
        final TheDragonAPI api = TheDragon.getApi();

        return api.getUserService()
                .getUsers()
                .stream()
                .filter(filter)
                .map(EventPlayer::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void runGame(final PasterSession session) {
        final TheDragonAPI api = TheDragon.getApi();

        api.getStructureService().clearAltars();
        api.getStructureService().spawnCrystals();
        api.getDragonService().selectDragon();

        api.getGameService()
                .startCountdown()
                .thenRun(() -> api.getGameService().makeBlockExplosion(session)
                .thenRun(this::startGame));

    }

    private void startGame() {
        final TheDragonAPI api = TheDragon.getApi();

        api.getGamePlayerCheckService().startChecking();
        api.getBossBarService().startBossBar();
        api.getDragonService().spawnDragon();
        api.getGameService().switchEvents();
    }
}
