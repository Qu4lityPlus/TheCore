package com.qualityplus.minions.base.gui.main.setup.items;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.main.MainMinionGUIConfig;
import com.qualityplus.minions.base.gui.main.setup.ItemSetup;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.util.MinionEggUtil;
import com.qualityplus.minions.util.MinionPlaceholderUtil;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class MinionEggSetup implements ItemSetup {
    @Override
    public void setItem(Inventory inventory, Box box, MainMinionGUIConfig config, MinionEntity minionEntity) {

        Optional<MinionData> data = TheMinions.getApi().getMinionsService().getData(minionEntity.getMinionUniqueId());

        String id = data.map(MinionData::getMinionId).orElse(null);

        if (id == null) return;

        Minion minion = Minions.getByID(id);

        if (minion == null) return;

        Optional<ItemStack> itemStack = MinionEggUtil.createFromExistent(box.files().config().minionEggItem, minionEntity.getMinionUniqueId());

        List<IPlaceholder> placeholders = MinionPlaceholderUtil
                .getMinionPlaceholders(minionEntity.getMinionUniqueId())
                .with(MinionPlaceholderUtil.getMinionPlaceholders(minion))
                .get();

        itemStack.ifPresent(item -> inventory.setItem(config.getMinionItem().getSlot(), ItemStackUtils.makeItem(config.getMinionItem(), placeholders, item)));

    }
}
