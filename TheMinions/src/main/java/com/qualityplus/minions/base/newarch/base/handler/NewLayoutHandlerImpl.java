package com.qualityplus.minions.base.newarch.base.handler;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.block.BlockUtils;
import com.qualityplus.minions.api.TheMinionsAPI;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.layout.LayoutType;
import com.qualityplus.minions.base.minions.minion.layout.MinionLayout;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionUpgrade;
import com.qualityplus.minions.base.newarch.api.entity.NewMinionEntity;
import com.qualityplus.minions.base.newarch.api.handler.NewLayoutHandler;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.UpgradeEntity;
import com.qualityplus.minions.util.MinionAnimationUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class NewLayoutHandlerImpl implements NewLayoutHandler {
    private TheMinionsAPI api;

    @Override
    public boolean hasValidLayout(final NewMinionEntity entity) {
        final Minion minion = entity.getConfig();

        final MinionLayout minionLayout = minion.getMinionLayout();

        final List<XMaterial> noException = minionLayout.getAllMaterialsCauseExceptionExcept();
        final List<XMaterial> exceptions = minionLayout.getMaterialThatCauseException();

        final List<Vector> vectors = getMinionLayout(entity).equals(LayoutType.THREE_X_THREE) ? MinionAnimationUtil.getThree() : MinionAnimationUtil.getSecond();

        final Location location = Optional.ofNullable(entity.getEntity())
                .filter(Objects::nonNull)
                .map(ArmorStand::getLocation)
                .filter(Objects::nonNull)
                .map(Location::clone)
                .map(e -> e.subtract(new Vector(0, 1, 0)))
                .orElse(null);

        if (location == null) {
            return true;
        }

        for (final Vector vector : vectors) {
            final Location newLocation = location.clone().add(vector);

            final Block block = newLocation.getBlock();

            if (BlockUtils.isNull(block)) {
                continue;
            }

            final XMaterial material = XMaterial.matchXMaterial(block.getType());

            if (exceptions.contains(material)) {
                return false;
            }

            if (noException.contains(material)) {
                continue;
            }

            return false;
        }

        return true;
    }

    private LayoutType getMinionLayout(final NewMinionEntity entity) {
        final Minion minion = entity.getConfig();

        if (minion.isLayoutType(LayoutType.THREE_X_THREE)) {
            return LayoutType.THREE_X_THREE;
        }

        Optional<MinionData> minionData = this.api.getMinionsService().getData(entity.getId());

        boolean firstUpgrade = minionData.map(data -> hasMinionExpander(data.getFirstUpgrade())).orElse(false);
        boolean secondUpgrade = minionData.map(data -> hasMinionExpander(data.getSecondUpgrade())).orElse(false);

        if (firstUpgrade || secondUpgrade) return LayoutType.THREE_X_THREE;

        return LayoutType.TWO_X_TWO;
    }

    private boolean hasMinionExpander(final UpgradeEntity entity) {
        if (entity == null) {
            return false;
        }

        final MinionUpgrade upgrade = this.api.getConfigFiles().upgrades().normalUpgrades.getOrDefault(entity.getId(), null);

        if (upgrade == null) {
            return false;
        }

        return upgrade.isExpandsOneBlock();
    }
}
