package com.qualityplus.souls.base.gui.addmessages;

import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.souls.api.edition.EditionObject;
import com.qualityplus.souls.api.edition.SoulEdition;
import com.qualityplus.souls.base.gui.SoulsGUI;
import com.qualityplus.souls.base.gui.editgui.SoulsEditGUI;
import com.qualityplus.souls.base.soul.Soul;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AddMessagesGUI extends SoulsGUI {
    private final Map<Integer, String> messageSlots = new HashMap<>();
    private final AddMessagesGUIConfig config;
    private final SoulEdition edition;
    private final Soul soul;

    public AddMessagesGUI(Box box, Soul soul, int page, SoulEdition edition) {
        super(box.files().inventories().addMessagesGUIConfig, box);

        this.maxPerPage = box.files().inventories().addMessagesGUIConfig.getMessagesSlots().size();
        this.hasNext = soul.getMessages().size() > maxPerPage * page;
        this.config = box.files().inventories().addMessagesGUIConfig;
        this.edition = edition;
        this.soul = soul;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<String> messageList = soul.getMessages();

        try {
            int slot = 0;
            int i = maxPerPage * (page - 1);
            if (messageList.size() > 0) {
                while (slot < maxPerPage) {
                    if (messageList.size() > i && i >= 0) {
                        String message = messageList.get(i);

                        int finalSlot = config.getMessagesSlots().get(slot);

                        inventory.setItem(finalSlot, ItemStackUtils.makeItem(config.getMessageItem(), PlaceholderBuilder.create(new Placeholder("soul_message", message)).get()));

                        messageSlots.put(finalSlot, message);

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
            setItem(config.getPreviousPageItem());

        if (hasNext)
            setItem(config.getNextPageItem());


        setItem(config.getGoBackItem());
        setItem(config.getAddMessageItem());
        setItem(config.getCloseGUI());

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBackItem())) {
            player.openInventory(new SoulsEditGUI(box, soul, edition).getInventory());
        } else if (isItem(slot, config.getNextPageItem()) && hasNext) {
            player.openInventory(new AddMessagesGUI(box, soul, page + 1, edition).getInventory());
        } else if (isItem(slot, config.getPreviousPageItem()) && page > 1) {
            player.openInventory(new AddMessagesGUI(box, soul, page - 1, edition).getInventory());
        } else if (messageSlots.containsKey(slot)) {
            if (!event.isRightClick()) return;

            String message = messageSlots.get(slot);

            if (message == null || !soul.getMessages().contains(message)) return;

            soul.getMessages().remove(message);

            player.openInventory(new AddMessagesGUI(box, soul, page, edition).getInventory());
        } else if (isItem(slot, config.getAddMessageItem())) {
            player.closeInventory();
            player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.typeAMessage));
            edition.setEditMode(player.getUniqueId(), new EditionObject(SoulEdition.EditionType.ADD_MESSAGE, soul));
        }
    }


}
