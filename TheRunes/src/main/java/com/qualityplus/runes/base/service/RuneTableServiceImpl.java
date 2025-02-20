package com.qualityplus.runes.base.service;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.runes.api.service.RuneTableService;
import com.qualityplus.runes.base.config.Config;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;


@Component
public final class RuneTableServiceImpl implements RuneTableService {
    private static final double HEAD_ROTATION_ANGLE = -90.10899D;
    private static final int AMOUNT_OF_TIMES = 1;
    private @Inject Config config;

    @Override
    public void createRuneTable(Location location) {
        try {
            location.clone().add(new Vector(0,1,0)).getBlock().setType(config.runeTableConfig.getBaseItem().parseMaterial());

            setCorner(location, new Vector(0.21D, 0.49D, 0.21D));
            setCorner(location, new Vector(0.21D, 0.49D, 0.81D));

            setCorner(location, new Vector(0.8D, 0.49D, 0.21D));
            setCorner(location, new Vector(0.8D, 0.49D, 0.81D));

            setCenter(location, new Vector(0.45D, 1.3D, 0.7D));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setCorner(Location location, Vector toAdd) {
        for (int i = 0; i < AMOUNT_OF_TIMES; i++) {
            Location toSpawn = location.clone().add(toAdd);

            ArmorStand stand = toSpawn.getWorld().spawn(toSpawn, ArmorStand.class);
            stand.setArms(true);
            stand.setGravity(false);
            stand.setVisible(false);
            stand.setCustomName("RunePart");
            stand.setHelmet(config.runeTableConfig.getCornerItem().get());
        }
    }

    private void setCenter(Location location, Vector vector) {
        Location spawnLocation = location.clone();

        spawnLocation.add(new Vector(vector.getX(), vector.getY(), vector.getZ()));
        spawnLocation.setYaw(25.0F);

        ArmorStand stand = spawnLocation.getWorld().spawn(spawnLocation, ArmorStand.class);

        stand.setArms(true);
        stand.setSmall(true);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setCustomName("RunePart");

        stand.setHelmet(config.runeTableConfig.getCenterItem().get());

        EulerAngle newRot = stand.getHeadPose().add(HEAD_ROTATION_ANGLE + 0.589D, 0.0D, 0.0D);

        stand.setHeadPose(newRot);
    }
}
