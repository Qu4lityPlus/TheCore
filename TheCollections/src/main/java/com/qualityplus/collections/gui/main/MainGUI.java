package com.qualityplus.collections.gui.main;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.collections.api.box.Box;
import com.qualityplus.collections.base.collection.category.CollectionCategory;
import com.qualityplus.collections.base.collection.gui.GUIOptions;
import com.qualityplus.collections.gui.CollectionsGUI;
import com.qualityplus.collections.gui.category.CategoryGUI;
import com.qualityplus.collections.persistance.data.UserData;
import com.qualityplus.collections.util.CollectionsItemStackUtil;
import com.qualityplus.collections.util.CollectionsPlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class MainGUI extends CollectionsGUI {
    private final MainGUIConfig config;

    public MainGUI(Box box, Player player) {
        super(box.files().inventories().mainGUIConfig, box);

        this.config = box.files().inventories().mainGUIConfig;
        this.uuid = player.getUniqueId();
    }

    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(config);

        setItem(config.getCloseGUI());

        UserData data = box.service().getData(uuid).orElse(new UserData());

        for (CollectionCategory category : box.files().categories().collectionCategories)
            Optional.ofNullable(category.getGuiOptions()).ifPresent(option -> inventory.setItem(option.getSlot(), getItem(data, category, option)));

        setItem(config.getAllCollectionsItem(), CollectionsPlaceholderUtil.getAllCategoriesPlaceholders(data));

        Optional.ofNullable(config.getCustomGoBackItem()).ifPresent(this::setItem);

        return inventory;
    }

    private ItemStack getItem(UserData data, CollectionCategory category, GUIOptions option) {
        return CollectionsItemStackUtil.makeItem(config.getCollectionsItem(), getPlaceholders(data, category), option);
    }

    private List<IPlaceholder> getPlaceholders(UserData data, CollectionCategory category) {
        return CollectionsPlaceholderUtil.getCategoryPlaceholders(data, category)
                .with(CollectionsPlaceholderUtil.getCategoryStatePlaceholder(data, category, box))
                .get();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getCustomGoBackItem())) {
            handleItemCommandClick(player, config.getCustomGoBackItem());
        } else {
            Optional<CollectionCategory> optionalCategory = box.files().categories().getBySlot(slot);

            optionalCategory.ifPresent(category -> player.openInventory(new CategoryGUI(box, player, category).getInventory()));
        }

    }
}
