package com.qualityplus.runes.api.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public final class RuneTableConfig extends OkaeriConfig {
    private final XMaterial baseItem;
    private final RuneItem centerItem;
    private final RuneItem cornerItem;

    @Builder
    public RuneTableConfig(XMaterial baseItem, RuneItem centerItem, RuneItem cornerItem) {
        this.baseItem = baseItem;
        this.centerItem = centerItem;
        this.cornerItem = cornerItem;
    }

    @Data
    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static final class RuneItem extends OkaeriConfig{
        private final XMaterial material;
        private final String texture;

        public ItemStack get() {
            if (material.equals(XMaterial.PLAYER_HEAD))
                return ItemStackUtils.makeItem(Item.builder()
                        .amount(1)
                        .headData(texture)
                        .material(material)
                        .build());
            return material.parseItem();
        }
    }
}
