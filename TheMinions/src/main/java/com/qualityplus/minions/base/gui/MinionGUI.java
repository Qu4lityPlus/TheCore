package com.qualityplus.minions.base.gui;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.GUI;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.inventory.SimpleGUI;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.util.MinionPlaceholderUtil;

import java.util.List;
import java.util.UUID;

public abstract class MinionGUI extends GUI {
    protected final Box box;
    protected UUID uuid;

    public MinionGUI(int size, String title, Box box) {
        super(size, title);

        this.box = box;
    }

    public MinionGUI(SimpleGUI simpleGUI, Box box) {
        super(simpleGUI);

        this.box = box;
    }

    public void setItem(Item item) {
        setItem(item, box.files().config().loreWrapper);
    }

    public void setItem(Item item, List<IPlaceholder> placeholderList) {
        setItem(item, placeholderList, box.files().config().loreWrapper);
    }

    protected static String getTitle(final String title, final MinionEntity entity) {
        final Minion minion = Minions.getByID(entity.getData()
                .map(MinionData::getMinionId)
                .orElse("null"));



        final List<IPlaceholder> placeholders = MinionPlaceholderUtil
                .getMinionPlaceholders(entity.getMinionUniqueId())
                .with(MinionPlaceholderUtil.getMinionPlaceholders(minion))
                .get();

        return StringUtils.processMulti(title, placeholders);
    }

    protected Minion getMinion(MinionEntity minionEntity) {
        String id = minionEntity.getData().map(MinionData::getMinionId).orElse(null);

        if (id == null) return null;

        return Minions.getByID(id);
    }
}
