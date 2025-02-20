package com.qualityplus.minions.api.service;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.minion.layout.LayoutType;

public interface MinionLayoutService {
    public LayoutType getMinionLayout(final MinionEntity minion);
    public boolean hasInvalidLayout(final MinionEntity minion);
}
