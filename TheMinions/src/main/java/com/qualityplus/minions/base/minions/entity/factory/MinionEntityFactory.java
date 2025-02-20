package com.qualityplus.minions.base.minions.entity.factory;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.entity.type.CropBreakMinion;
import com.qualityplus.minions.base.minions.entity.type.MobKillerMinion;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.entity.type.BlockBreakMinion;
import com.qualityplus.minions.base.minions.minion.MinionType;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class MinionEntityFactory {
    public MinionEntity create(final UUID petUniqueId, final UUID owner, Minion minion, boolean loaded) {
        if (minion.getType().equals(MinionType.BLOCK_BREAK)) {
            return BlockBreakMinion.create(petUniqueId, owner, minion, loaded);
        } else if (minion.getType().equals(MinionType.MOB_KILLER)) {
            return MobKillerMinion.create(petUniqueId, owner, minion, loaded);
        } else {
            return CropBreakMinion.create(petUniqueId, owner, minion, loaded);
        }
    }
}
