package com.qualityplus.dragon.gui.equipment;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.base.game.guardian.DragonGuardian;
import com.qualityplus.dragon.base.game.guardian.GuardianArmor;
import com.qualityplus.dragon.gui.TheDragonGUI;
import com.qualityplus.dragon.gui.guardian.GuardianGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public final class GuardianEquipmentGUI extends TheDragonGUI {
    private final GuardianEquipmentType equipmentType;
    private final DragonGuardian guardian;
    private final ItemStack[] itemStacks;

    public GuardianEquipmentGUI(Box box, Player player, DragonGuardian guardian, GuardianEquipmentType equip) {
        super(box.files().inventories().guardianEquipmentGUIConfig, box);

        this.itemStacks = player.getInventory().getContents();
        this.equipmentType = equip;
        this.guardian = guardian;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Optional.ofNullable(itemStacks).ifPresent(inventory::setContents);

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        ItemStack itemStack = Optional.ofNullable(event.getCurrentItem())
                .filter(BukkitItemUtil::isNotNull)
                .map(ItemStack::clone)
                .orElse(null);

        if (itemStack == null) return;

        Player player = (Player) event.getWhoClicked();

        GuardianArmor armor = guardian.getGuardianArmor();


        switch (equipmentType) {
            case BOOTS:
                if (!itemMatch(player, itemStack, "boots")) return;

                armor.setBoots(itemStack);
                break;
            case HELMET:
                if (!itemMatch(player, itemStack, "helmet")) return;

                armor.setHelmet(itemStack);
                break;
            case LEGGINGS:
                if (!itemMatch(player, itemStack, "leggings")) return;

                armor.setLeggings(itemStack);
                break;
            case CHESTPLATE:
                if (!itemMatch(player, itemStack, "chestplate")) return;

                armor.setChestplate(itemStack);
                break;
            case WEAPON:
                if (!itemMatch(player, itemStack, "chestplate", "hoe", "axe", "pickaxe")) return;

                armor.setWeapon(itemStack);
                break;
        }

        player.openInventory(new GuardianGUI(box, guardian).getInventory());
    }

    private boolean itemMatch(Player player, ItemStack itemStack, String... toMatch) {

        String item = BukkitItemUtil.getMaterialName(itemStack).toLowerCase();

        if (Arrays.stream(toMatch).anyMatch(item::contains)) return true;

        player.sendMessage(StringUtils.color(box.files().messages().setupMessages.guardianEquipItemDoesntMatch));

        player.openInventory(new GuardianGUI(box, guardian).getInventory());

        return false;
    }

    public enum GuardianEquipmentType {
        HELMET,
        LEGGINGS,
        WEAPON,
        BOOTS,
        CHESTPLATE,

    }
}
