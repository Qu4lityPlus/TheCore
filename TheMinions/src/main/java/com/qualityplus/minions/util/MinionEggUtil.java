package com.qualityplus.minions.util;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.faster.FastStack;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.base.event.MinionCreateEvent;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.egg.MinionEgg;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.base.minions.minion.skin.MinionSkin;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class MinionEggUtil {
    private final String PET_ID_KEY = "PET_ID_KEY";
    private final String PET_UNIQUE_ID_KEY = "PET_UUID_KEY";

    public Optional<MinionData> dataFromEgg(final ItemStack itemStack) {

        if (BukkitItemUtil.isNull(itemStack)) {
            return Optional.empty();
        }

        final NBTItem nbtItem = new NBTItem(itemStack);

        if (!nbtItem.hasKey(PET_ID_KEY) || !nbtItem.hasKey(PET_UNIQUE_ID_KEY)) {
            return Optional.empty();
        }

        final Minion minion = Minions.getByID(nbtItem.getString(PET_ID_KEY));

        if (minion == null) {
            return Optional.empty();
        }

        final UUID uuid = UUID.fromString(nbtItem.getString(PET_UNIQUE_ID_KEY));

        return TheMinions.getApi().getMinionsService().getData(uuid);
    }

    public Optional<ItemStack> createNewEgg(final Player player, final Item item, final Minion pet, final int level) {
        final UUID petUuid = UUID.randomUUID();

        final MinionData minionData = new MinionData(
                petUuid, player.getUniqueId(),
                level,
                pet.getId(),
                0,
                new HashMap<>(),
                null,
                null,
                null,
                null,
                null,
                null
        );

        final MinionCreateEvent petCreateEvent = new MinionCreateEvent(minionData);

        Bukkit.getServer().getPluginManager().callEvent(petCreateEvent);

        return getItemStack(item, pet, minionData);

    }

    public Optional<ItemStack> createFromExistent(final Item item, final UUID petUuid) {
        final Optional<MinionData> data = TheMinions.getApi().getMinionsService().getData(petUuid);

        if (data.isEmpty()) {
            return Optional.empty();
        }

        final Optional<Minion> minion = Optional.ofNullable(Minions.getByID(data.get().getMinionId()));

        return minion.flatMap(m -> getItemStack(item, m, data.get()));
    }


    private Optional<ItemStack> getItemStack(final Item item, final Minion minion, final MinionData minionData) {
        final int level = minionData.getLevel();

        final Optional<MinionSkin> minionSkin = minion.getSkin(level);

        final List<IPlaceholder> placeholderList = MinionPlaceholderUtil.getMinionPlaceholders(minion)
                .with(MinionPlaceholderUtil.getMinionPlaceholders(minionData))
                .get();

        final Item item1 = ItemBuilder.of(XMaterial.PLAYER_HEAD, 1)
                .displayName(StringUtils.processMulti(item.getDisplayName(), placeholderList))
                .lore(StringUtils.processMulti(item.getLore(), placeholderList))
                .amount(1)
                .build();

        final ItemStack eggIcon = minionSkin
                .map(MinionSkin::getHelmet)
                .orElse(FastStack.fast(XMaterial.PLAYER_HEAD));

        final ItemStack itemStack = ItemStackUtils.makeItem(item1, eggIcon.clone());

        final NBTItem nbtItem = new NBTItem(itemStack);

        nbtItem.setString(PET_ID_KEY, minion.getId());
        nbtItem.setString(PET_UNIQUE_ID_KEY, minionData.getUuid().toString());

        return Optional.ofNullable(nbtItem.getItem());
    }

}
