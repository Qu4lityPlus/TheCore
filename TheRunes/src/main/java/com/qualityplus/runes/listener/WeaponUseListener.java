package com.qualityplus.runes.listener;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.base.event.PlayerKillEvent;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.runes.api.provider.EffectProvider;
import com.qualityplus.runes.api.session.RuneInstance;
import com.qualityplus.runes.util.RunesUtils;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

@Component
public final class WeaponUseListener implements Listener {
    private @Inject EffectProvider effectProvider;

    @EventHandler
    public void onUseBow(PlayerKillEvent e) {
        Player player = e.getPlayer();

        ItemStack inHand = player.getItemInHand();

        if (BukkitItemUtil.isNull(inHand)) return;

        String mat = inHand.getType().toString();

        if (!mat.contains("SWORD"))  return;

        RuneInstance runeInstance = RunesUtils.getRuneItemInstance(inHand);

        if (runeInstance.getRune() == null) return;

        effectProvider.execute(player, e.getKilled(), runeInstance.getRune());
    }

    @EventHandler
    public void onUseBow(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();

        ItemStack inHand = player.getItemInHand();

        if (BukkitItemUtil.isNull(inHand)) return;

        RuneInstance runeInstance = RunesUtils.getRuneItemInstance(inHand);

        if (runeInstance.getRune() == null) return;

        effectProvider.execute(player, e.getProjectile(), runeInstance.getRune());
    }
}
