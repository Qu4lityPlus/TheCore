package com.qualityplus.enchanting.base.gui.enchantmain;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.enchanting.api.box.Box;
import com.qualityplus.enchanting.api.enchantment.CoreEnchants;
import com.qualityplus.enchanting.api.enchantment.ICoreEnchantment;
import com.qualityplus.enchanting.base.gui.EnchantingGUI;
import com.qualityplus.enchanting.base.gui.enchantmain.handler.ClickHandler;
import com.qualityplus.enchanting.base.gui.enchantmain.handler.MainClickHandler;
import com.qualityplus.enchanting.base.gui.enchantmain.handler.NormalClickHandler;
import com.qualityplus.enchanting.base.gui.enchantmain.handler.ShiftClickHandler;
import com.qualityplus.enchanting.base.gui.enchantsub.EnchantSubGUI;
import com.qualityplus.enchanting.util.EnchantingPlaceholderUtil;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class EnchantMainGUI extends EnchantingGUI {
    private final Map<Integer, ICoreEnchantment> enchantments = new HashMap<>();
    private final EnchantMainGUIConfig config;
    private final ClickHandler handler;
    @Setter
    private boolean giveItem = true;
    private final ItemStack item;
    private final int bookShelf;

    public EnchantMainGUI(Box box, int page, int bookShelf, ItemStack item) {
        super(box.files().inventories().enchantMainGUI, box);

        this.handler = new MainClickHandler(new NormalClickHandler(this, bookShelf, page, box), new ShiftClickHandler(this, item, bookShelf, page, box));
        this.maxPerPage = box.files().inventories().enchantMainGUI.getEnchantmentSlots().size();
        this.hasNext = getEnchantments(item).size() > maxPerPage * page;
        this.config = box.files().inventories().enchantMainGUI;
        this.bookShelf = bookShelf;
        this.item = item;
        this.page = page;
    }



    private static List<ICoreEnchantment> getEnchantments(ItemStack item) {
        if (BukkitItemUtil.isNull(item)) return Collections.emptyList();

        return CoreEnchants.values().stream()
                .filter(enchant -> enchant.canEnchantItem(item))
                .collect(Collectors.toList());
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (getTarget(event).equals(ClickTarget.INSIDE)) {
            int slot = event.getSlot();


            if (isItem(slot, config.getCloseGUI())) {
                event.setCancelled(true);
                player.closeInventory();
            } else if (slot == 19) {
                handler.handle(event);
            } else if (isItem(slot, config.getCustomGoBackItem())) {
                handleItemCommandClick(player, config.getCustomGoBackItem());
            } else {
                event.setCancelled(true);

                if (enchantments.containsKey(slot)) {
                    giveItem = false;
                    Optional.ofNullable(enchantments.getOrDefault(slot, null))
                            .ifPresent(enchant -> player.openInventory(new EnchantSubGUI(box, bookShelf, item, enchant, player.getUniqueId()).getInventory()));
                } else if (isItem(slot, config.getNextPageItem()) && hasNext) {
                    giveItem = false;
                    player.openInventory(new EnchantMainGUI(box, page + 1, bookShelf, item).getInventory());
                } else if (isItem(slot, config.getBackPageItem()) && page > 1) {
                    giveItem = false;
                    player.openInventory(new EnchantMainGUI(box, page - 1, bookShelf, item).getInventory());
                }
            }
        } else {

            handler.handleOutSide(event);

        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<ICoreEnchantment> enchantmentList = getEnchantments(item);

        try {
            int slot = 0;
            int i = maxPerPage * (page - 1);
            if (enchantmentList.size() > 0) {
                while (slot < maxPerPage) {
                    if (enchantmentList.size() > i && i >= 0) {
                        ICoreEnchantment enchantment = enchantmentList.get(i);

                        List<IPlaceholder> placeholders = EnchantingPlaceholderUtil.getEnchantPlaceholders(enchantment, 1);

                        int finalSlot = config.getEnchantmentSlots().get(slot);

                        if (enchantment.getRequiredBookShelf() > bookShelf)
                            inventory.setItem(finalSlot, ItemStackUtils.makeItem(config.getEnchantItemNoBookShelfPower(), placeholders));
                        else
                            inventory.setItem(finalSlot, ItemStackUtils.makeItem(config.getEnchantItem(), placeholders));

                        enchantments.put(finalSlot, enchantment);

                        slot++;
                        i++;
                        continue;
                    }
                    slot++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (page > 1)
            setItem(config.getBackPageItem(), Collections.singletonList(new Placeholder("previous_page", page - 1)));

        if (hasNext)
            setItem(config.getNextPageItem(), Collections.singletonList(new Placeholder("next_page", page + 1)));

        if (item != null) {
            inventory.setItem(19, item);

            if (enchantmentList.size() <= 0)
                setItem(config.getCannotEnchantItem());
        } else
            setItem(config.getEmptyItem());

        setItem(config.getBookShelfItem(), Collections.singletonList(new Placeholder("enchanting_table_bookshelf", bookShelf)));

        Optional.ofNullable(config.getCustomGoBackItem()).ifPresent(this::setItem);

        setItem(config.getCloseGUI());

        return inventory;
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (BukkitItemUtil.isNull(item)) return;

        if (!giveItem) return;

        player.getInventory().addItem(item);
    }
}
