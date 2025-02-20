package com.qualityplus.dragon.base.controller;

import com.qualityplus.assistant.api.util.MathUtil;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.controller.DragonController;
import com.qualityplus.dragon.api.game.structure.type.DragonSpawn;
import com.qualityplus.dragon.base.event.DragonTargetEvent;
import com.qualityplus.dragon.util.DragonVelocityUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Optional;

public final class DragonControllerImpl implements DragonController {
    private final EnderDragon dragon;
    private Entity entTarget = null;
    private Location target = null;
    private Location location = null;
    @Getter @Setter
    private boolean afk = false;
    private float pitch = 0.0F;
    private Vector velocity = new Vector(0, 0, 0);
    private double rangeBest = 1000.0D;
    private long rangeTime = 0L;

    public DragonControllerImpl(EnderDragon paramEnderDragon) {
        this.dragon = paramEnderDragon;
        this.velocity = paramEnderDragon.getLocation().getDirection().setY(0).normalize();
        this.pitch = DragonVelocityUtil.getPitch(paramEnderDragon.getLocation().getDirection());
        this.location = paramEnderDragon.getLocation();
    }

    @Override
    public void move() {
        turn();
        this.location.add(this.velocity);
        this.location.add(0.0D, -this.pitch, 0.0D);
        this.location.setPitch(-1.0F * this.pitch);
        this.location.setYaw(180.0F + DragonVelocityUtil.getYaw(this.velocity));
        this.dragon.teleport(this.location);
    }

    private void turn() {
        float f = DragonVelocityUtil.getPitch(DragonVelocityUtil.getTrajectory(this.location, this.target));
        if (f < this.pitch)
            this.pitch = (float)(this.pitch - 0.05D);
        if (f > this.pitch)
            this.pitch = (float)(this.pitch + 0.05D);
        if (this.pitch > 0.5D)
            this.pitch = 0.5F;
        if (this.pitch < -0.5D)
            this.pitch = -0.5F;
        Vector vector = DragonVelocityUtil.getTrajectory2d(this.location, this.target);
        vector.subtract(DragonVelocityUtil.normalize(new Vector(this.velocity.getX(), 0.0D, this.velocity.getZ())));
        vector.multiply(0.075D);
        this.velocity.add(vector);
        DragonVelocityUtil.normalize(this.velocity);
    }

    @Override
    public void target() {
        if (this.entTarget != null) {
            if (!this.entTarget.isValid()) {
                this.entTarget = null;
            } else {
                this.target = this.entTarget.getLocation().subtract(0.0D, 8.0D, 0.0D);
            }
            return;
        }
        if (this.target == null)
            targetSky();
        if (MathUtil.offset(this.location, this.target) < 4.0D) {
            Optional<DragonSpawn> spawn = TheDragon.getApi().getStructureService().getSpawn();
            if (!spawn.isPresent()) return;
            if (this.target.getY() >= spawn.get().getLocation().getY()) {
                targetPlayer();
            } else {
                targetSky();
            }
        }
        targetTimeout();
    }

    private void targetTimeout() {
        if (MathUtil.offset(this.location, this.target) + 1.0D < this.rangeBest) {
            this.rangeTime = System.currentTimeMillis();
            this.rangeBest = MathUtil.offset(this.location, this.target);
        } else if (elapsed(this.rangeTime, 10000L)) {
            targetSky();
        }
    }

    @Override
    public void targetSky() {
        Optional<DragonSpawn> spawn = TheDragon.getApi().getStructureService().getSpawn();
        if (!spawn.isPresent()) return;
        this.rangeBest = 9000.0D;
        this.rangeTime = System.currentTimeMillis();
        this.target = spawn.get().getLocation().clone().add((50 - RandomUtil.randomUpTo(100)), (20 + RandomUtil.randomUpTo(30)), (50 - RandomUtil.randomUpTo(100)));
        TheDragon.getApi().getPlugin().getServer().getPluginManager().callEvent(new DragonTargetEvent(null, this, TargetType.SKY));
    }

    @Override
    public void targetPlayer() {
        this.rangeBest = 9000.0D;
        this.rangeTime = System.currentTimeMillis();
        final int randomIndex = Math.max(RandomUtil.randomUpTo(TheDragon.getApi().getUserService().getUsers().size() - 1), 0);
        final Player player = TheDragon.getApi().getUserService().getUsers().get(randomIndex).getPlayer();
        this.target = player.getLocation();
        this.target.add(DragonVelocityUtil.getTrajectory(this.location, this.target).multiply(3.5D));
        TheDragon.getApi().getPlugin().getServer().getPluginManager().callEvent(new DragonTargetEvent(player, this, TargetType.PLAYER));
    }

    @Override
    public void targetPlayer(Player player) {
        this.rangeBest = 9000.0D;
        this.rangeTime = System.currentTimeMillis();
        this.target = player.getLocation();
        this.target.add(DragonVelocityUtil.getTrajectory(this.location, this.target).multiply(3.5D));
        TheDragon.getApi().getPlugin().getServer().getPluginManager().callEvent(new DragonTargetEvent(player, this, TargetType.PLAYER));
    }

    @Override
    public void teleport(Location location) {
        Optional.ofNullable(dragon).ifPresent(dr -> dr.teleport(location));;
    }

    @Override
    public void kill() {
        dragon.setHealth(0);
    }

    @Override
    public EnderDragon dragon() {
        return dragon;
    }

    private static boolean elapsed(long paramLong1, long paramLong2) {
        return (System.currentTimeMillis() - paramLong1 > paramLong2);
    }
}