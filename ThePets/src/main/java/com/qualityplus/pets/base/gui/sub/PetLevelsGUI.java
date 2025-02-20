package com.qualityplus.pets.base.gui.sub;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.pets.api.box.Box;
import com.qualityplus.pets.api.pet.Pets;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.egg.PetEgg;
import com.qualityplus.pets.base.gui.PetsGUI;
import com.qualityplus.pets.base.gui.main.PetMainGUI;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.pets.persistance.data.inside.InventoryPet;
import com.qualityplus.pets.util.PetPlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class PetLevelsGUI extends PetsGUI {
    private final PetLevelsGUIConfig config;
    private final InventoryPet invPet;

    private static int getMaxPage(InventoryPet skill, Box box) {
        double maxLevel = getMaxLevel(skill);

        double slotsSize = box.files().inventories().petLevelsGUIConfig.getLevelSlots().size();

        return (int)Math.ceil(maxLevel / slotsSize);
    }

    private static int getMaxLevel(InventoryPet pet) {
        return Optional.ofNullable(Pets.getByID(pet.getPetId())).map(Pet::getMaxLevel).orElse(1);
    }

    private static String getName(InventoryPet pet) {
        return Optional.ofNullable(Pets.getByID(pet.getPetId())).map(Pet::getPetEgg)
                .map(PetEgg::getDisplayName)
                .orElse("");
    }

    public PetLevelsGUI(Box box, Player player, InventoryPet pet, int page) {
        super(box.files().inventories().petLevelsGUIConfig.getSize(),
              StringUtils.processMulti(box.files().inventories().petLevelsGUIConfig.getTitle(), Arrays.asList(
                      new Placeholder("pet_egg_displayname", getName(pet)),
                      new Placeholder("current_page", page),
                      new Placeholder("max_page", getMaxPage(pet, box) )
              )), box);

        this.hasNext = getMaxLevel(pet) > box.files().inventories().petLevelsGUIConfig.getLevelSlots().size() * page;
        this.config = box.files().inventories().petLevelsGUIConfig;
        this.uuid = player.getUniqueId();
        this.invPet = pet;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(config);

        setItem(config.getCloseGUI());

        PetData data = box.petService().getData(invPet.getUuid()).orElse(new PetData());

        int level = data.getLevel();

        int maxPerPage = config.getLevelSlots().size();

        int count = (maxPerPage * page) - maxPerPage;

        int maxLevel = getMaxLevel(invPet);

        Pet pet = Pets.getByID(invPet.getPetId());

        List<IPlaceholder> placeholders = PetPlaceholderUtil.getPetPlaceholders(pet)
                .with(PetPlaceholderUtil.getPetPlaceholders(invPet.getUuid()))
                .get();

        inventory.setItem(config.getPetItem().getSlot(), ItemStackUtils.makeItem(config.getPetItem(), placeholders, getIcon(invPet)));

        for (Integer slot : config.getLevelSlots()) {
            count++;

            if (count > maxLevel) break;

            Item item = count == level + 1 ? config.getInProgressItem() :
                        count > level ? config.getLockedItem() : config.getUnlockedItem();

            inventory.setItem(slot, getItem(item, data, pet, count));

        }

        if (page > 1)
            setItem(config.getPreviousPage());

        if (hasNext)
            setItem(config.getNextPage());

        setItem(config.getGoBack());

        return inventory;
    }

    private ItemStack getItem(Item item, PetData data, Pet pet, int level) {
        /**
         * TODO Pass Pet Placeholders in arguments
         */
        PlaceholderBuilder builder = PetPlaceholderUtil.getPetPlaceholders(pet)
                .with(PetPlaceholderUtil.getPetPlaceholders(data.getUuid(), level));

        return ItemStackUtils.makeItem(item, builder.get());
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBack())) {
            player.openInventory(new PetMainGUI(box, player).getInventory());
        } else if (isItem(slot, config.getPreviousPage()) && page > 1) {
            player.openInventory(new PetLevelsGUI(box, player, invPet, page - 1).getInventory());
        } else if (isItem(slot, config.getNextPage()) && hasNext) {
            player.openInventory(new PetLevelsGUI(box, player, invPet, page + 1).getInventory());
        }
    }
}
