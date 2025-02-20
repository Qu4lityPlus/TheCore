package com.qualityplus.minions.base.gui.main.setup.placeholders;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.main.setup.PlaceholdersSetup;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.level.MatRequirement;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.persistance.data.MinionData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class UpgradePlaceholdersSetup implements PlaceholdersSetup {
    @Override
    public List<IPlaceholder> getPlaceholders(Box box, MinionEntity minionEntity) {
        TemporalData data = new TemporalData(minionEntity);

        return PlaceholderBuilder.create(
                new Placeholder("minion_can_upgrade", getCanUpgrade(data, box, minionEntity)),
                new Placeholder("minion_upgrade_status", getUpgradeStatus(data, box))).get();
    }

    private List<String> getUpgradeStatus(TemporalData temporalData, Box box) {
        int level = temporalData.getLevel();

        Optional<Minion> minion = temporalData.getMinion();

        if (level >= temporalData.getMaxLevel())
            return box.files().messages().minionMessages.upgradeMaxLevelPlaceholder;

        int currentTime = minion.map(minion1 -> minion1.getTimerSeconds(level)).orElse(-1);
        int nextTime = minion.map(minion1 -> minion1.getTimerSeconds(level + 1)).orElse(-1);

        String betweenActionUpgrade = currentTime == nextTime ?
                box.files().messages().minionMessages.timeBetweenWithoutDifferencePlaceholder :
                box.files().messages().minionMessages.timeBetweenWithDifferencePlaceholder;

        int currentStorage = minion.map(minion1 -> minion1.getMaxStorageForInv(level)).orElse(-1);
        int nextStorage = minion.map(minion1 -> minion1.getMaxStorageForInv(level + 1)).orElse(-1);

        String storageUpgrade = currentStorage == nextStorage ?
                box.files().messages().minionMessages.maxStorageWithoutDifferencePlaceholder :
                box.files().messages().minionMessages.maxStorageWithDifferencePlaceholder;

        List<IPlaceholder> toParse = PlaceholderBuilder.create(
                new Placeholder("minion_max_storage", currentStorage),
                new Placeholder("minion_next_level_max_storage", nextStorage),
                new Placeholder("minion_next_level_time_between_actions", nextTime),
                new Placeholder("minion_time_between_actions", currentTime)

        ).get();

        List<IPlaceholder> placeholders = PlaceholderBuilder.create(
                new Placeholder("minion_time_between_actions_upgrade", StringUtils.processMulti(betweenActionUpgrade, toParse)),
                new Placeholder("minion_max_storage_upgrade", StringUtils.processMulti(storageUpgrade, toParse))
        ).get();

        return StringUtils.processMulti(box.files().messages().minionMessages.upgradeStatusPlaceholder, placeholders);
    }

    private String getCanUpgrade(TemporalData temporalData, Box box, MinionEntity minionEntity) {
        if (temporalData.getLevel() >= temporalData.getMaxLevel())
            return box.files().messages().minionMessages.canUpgradeMaxLevel;

        MatRequirement matRequirement = temporalData.getMinion()
                .map(minion1 -> minion1.getRequirement(temporalData.getLevel()))
                .orElse(new MatRequirement(XMaterial.BEDROCK, 0));

        Player player = Bukkit.getPlayer(minionEntity.getState().getOwner());

        if (player == null) return "";

        ItemStack item = matRequirement.getRequiredMaterial().parseItem();

        int amount = InventoryUtils.getItemQuantity(player.getInventory().getContents(), item);
        int required = matRequirement.getRequiredMaterialAmount();

        if (amount >= required) {
            return box.files().messages().minionMessages.canUpgrade;
        } else {
            List<IPlaceholder> placeholders = PlaceholderBuilder.create(
                    new Placeholder("minion_upgrade_material_amount", required - amount),
                    new Placeholder("minion_upgrade_material_name", BukkitItemUtil.getName(item))
            ).get();
            return StringUtils.processMulti(box.files().messages().minionMessages.canUpgradeNoMaterials, placeholders);
        }
    }

    @Getter
    @SuppressWarnings("all")
    private static class TemporalData{
        private final Optional<Minion> minion;
        private final int maxLevel;
        private final int level;

        public TemporalData(MinionEntity minionEntity) {
            Optional<MinionData> minionData = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());

            this.level = minionData.map(MinionData::getLevel).orElse(1);

            this.minion = minionData.map(MinionData::getMinionId)
                    .filter(Objects::nonNull)
                    .map(Minions::getByID);

            this.maxLevel = minion.map(Minion::getMaxLevel).orElse(0);
        }
    }
}
