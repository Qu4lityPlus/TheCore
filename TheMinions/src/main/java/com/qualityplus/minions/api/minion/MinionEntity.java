package com.qualityplus.minions.api.minion;

import com.qualityplus.assistant.hologram.TheHologram;
import com.qualityplus.minions.base.minions.entity.getter.DataGetter;
import com.qualityplus.minions.base.minions.entity.getter.LevelGetter;
import com.qualityplus.minions.base.minions.entity.getter.MinionItemsGetter;
import com.qualityplus.minions.base.minions.entity.state.MinionState;
import com.qualityplus.minions.base.minions.minion.Minion;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface MinionEntity extends LevelGetter, MinionItemsGetter, DataGetter {
    public void spawnMinionEntity();
    public void unloadMinionEntity();

    public MinionState getState();
    public void tick();

    public Collection<ItemStack> pickUpAllItems();

    public void spawnMinion(final Location location, boolean load);
    public void unloadMinion(final DeSpawnReason reason, final boolean saveDBAsync);

    public void updateSkin();
    public void updateInventory();

    public ArmorStand getEntity();
    public TheHologram getHologram();
    public void setHologram(final TheHologram hologram);
    public void setEntity(final ArmorStand entity);

    public Minion getMinion();

    public enum DeSpawnReason{
        SERVER_TURNED_OFF,
        PLAYER_DE_SPAWN_MINION
    }
}
