package com.qualityplus.dragon.api.event;

import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.controller.DragonController;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.game.structure.type.DragonSpawn;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;

/**
 * Implementation for Dragon Events
 */
public abstract class DragonGameEvent {
    protected int time = 0;
    protected final int duration;
    protected final int repeat;
    protected final double dragonSpeed;
    protected final boolean afk;
    protected BukkitTask task;

    /**
     * Default Constructor
     *
     * @param duration    Event Duration
     * @param repeat      Each every seconds must be repited
     * @param dragonSpeed Dragon Speed during event
     * @param afk      If Dragon must be Freezed during event
     */
    public DragonGameEvent(int duration, int repeat, double dragonSpeed, boolean afk) {
        this.duration = duration;
        this.repeat = repeat;
        this.dragonSpeed = dragonSpeed;
        this.afk = afk;
    }

    /**
     * Start Event
     *
     * @param dragonGame The Dragon Game where event is happening
     */
    public abstract void start(DragonGame dragonGame);

    /**
     * Finish Event
     */
    public void finish() {
        Optional.ofNullable(task).ifPresent(BukkitTask::cancel);
    }

    /**
     *
     * @return Event's remaining time
     */
    public int getRemainingTime() {
        return Math.abs(duration - time);
    }

    /**
     *
     * @return If Event is active
     */
    public boolean isActive() {
        return task != null && !task.isCancelled();
    }

    /**
     * Manage Dragon movement during game
     *
     * @param dragonGame The Dragon Game where event is happening
     */
    protected final void move(DragonGame dragonGame) {
        if (!dragonGame.isActive()) return;

        DragonController controller = TheDragon.getApi().getDragonService().getDragonController();

        if (controller == null || controller.dragon() == null) return;

        if (afk)
            controller.setAfk(false);
        else
            TheDragon.getApi()
                    .getStructureService()
                    .getSpawn()
                    .ifPresent(this::setAfk);
    }

    private void setAfk(DragonSpawn dragonSpawn) {
        DragonController controller = TheDragon.getApi().getDragonService().getDragonController();

        Bukkit.getScheduler().runTask(TheDragon.getApi().getPlugin(), () -> {
            controller.teleport(dragonSpawn.getLocation());
            controller.setAfk(true);
        });
    }
}
