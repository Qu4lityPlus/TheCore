package com.qualityplus.dragon.gui.guardian;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.game.guardian.Guardian;
import com.qualityplus.dragon.base.editmode.GuardianEditMode;
import com.qualityplus.dragon.base.game.guardian.DragonGuardian;
import com.qualityplus.dragon.base.game.guardian.GuardianArmor;
import com.qualityplus.dragon.base.service.GuardianEditServiceImpl.EditType;
import com.qualityplus.dragon.gui.TheDragonGUI;
import com.qualityplus.dragon.gui.equipment.GuardianEquipmentGUI;
import com.qualityplus.dragon.gui.guardians.DragonGuardiansGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class GuardianGUI extends TheDragonGUI {
    private static final String NULL_FORMAT = "&c✘";
    private final GuardianGUIConfig config;
    private final DragonGuardian guardian;

    public GuardianGUI(Box box, Guardian guardian) {
        super(box.files().inventories().guardianGUIConfig, box);

        this.config = box.files().inventories().guardianGUIConfig;
        this.guardian = (DragonGuardian) guardian;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        GuardianArmor guardianArmor = guardian.getGuardianArmor();

        inventory.setItem(config.getHelmetItem().getSlot(), ItemStackUtils.makeItem(config.getHelmetItem(), Optional
                .ofNullable(guardianArmor.getHelmet())
                .orElse(XMaterial.GOLDEN_HELMET.parseItem())));

        inventory.setItem(config.getChestPlateItem().getSlot(), ItemStackUtils.makeItem(config.getChestPlateItem(), Optional
                .ofNullable(guardianArmor.getChestplate())
                .orElse(XMaterial.GOLDEN_CHESTPLATE.parseItem())));

        inventory.setItem(config.getLeggingsItem().getSlot(), ItemStackUtils.makeItem(config.getLeggingsItem(), Optional
                .ofNullable(guardianArmor.getLeggings())
                .orElse(XMaterial.GOLDEN_LEGGINGS.parseItem())));

        inventory.setItem(config.getBootsItem().getSlot(), ItemStackUtils.makeItem(config.getBootsItem(), Optional
                .ofNullable(guardianArmor.getBoots())
                .orElse(XMaterial.GOLDEN_BOOTS.parseItem())));

        inventory.setItem(config.getWeaponItem().getSlot(), ItemStackUtils.makeItem(config.getWeaponItem(), Optional
                .ofNullable(guardianArmor.getWeapon())
                .orElse(XMaterial.GOLDEN_SWORD.parseItem())));

        inventory.setItem(config.getMobTypeItem().getSlot(), ItemStackUtils.makeItem(config.getMobTypeItem(), getMobTypePlaceholders()));
        inventory.setItem(config.getDisplayNameItem().getSlot(), ItemStackUtils.makeItem(config.getDisplayNameItem(), getDisplayNamePlaceholders()));
        inventory.setItem(config.getHealthItem().getSlot(), ItemStackUtils.makeItem(config.getHealthItem(), getHealthPlaceholders()));

        inventory.setItem(config.getGoBackItem().getSlot(), ItemStackUtils.makeItem(config.getGoBackItem()));
        inventory.setItem(config.getCloseGUI().getSlot(), ItemStackUtils.makeItem(config.getCloseGUI()));

        return inventory;
    }

    private List<IPlaceholder> getMobTypePlaceholders() {
        return Collections.singletonList(new Placeholder("type", Optional.ofNullable(guardian.getEntity()).orElse(NULL_FORMAT)));
    }

    private List<IPlaceholder> getDisplayNamePlaceholders() {
        return Collections.singletonList(new Placeholder("displayname", Optional.ofNullable(guardian.getDisplayName()).orElse(NULL_FORMAT)));
    }

    private List<IPlaceholder> getHealthPlaceholders() {
        return Collections.singletonList(new Placeholder("health", String.valueOf(guardian.getHealth())));
    }

    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();

        int slot = e.getSlot();

        e.setCancelled(true);

        if (!getTarget(e).equals(ClickTarget.INSIDE)) return;

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBackItem())) {
            player.openInventory(new DragonGuardiansGUI(box, 1).getInventory());
        } else if (isItem(slot, config.getHelmetItem())) {
            player.openInventory(new GuardianEquipmentGUI(box, player, guardian, GuardianEquipmentGUI.GuardianEquipmentType.HELMET).getInventory());
        } else if (isItem(slot, config.getChestPlateItem())) {
            player.openInventory(new GuardianEquipmentGUI(box, player, guardian, GuardianEquipmentGUI.GuardianEquipmentType.CHESTPLATE).getInventory());
        } else if (isItem(slot, config.getLeggingsItem())) {
            player.openInventory(new GuardianEquipmentGUI(box, player, guardian, GuardianEquipmentGUI.GuardianEquipmentType.LEGGINGS).getInventory());
        } else if (isItem(slot, config.getBootsItem())) {
            player.openInventory(new GuardianEquipmentGUI(box, player, guardian, GuardianEquipmentGUI.GuardianEquipmentType.BOOTS).getInventory());
        } else if (isItem(slot, config.getWeaponItem())) {
            player.openInventory(new GuardianEquipmentGUI(box, player, guardian, GuardianEquipmentGUI.GuardianEquipmentType.WEAPON).getInventory());
        } else
            setSetupMode(player, slot);
    }

    private void setSetupMode(Player player, int slot) {

        EditType type = slot == config.getMobTypeItem().getSlot() ? EditType.MOB :
                slot == config.getDisplayNameItem().getSlot() ? EditType.DISPLAYNAME :
                        slot == config.getHealthItem().getSlot() ? EditType.HEALTH : null;

        if (type == null) return;

        GuardianEditMode editMode = GuardianEditMode.builder()
                .uuid(player.getUniqueId())
                .dragonGuardian(guardian)
                .editType(type)
                .build();

        TheDragon.getApi().getGuardianEditService().setInEditMode(editMode);

        player.sendMessage(StringUtils.color(box.files().messages().setupMessages.guardianEditModeMessage.replace("%type%", type.getName())));

        player.closeInventory();

    }

}
