package com.qualityplus.anvil.base.provider;

import com.qualityplus.anvil.api.provider.EnchantmentProvider;
import com.qualityplus.anvil.api.session.AnvilSession;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.anvil.api.session.AnvilSession.SessionResult;
import org.bukkit.Material;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public abstract class CommonEnchantmentProvider implements EnchantmentProvider {
   protected SessionResult getCommonAnswer(AnvilSession session) {
       boolean toUpgradeIsNull = BukkitItemUtil.isNull(session.getItemToUpgrade());
       boolean toSacrificeIsNull = BukkitItemUtil.isNull(session.getItemToSacrifice());

       if (toSacrificeIsNull && toUpgradeIsNull) return AnvilSession.SessionResult.NOTHING_SET;

       if (!toUpgradeIsNull && toSacrificeIsNull) return AnvilSession.SessionResult.ONLY_ITEM_TO_UPGRADE;

       if (toUpgradeIsNull) return AnvilSession.SessionResult.ONLY_ITEM_TO_SACRIFICE;

       Material toUpgrade = session.getItemToUpgrade().getType();
       Material toSacrifice = session.getItemToSacrifice().getType();

       if (!toUpgrade.equals(toSacrifice) && !(session.getItemToSacrifice().getItemMeta() instanceof EnchantmentStorageMeta)) return AnvilSession.SessionResult.ERROR_CANNOT_COMBINE_THOSE_ITEMS;

       return null;
   }
}
