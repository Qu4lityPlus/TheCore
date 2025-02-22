package com.qualityplus.collections.gui.category;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.collections.api.box.Box;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.collection.category.CollectionCategory;
import com.qualityplus.collections.base.collection.gui.GUIOptions;
import com.qualityplus.collections.base.collection.registry.CollectionsRegistry;
import com.qualityplus.collections.gui.CollectionsGUI;
import com.qualityplus.collections.gui.collection.CollectionGUI;
import com.qualityplus.collections.gui.main.MainGUI;
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

public final class CategoryGUI extends CollectionsGUI {
    private final CategoryGUIConfig config;
    private final CollectionCategory category;

    public CategoryGUI(Box box, Player player, CollectionCategory category) {
        super(box.files().inventories().categoryGUIConfig, box);

        this.config = box.files().inventories().categoryGUIConfig;
        this.uuid = player.getUniqueId();
        this.category = category;
    }

    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(config);

        setItem(config.getCloseGUI());

        UserData data = box.service().getData(uuid).orElse(new UserData());

        setItems(category, data);

        setItem(config.getGoBackItem());

        return inventory;
    }

    private ItemStack getCollectionItem(UserData data, Collection collection, GUIOptions option) {
        if (data.getCollections().getXp(collection.getId()) > 0) {
            return CollectionsItemStackUtil.makeItem(config.getUnlockedItem(), getPlaceholders(data, collection), option);
        } else
            return ItemStackUtils.makeItem(config.getLockedItem(), getPlaceholders(data, collection));
    }

    private List<IPlaceholder> getPlaceholders(UserData data, Collection collection) {
        return CollectionsPlaceholderUtil.getCollectionsPlaceholders(data, collection);
    }

    private void setItems(CollectionCategory category, UserData data) {
        setCollectionItems(category, data);

        setCategoryItem(category, data);
    }

    private void setCategoryItem(CollectionCategory category, UserData data) {
        List<IPlaceholder> placeholders = CollectionsPlaceholderUtil.getCategoryPlaceholders(data, category)
                .with(CollectionsPlaceholderUtil.getCategoryStatePlaceholder(data, category, box))
                .get();

        inventory.setItem(config.getCategoryItem().getSlot(), CollectionsItemStackUtil.makeItem(config.getCategoryItem(), placeholders, category.getGuiOptions()));
    }

    private void setCollectionItems(CollectionCategory category, UserData data) {
        for (Collection collection : CollectionsRegistry.getByCategory(category))
            Optional.ofNullable(collection.getGuiOptions()).ifPresent(option -> inventory.setItem(option.getSlot(), getCollectionItem(data, collection, option)));

    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBackItem())) {
            player.openInventory(new MainGUI(box, player).getInventory());
        } else {
            Optional<Collection> optionalCollection = CollectionsRegistry.values(Collection::isEnabled)
                    .stream()
                    .filter(collection -> collection.getGuiOptions().getSlot() == slot)
                    .filter(collection -> collection.getCategory().equals(category.getId()))
                    .findFirst();

            UserData data = box.service().getData(uuid).orElse(new UserData());

            optionalCollection.ifPresent(s -> {
                if (data.getCollections().getXp(s.getId()) > 0)
                    player.openInventory(new CollectionGUI(box, player, s).getInventory());
                else
                    player.sendMessage(StringUtils.color(box.files().messages().collectionsMessages.lockedMessage));
            });
        }

    }
}
