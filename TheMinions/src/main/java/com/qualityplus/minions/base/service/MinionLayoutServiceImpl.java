package com.qualityplus.minions.base.service;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.util.armorstand.ArmorStandUtil;
import com.qualityplus.assistant.util.block.BlockUtils;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.handler.ArmorStandHandler;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.service.MinionLayoutService;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.layout.LayoutType;
import com.qualityplus.minions.base.minions.minion.layout.MinionLayout;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionUpgrade;
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

@Component
public class MinionLayoutServiceImpl implements MinionLayoutService {
    @Override
    public LayoutType getMinionLayout(final MinionEntity entity) {
        final Minion minion = entity.getMinion();
        final LayoutType layoutType = minion.getMinionLayout().getType();

        final Optional<MinionData> data = TheMinions.getApi().getMinionsService().getData(entity.getMinionUniqueId());
        if (data.isEmpty()) {
            return layoutType;
        }

        final boolean hasExpander = hasMinionExpander(data.get().getFirstUpgrade()) || hasMinionExpander(data.get().getSecondUpgrade());
        if (hasExpander) {
            return LayoutType.THREE_X_THREE;
        }

        return layoutType;
    }

    @Override
    public boolean hasInvalidLayout(final MinionEntity entity) {
        final Minion minion = entity.getMinion();
        final MinionLayout minionLayout = minion.getMinionLayout();

        final List<XMaterial> noException = minionLayout.getAllMaterialsCauseExceptionExcept();
        final List<XMaterial> exceptions = minionLayout.getMaterialThatCauseException();
        final LayoutType layoutType = minion.getMinionLayout().getType();
        final List<Vector> vectors = layoutType.equals(LayoutType.THREE_X_THREE) ? MinionAnimationUtil.getThree() : MinionAnimationUtil.getSecond();

        final Location location = Optional.ofNullable(entity.getEntity())
                .filter(ArmorStandUtil::entityIsValid)
                .map(ArmorStand::getLocation)
                .map(e -> e.subtract(new Vector(0, 1, 0)))
                .orElse(null);

        if (location == null) {
            return false;
        }

        for (final Vector vector : vectors) {
            if (!entity.getState().isArmorStandLoaded()) {
                return false;
            }

            final Location newLocation = location.clone().add(vector);
            final Block block = newLocation.getBlock();
            if (BlockUtils.isNull(block)) {
                continue;
            }

            final XMaterial material = XMaterial.matchXMaterial(block.getType());
            if (exceptions.contains(material)) {
                return true;
            }

            if (noException.contains(material)) {
                continue;
            }

            return true;
        }

        return false;
    }

    private boolean hasMinionExpander(final UpgradeEntity entity) {
        if (entity == null) {
            return false;
        }

        final MinionUpgrade upgrade = TheMinions.getApi().getConfigFiles().upgrades().normalUpgrades.getOrDefault(entity.getId(), null);
        if (upgrade == null) {
            return false;
        }

        return upgrade.isExpandsOneBlock();
    }
}
