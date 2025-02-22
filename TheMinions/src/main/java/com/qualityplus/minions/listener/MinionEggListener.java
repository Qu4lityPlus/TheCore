package com.qualityplus.minions.listener;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.block.BlockUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.entity.factory.MinionEntityFactory;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.UserData;
import com.qualityplus.minions.util.MinionEggUtil;
import com.qualityplus.minions.util.MinionPlayerUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;

@Component
public final class MinionEggListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void onJoin(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Block block = event.getClickedBlock();

        ItemStack inHand = player.getItemInHand();

        Optional<MinionData> data = MinionEggUtil.dataFromEgg(inHand);

        if (!data.isPresent())
            return;

        event.setCancelled(true);

        if (BlockUtils.isNull(block)) return;

        Minion minion = Minions.getByID(data.get().getMinionId());

        if (minion == null) return;

        if (!canPlaceMinion(player)) return;

        player.setItemInHand(BukkitItemUtil.getItemWithout(inHand, 1));

        MinionEntity petEntity = MinionEntityFactory.create(data.get().getUuid(), player.getUniqueId(), minion, true);

        Location location = block.getLocation().clone()
                .add(0.5,1,0.5);

        Vector direction = location.toVector().subtract(player.getEyeLocation().toVector()).normalize();
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();

        Location changed = location.clone();
        changed.setYaw(180 - toDegree(Math.atan2(x, z)));
        changed.setPitch(90 - toDegree(Math.acos(y)));

        TheMinions.getApi()
                .getUserService()
                .getData(player.getUniqueId())
                .ifPresent(userData -> userData.addMinion(petEntity.getMinionUniqueId()));

        petEntity.spawnMinion(changed, true);
    }

    private boolean canPlaceMinion(Player player) {
        int minionsAmount = MinionPlayerUtil.getMinionsAmount(player);
        int placedAmount = TheMinions.getApi().getUserService().getData(player.getUniqueId()).map(UserData::getMinionsToPlace).orElse(0);

        List<IPlaceholder> placeholders = PlaceholderBuilder.create(
                new Placeholder("minions_max_amount_to_place", minionsAmount),
                new Placeholder("minions_placed_amount", placedAmount + 1)).get();

        if (placedAmount < minionsAmount) {
            player.sendMessage(StringUtils.processMulti(box.files().messages().minionMessages.youPlacedAMinion, placeholders));

            return true;
        } else {
            player.sendMessage(StringUtils.processMulti(box.files().messages().minionMessages.youCanOnlyPlaceAMaxOf, placeholders));
            return false;
        }
    }

    private float toDegree(double angle) {
        return (float) Math.toDegrees(angle);
    }
}