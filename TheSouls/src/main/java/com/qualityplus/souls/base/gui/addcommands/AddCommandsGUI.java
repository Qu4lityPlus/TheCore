package com.qualityplus.souls.base.gui.addcommands;

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

public final class AddCommandsGUI extends SoulsGUI {
    private final Map<Integer, String> commandSlots = new HashMap<>();
    private final AddCommandsGUIConfig config;
    private final SoulEdition edition;
    private final Soul soul;

    public AddCommandsGUI(Box box, Soul soul, int page, SoulEdition edition) {
        super(box.files().inventories().addCommandsGUIConfig, box);

        this.maxPerPage = box.files().inventories().addCommandsGUIConfig.getCommandsSlots().size();
        this.hasNext = soul.getCommands().size() > maxPerPage * page;
        this.config = box.files().inventories().addCommandsGUIConfig;
        this.edition = edition;
        this.soul = soul;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<String> messageList = soul.getCommands();

        try {
            int slot = 0;
            int i = maxPerPage * (page - 1);
            if (messageList.size() > 0) {
                while (slot < maxPerPage) {
                    if (messageList.size() > i && i >= 0) {
                        String message = messageList.get(i);

                        int finalSlot = config.getCommandsSlots().get(slot);

                        inventory.setItem(finalSlot, ItemStackUtils.makeItem(config.getCommandItem(), PlaceholderBuilder.create(new Placeholder("soul_command", message)).get()));

                        commandSlots.put(finalSlot, message);

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
        setItem(config.getAddCommandItem());
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
            player.openInventory(new AddCommandsGUI(box, soul, page + 1, edition).getInventory());
        } else if (isItem(slot, config.getPreviousPageItem()) && page > 1) {
            player.openInventory(new AddCommandsGUI(box, soul, page - 1, edition).getInventory());
        } else if (commandSlots.containsKey(slot)) {
            if (!event.isRightClick()) return;

            String command = commandSlots.get(slot);

            if (command == null || !soul.getCommands().contains(command)) return;

            soul.getCommands().remove(command);

            player.openInventory(new AddCommandsGUI(box, soul, page, edition).getInventory());
        } else if (isItem(slot, config.getAddCommandItem())) {
            player.closeInventory();
            player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.typeACommand));
            edition.setEditMode(player.getUniqueId(), new EditionObject(SoulEdition.EditionType.ADD_COMMAND, soul));
        }
    }


}
