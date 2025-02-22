package com.qualityplus.dragon.base.service;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.controller.DragonController;
import com.qualityplus.dragon.api.factory.DragonFactory;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.api.game.structure.type.DragonSpawn;
import com.qualityplus.dragon.api.service.DragonService;
import com.qualityplus.dragon.base.configs.DragonsFile;
import com.qualityplus.dragon.base.controller.DragonControllerImpl;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import lombok.Getter;
import org.bukkit.entity.EnderDragon;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public final class DragonServiceImpl implements DragonService {
    private @Getter DragonController dragonController;
    private TheDragonEntity theDragonEntity;
    private @Inject DragonFactory factory;
    private @Inject DragonGame dragonGame;
    private @Inject DragonsFile dragons;

    @Override
    public void selectDragon() {
        this.theDragonEntity = factory.getDragon(dragons.dragonMap);

    }

    @Override
    public void spawnDragon() {
        final Optional<DragonSpawn> spawn = TheDragon.getApi().getStructureService().getSpawn();

        this.theDragonEntity = this.factory.getDragon(this.dragons.dragonMap);

        if (this.theDragonEntity != null && spawn.isPresent()) {
            this.dragonController = new DragonControllerImpl(this.theDragonEntity.spawn(spawn.get().getLocation()));
        }
    }

    @Override
    public CompletableFuture<Void> killDragon() {

        if (this.dragonController != null && this.dragonController.dragon() != null) {
            this.dragonController.setAfk(true);
            this.dragonController.kill();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public TheDragonEntity getActiveDragon() {
        return this.theDragonEntity;
    }

    @Override
    public EnderDragon getActiveEnderDragon() {
        return Optional.ofNullable(this.dragonController)
                .map(DragonController::dragon)
                .orElse(null);
    }
}
