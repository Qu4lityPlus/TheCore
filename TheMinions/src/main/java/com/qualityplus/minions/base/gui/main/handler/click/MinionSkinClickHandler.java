package com.qualityplus.minions.base.gui.main.handler.click;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.config.Skins;
import com.qualityplus.minions.base.gui.main.handler.ClickHandler;
import com.qualityplus.minions.base.minions.minion.skin.MinionSkin;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.SkinEntity;
import com.qualityplus.minions.util.MinionUpgradeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class MinionSkinClickHandler implements ClickHandler {
    @Override
    public void handle(InventoryClickEvent event, MinionEntity minionEntity, Box box) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (event.isShiftClick()) return;

        Optional<MinionData> data = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());
        SkinEntity entity = data.map(MinionData::getSkinEntity).orElse(null);

        ItemStack cursor = BukkitItemUtil.cloneOrNull(event.getCursor());
        ItemStack oldToGive = getTaken(box, entity, minionEntity);
        boolean cursorIsEntity = MinionUpgradeUtil.isSkin(cursor);

        if (cursorIsEntity) {
            SkinEntity newSkin = getEntityFromItem(box, entity, data, cursor);

            data.ifPresent(d -> d.setSkinEntity(newSkin));

            player.setItemOnCursor(oldToGive);
        } else {
            if (entity == null) return;

            if (BukkitItemUtil.isNull(cursor)) {
                Optional.ofNullable(oldToGive).ifPresent(player::setItemOnCursor);
            } else
                Optional.ofNullable(oldToGive).ifPresent(item -> BukkitItemUtil.dropItem(player, item));

            data.ifPresent(MinionData::removeSkin);
        }

        minionEntity.updateSkin();
    }

    @SuppressWarnings("all")
    private SkinEntity getEntityFromItem(Box box, SkinEntity entity, Optional<MinionData> minionData, @Nullable ItemStack itemStack) {
        SkinEntity modified = MinionUpgradeUtil.getSkinFromItem(itemStack.clone());

        minionData.ifPresent(data -> data.setSkinEntity(modified));

        return modified;
    }

    @SuppressWarnings("all")
    private ItemStack getTaken(Box box, SkinEntity skinEntity, MinionEntity entity) {
        if (skinEntity == null) return null;

        Optional<MinionSkin> minionSkin = Skins.getSkin(skinEntity.getId());

        if (!minionSkin.isPresent()) return null;

        return minionSkin.get().getItemStack();
    }
}
