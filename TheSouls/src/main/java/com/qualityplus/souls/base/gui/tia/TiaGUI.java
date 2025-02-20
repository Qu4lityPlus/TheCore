package com.qualityplus.souls.base.gui.tia;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.console.ConsoleUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.player.PlayerUtils;
import com.qualityplus.assistant.util.sound.SoundUtils;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.souls.base.gui.SoulsGUI;
import com.qualityplus.souls.persistance.data.SoulsData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class TiaGUI extends SoulsGUI {
    private final TiaGUIConfig config;

    public TiaGUI(Box box, Player player) {
        super(box.files().inventories().tiaGUI, box);

        this.config = box.files().inventories().tiaGUI;
        this.uuid = player.getUniqueId();
    }

    private List<IPlaceholder> getPlaceholders(SoulsData soulsData) {
        int collected = soulsData.getTiaSoulsCollected().size();
        int max = box.files().tiaTheFairy().requiredSoulsToExchange;

        String placeholder = collected >= max ? box.files().messages().soulsMessages.clickToClaim : box.files().messages().soulsMessages.notEnoughSoulsPlaceholder;

        return Arrays.asList(
                new Placeholder("souls_player_tia_collected", collected),
                new Placeholder("souls_tia_placeholder", placeholder)
        );
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        SoulsData data = box.service().getData(uuid).orElse(new SoulsData());

        setItem(config.getTiaItem(), getPlaceholders(data));
        setItem(config.getCloseGUI());

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getTiaItem())) {
            player.closeInventory();

            SoulsData data = box.service().getData(uuid).orElse(new SoulsData());

            int collected = data.getTiaSoulsCollected().size();
            int max = box.files().tiaTheFairy().requiredSoulsToExchange;

            if (collected < max) {
                player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.notEnoughSoulsMessage));
                return;
            }

            ConsoleUtils.cmd(PlayerUtils.parseWithName(box.files().tiaTheFairy().tiaCommands, player));

            PlayerUtils.parseWithName(box.files().tiaTheFairy().tiaMessages, player).forEach(message -> player.sendMessage(StringUtils.color(message)));

            SoundUtils.playSound(player, box.files().tiaTheFairy().tiaExchangeSound);

            data.removeAmount(box.files().tiaTheFairy().requiredSoulsToExchange);
        }
    }


}
