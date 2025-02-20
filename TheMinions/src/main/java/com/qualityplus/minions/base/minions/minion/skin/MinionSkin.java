package com.qualityplus.minions.base.minions.minion.skin;

import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.handler.ArmorStandHandler;
import com.qualityplus.minions.util.MinionUpgradeUtil;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class MinionSkin extends OkaeriConfig {
    private String id;
    private String displayName;
    private List<String> description = Collections.emptyList();
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack inHand;

    public ItemStack getItemStack() {
        final Item item = TheMinions.getApi().getConfigFiles().config().skinItem;

        final ItemStack itemStack = ItemStackUtils.makeItem(item,
                PlaceholderBuilder.create(
                        new Placeholder("minion_skin_display_name", displayName),
                        new Placeholder("minion_skin_lore", Optional.ofNullable(description).orElse(Collections.emptyList()))
                ).get(), helmet);

        return MinionUpgradeUtil.addSkinTags(itemStack, id);
    }

    public void apply(final ArmorStandHandler handler) {
        Optional.ofNullable(handler).ifPresent(armorStandHandler -> armorStandHandler.manipulateEntity(this::apply));
    }

    public void apply(final ArmorStand entity) {
        if (entity == null) {
            return;
        }

        final EntityEquipment equipment = entity.getEquipment();

        if (equipment == null) {
            return;
        }

        Optional.ofNullable(helmet).map(ItemStack::clone).ifPresent(equipment::setHelmet);
        Optional.ofNullable(chestplate).map(ItemStack::clone).ifPresent(equipment::setChestplate);
        Optional.ofNullable(leggings).map(ItemStack::clone).ifPresent(equipment::setLeggings);
        Optional.ofNullable(boots).map(ItemStack::clone).ifPresent(equipment::setBoots);
        Optional.ofNullable(inHand).map(ItemStack::clone).ifPresent(equipment::setItemInMainHand);
    }
}
