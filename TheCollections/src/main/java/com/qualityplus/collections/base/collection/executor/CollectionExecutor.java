package com.qualityplus.collections.base.collection.executor;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Exclude;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public final class CollectionExecutor extends OkaeriConfig {
    private final ExecutorType executorType;
    private final XMaterial material;
    private final String itemStack;

    @Exclude
    private ItemStack item;

    private CollectionExecutor(ExecutorType executorType, XMaterial material, String itemStack) {
        this.executorType = executorType;
        this.itemStack = itemStack;
        this.material = material;
    }

    public ItemStack getItem() {
        if (item == null) item = BukkitItemUtil.deserialize(itemStack);

        return item;
    }

    public static CollectionExecutor ofMaterial(XMaterial material) {
        return new CollectionExecutor(ExecutorType.BY_PICK_UP_MATERIAL, material, null);
    }

    public static CollectionExecutor ofItemStack(ItemStack itemStack) {
        return new CollectionExecutor(ExecutorType.BY_PICK_UP_ITEM_STACK, null, BukkitItemUtil.serialize(itemStack));
    }

}
