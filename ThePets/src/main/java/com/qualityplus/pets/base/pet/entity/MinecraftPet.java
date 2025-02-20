package com.qualityplus.pets.base.pet.entity;

import com.destroystokyo.paper.ParticleBuilder;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.api.util.MathUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.pets.ThePets;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.tracker.PetEntityTracker;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.pets.persistance.data.UserData;
import com.qualityplus.pets.util.PetPlaceholderUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MinecraftPet implements PetEntity {
    protected @Getter final UUID petUniqueId;
    protected @Getter final UUID owner;

    protected final Pet pet;
    protected int count = 0;

    @Override
    public void spawn() {
        PetEntityTracker.registerNewEntity(this);

        handleSpawn();

        addPotions();

        addStats();
    }

    @Override
    public void deSpawn(DeSpawnReason deSpawnReason) {
        PetEntityTracker.unregisterEntity(this);

        handleDeSpawnReason(deSpawnReason);

        removePotions();

        removeStats();
    }

    @Override
    public void spellParticle() {
        String particle = pet.getPetEntityOptions().getParticle();

        if (particle == null) return;

        try {
            Particle particleEffect = Particle.valueOf(particle);
            Optional.ofNullable(getNextLocation())
                    .map(location -> location.clone().add(new Vector(0, 0.6, 0)))
                    .ifPresent(location -> new ParticleBuilder(particleEffect)
                            .location(location)
                            .allPlayers()
                            .spawn());
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.color("&c[ThePets] Invalid Particle " + particle));
        }


    }

    @Override
    public void update() {
        addPotions();
        addStats();
    }


    private void handleSpawn() {
        OfflinePlayer offPlayer = getOffPlayer();

        Optional<UserData> data = ThePets.getApi().getUsersService().getData(offPlayer.getUniqueId());

        data.flatMap(userData -> Optional.ofNullable(userData.getSpawnedData())).ifPresent(spawnedData -> {
            spawnedData.setSpawnedPetUUID(petUniqueId);
            spawnedData.setSpawnedPetId(pet.getId());
        });
    }


    protected void handleDeSpawnReason(DeSpawnReason deSpawnReason) {
        OfflinePlayer offPlayer = getOffPlayer();

        Optional<UserData> userData = ThePets.getApi().getUsersService().getData(offPlayer.getUniqueId());

        if (!deSpawnReason.equals(DeSpawnReason.PLAYER_DE_SPAWN_PET)) return;

        userData.ifPresent(data -> data.getSpawnedData().setSpawnedPetUUID(null));
        userData.ifPresent(data -> data.getSpawnedData().setSpawnedPetId(null));
    }

    private OfflinePlayer getOffPlayer() {
        return Bukkit.getOfflinePlayer(owner);
    }


    private Player getPlayer() {
        return Bukkit.getPlayer(owner);
    }

    protected Location getNextLocation() {
        Player player = getPlayer();

        if (player == null || !player.isOnline()) return null;


        Vector vector = player.getEyeLocation().getDirection().clone().normalize().multiply(-1);

        vector.setY(Math.abs(vector.getY()));

        double x = Math.abs(vector.getX()) < 0.5 ? 0.5 : vector.getX();
        double z = Math.abs(vector.getZ()) < 0.5 ? 0.5 : vector.getZ();

        vector.setX(x);
        vector.setZ(z);

        Location location = player.getEyeLocation().clone().add(rotateAroundY(vector, Math.PI / 6));

        location.setY(location.getY() + MathUtil.sinCalc(count / (2 * Math.PI) * 0.5) * 0.15);

        count++;

        return location;
    }

    private Vector rotateAroundY(final Vector vector, final double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * vector.getX() + angleSin * vector.getZ();
        double z = -angleSin * vector.getX() + angleCos * vector.getZ();
        return vector.setX(x).setZ(z);
    }


    protected String getDisplayName() {
        String displayName = pet.getPetEntityOptions().getDisplayName();

        List<IPlaceholder> placeholderList = PetPlaceholderUtil.getPetPlaceholders(petUniqueId)
                .with(PetPlaceholderUtil.getPetPlaceholders(pet))
                .with(new Placeholder("player", Bukkit.getOfflinePlayer(owner).getName())).get();

        return StringUtils.processMulti(displayName, placeholderList);
    }

    protected void addPotions() {
        Player player = getPlayer();

        if (player == null || !player.isOnline()) return;

        pet.getPetPotions(getLevel())
                .forEach(petPotion -> petPotion.addPotion(player));

    }

    protected void removePotions() {
        Player player = getPlayer();

        if (player == null || !player.isOnline()) return;

        pet.getPetPotions(getLevel())
                .forEach(petPotion -> petPotion.removePotion(player));

    }

    protected void addStats() {
        Player player = getPlayer();

        if (player == null || !player.isOnline()) return;

        pet.getStatRewards(getLevel())
                .forEach(petPotion -> petPotion.addStat(player));
    }

    protected void removeStats() {
        Player player = getPlayer();

        if (player == null || !player.isOnline()) return;

        pet.getStatRewards(getLevel())
                .forEach(petPotion -> petPotion.removeStat(player));
    }

    private int getLevel() {
        Optional<PetData> petData = ThePets.getApi().getPetsService().getData(petUniqueId);
        return petData.map(PetData::getLevel).orElse(1);
    }
}
