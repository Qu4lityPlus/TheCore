package com.qualityplus.trades.base.trades;

import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.payable.ItemPayable;
import com.qualityplus.assistant.permissionable.Permissionable;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.trades.api.recipes.Trades;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class PluginTrade extends OkaeriConfig implements ItemPayable, Permissionable {
    private Map<XMaterial, Integer> itemCost;
    private List<String> description;
    private String displayName;
    private String permission;
    private Item itemStack;
    private double price;
    private String id;
    private int slot;
    private int page;

    @Builder
    public PluginTrade(String id, String displayName, List<String> description, String permission, double price, Map<XMaterial, Integer> itemCost, Item itemStack, int slot, int page) {
        this.itemStack = itemStack;
        this.description = description;
        this.displayName = displayName;
        this.permission = permission;
        this.itemCost = itemCost;
        this.price = price;
        this.page = page;
        this.slot = slot;
        this.id = id;
    }

    public ItemStack getResult() {
        return itemStack == null ? null : ItemStackUtils.makeItem(itemStack).clone();
    }

    public void register() {
        Trades.registerNewTrade(this);
    }
}
