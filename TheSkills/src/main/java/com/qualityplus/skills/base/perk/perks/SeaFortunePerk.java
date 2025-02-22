package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.base.event.EntityFortunePerkEvent;
import com.qualityplus.skills.base.perk.perks.common.AbstractFortunePerk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public final class SeaFortunePerk extends AbstractFortunePerk {
    @Builder
    public SeaFortunePerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel, new ArrayList<>());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(PlayerFishEvent e) {
        Player player = e.getPlayer();

        PlayerFishEvent.State state = e.getState();

        if (e.getCaught() == null || !(e.getCaught() instanceof Item)) return;

        if (state != PlayerFishEvent.State.CAUGHT_FISH && state != PlayerFishEvent.State.CAUGHT_ENTITY) return;

        if (state == PlayerFishEvent.State.CAUGHT_ENTITY) return;

        double level = getStat(player);

        double chance = getChance(level);

        double additional = getMultiplier(level) - 2;

        if (additional <= 0)
            return;

        if (RandomUtil.randomBetween(0.0, 100.0) < chance)
            additional++;

        EntityFortunePerkEvent fortunePerkEvent = new EntityFortunePerkEvent(player, this, e.getCaught(), e.getPlayer().getLocation());

        Bukkit.getPluginManager().callEvent(fortunePerkEvent);

        if (fortunePerkEvent.getToDropEntity().isEmpty() || fortunePerkEvent.isCancelled())
            return;

        int finalAdditional = (int) additional;

        dropVarious(fortunePerkEvent.getToDropLocation(), fortunePerkEvent.getToDropEntity(), finalAdditional);
    }

    private void dropVarious(Location location, Entity entity, int additional) {
        for (int i = 0; i<additional; i++)
            Optional.ofNullable(location.getWorld()).ifPresent(world -> world.dropItem(location, ((Item) entity).getItemStack()));
    }
}
