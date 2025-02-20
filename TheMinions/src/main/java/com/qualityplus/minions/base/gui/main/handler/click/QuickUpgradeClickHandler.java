package com.qualityplus.minions.base.gui.main.handler.click;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.config.Messages;
import com.qualityplus.minions.base.gui.main.handler.ClickHandler;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.level.MatRequirement;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.persistance.data.MinionData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class QuickUpgradeClickHandler implements ClickHandler {
    @Override
    public void handle(InventoryClickEvent event, MinionEntity minionEntity, Box box) {
        Player player = (Player) event.getWhoClicked();

        player.closeInventory();

        Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());

        Messages.MinionMessages messages = box.files().messages().minionMessages;

        int level = minionData.map(MinionData::getLevel).orElse(1);

        Optional<Minion> minion = minionData.map(MinionData::getMinionId)
                .filter(Objects::nonNull)
                .map(Minions::getByID);

        int maxLevel = minion.map(Minion::getMaxLevel).orElse(0);

        if (level >= maxLevel) {
            player.sendMessage(StringUtils.color(messages.upgradeMaxLevelMessage));
            return;
        }

        MatRequirement matRequirement = minion
                .map(minion1 -> minion1.getRequirement(level))
                .orElse(new MatRequirement(XMaterial.BEDROCK, 0));

        ItemStack item = matRequirement.getRequiredMaterial().parseItem();

        int amount = InventoryUtils.getItemQuantity(player.getInventory().getContents(), item);
        int required = matRequirement.getRequiredMaterialAmount();

        if (amount >= required) {
            minionData.ifPresent(MinionData::addOneLevel);

            minionEntity.updateInventory();

            InventoryUtils.removeItems(player.getInventory(), item, required);

            List<IPlaceholder> placeholders = new Placeholder("minion_level_roman", NumberUtil.toRoman(level + 1))
                    .alone();

            player.sendMessage(StringUtils.processMulti(messages.upgradeMessage, placeholders));
        } else {
            List<IPlaceholder> placeholders = PlaceholderBuilder.create(
                    new Placeholder("minion_upgrade_material_amount", required - amount),
                    new Placeholder("minion_upgrade_current_amount", amount),
                    new Placeholder("minion_upgrade_required_amount", required)
            ).get();

            player.sendMessage(StringUtils.processMulti(messages.upgradeNoMaterialsMessage, placeholders));
        }
    }
}
