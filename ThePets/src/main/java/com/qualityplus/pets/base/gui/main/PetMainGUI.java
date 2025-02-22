package com.qualityplus.pets.base.gui.main;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.pets.api.box.Box;
import com.qualityplus.pets.api.pet.Pets;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.api.pet.entity.PetEntity.DeSpawnReason;
import com.qualityplus.pets.base.config.Messages.PetMessages;
import com.qualityplus.pets.base.gui.PetsGUI;
import com.qualityplus.pets.base.gui.sub.PetLevelsGUI;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.factory.PetEntityFactory;
import com.qualityplus.pets.base.pet.tracker.PetEntityTracker;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.pets.persistance.data.UserData;
import com.qualityplus.pets.persistance.data.inside.InventoryPet;
import com.qualityplus.pets.util.PetEggUtil;
import com.qualityplus.pets.util.PetPlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class PetMainGUI extends PetsGUI {
    private final Map<Integer, InventoryPet> petsMap = new HashMap<>();
    private final PetsGUIConfig config;

    public PetMainGUI(Box box, Player player) {
        super(box.files().inventories().mainGUIConfig, box);

        this.config = box.files().inventories().mainGUIConfig;
        this.uuid = player.getUniqueId();
    }


    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(config);
        setItem(config.getCloseGUI());

        Optional<UserData> data = box.service().getData(uuid);

        boolean petsAreHidden = data.map(userData -> userData.getUserSettings().isPetsAreHidden()).orElse(false);
        boolean isConvertItemMode = data.map(userData -> userData.getUserSettings().isConvertPetToItemMode()).orElse(false);

        List<InventoryPet> pets = data.map(userData -> userData.getInventoryData().getPets()).orElse(Collections.emptyList());

        int count = 0;

        Optional<Pet> selectedPet = data.flatMap(UserData::getSpawnedPet);

        PetMessages petMessages = box.files().messages().petMessages;

        String petName = selectedPet
                .map(value -> petMessages.selectedPetInGUI.replace("%pet_egg_item_displayname%", value.getPetEgg().getDisplayName()))
                .orElseGet(() -> petMessages.nonSelectedPetInGUI);

        /**
         * TODO
         *
         * Change try/catch for list size checker
         */
        for (Integer slot : config.getSlots()) {
            try {
                InventoryPet pet = pets.get(count);

                List<IPlaceholder> placeholders = PetPlaceholderUtil.getPetPlaceholders(pet.getPetId())
                        .with(PetPlaceholderUtil.getPetPlaceholders(pet.getUuid()))
                        .with(getSummonPlaceholder(pet.getUuid()))
                        .get();

                inventory.setItem(slot, ItemStackUtils.makeItem(config.getPetItem(), placeholders, getIcon(pet)));

                petsMap.put(slot, pet);

                count++;
            } catch (Exception ignored) {
                break;
            }
        }

        Optional.ofNullable(config.getCustomGoBackItem()).ifPresent(this::setItem);

        List<IPlaceholder> selectedPetPl = new Placeholder("pet_selected_pet", petName).alone();

        Item petsAreHiddenItem = petsAreHidden ? config.getHidePetsEnabled() : config.getHidePetsDisabled();

        setItem(petsAreHiddenItem, selectedPetPl);

        Item convertItemModeItem = isConvertItemMode ? config.getConvertPetToItemEnabled() : config.getConvertPetToItemDisabled();

        setItem(convertItemModeItem, selectedPetPl);

        setItem(config.getPetMenuInfoItem(), selectedPetPl);

        return inventory;
    }


    private List<IPlaceholder> getSummonPlaceholder(UUID petUuid) {
        Optional<UserData> data = box.service().getData(uuid);

        Optional<PetData> selectedPetData = data.flatMap(UserData::getSpawnedPetData);

        PetMessages messages = box.files().messages().petMessages;

        String summonPlaceholder = selectedPetData
                .filter(petData -> petData.getUuid().equals(petUuid)).isPresent() ? messages.clickToDeSpawn : messages.clickToSummon;

        return new Placeholder("pet_summon_status", summonPlaceholder).alone();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getConvertPetToItemDisabled()) || isItem(slot, config.getConvertPetToItemEnabled())) {
            Optional<UserData> data = box.service().getData(uuid);

            data.ifPresent(UserData::switchConvertToItemMode);

            reOpen(player);
        } else if (isItem(slot, config.getHidePetsEnabled()) || isItem(slot, config.getHidePetsDisabled())) {
            Optional<UserData> data = box.service().getData(uuid);

            data.ifPresent(UserData::switchPetsHiddenMode);

            reOpen(player);
        } else if (petsMap.containsKey(slot)) {
            InventoryPet pet = petsMap.getOrDefault(slot, null);

            if (pet == null) return;

            if (event.isLeftClick()) {
                Optional<UserData> data = box.service().getData(uuid);

                boolean isConvertItemMode = data.map(userData -> userData.getUserSettings().isConvertPetToItemMode()).orElse(false);

                UUID spawnedPet = data.map(userData -> userData.getSpawnedData().getSpawnedPetUUID()).orElse(null);

                if (isConvertItemMode)
                    convertItem(player, pet, spawnedPet);
                else
                    spawnAndDeSpawn(player, pet, spawnedPet);
            } else if (isItem(slot, config.getCustomGoBackItem())) {
                handleItemCommandClick(player, config.getCustomGoBackItem());
            } else {
                player.openInventory(new PetLevelsGUI(box, player, pet, 1).getInventory());
            }


        }

    }

    private void spawnAndDeSpawn(Player player, InventoryPet pet, UUID spawnedPet) {
        player.closeInventory();

        Pet pet1 = Pets.getByID(pet.getPetId());

        boolean spawnMessage = spawnedPet == null;

        if (spawnedPet == null) {

            if (pet1 == null) return;

            PetEntity petEntity = PetEntityFactory.create(pet.getUuid(), player.getUniqueId(), pet1);

            petEntity.spawn();
        } else if (spawnedPet.equals(pet.getUuid())) {
            PetEntityTracker
                    .getByID(pet.getUuid())
                    .ifPresent(e -> e.deSpawn(DeSpawnReason.PLAYER_DE_SPAWN_PET));
        }

        if (pet1 == null) return;

        String preMessage = spawnMessage ? box.files().messages().petMessages.summonedPet : box.files().messages().petMessages.deSpawnedPet;

        String message = StringUtils.processMulti(preMessage, PetPlaceholderUtil.getPetPlaceholders(pet1).get());

        player.sendMessage(message);
    }

    private void convertItem(Player player, InventoryPet pet, UUID spawnedPet) {
        player.closeInventory();

        //DeSpawn pet if it's spawned
        Optional.ofNullable(spawnedPet)
                .filter(petUUID -> petUUID.equals(pet.getUuid()))
                .flatMap(petUUID -> PetEntityTracker.getByID(pet.getUuid()))
                .ifPresent(e -> e.deSpawn(DeSpawnReason.PLAYER_DE_SPAWN_PET));


        //Remove pet from gui
        box.service()
                .getData(uuid)
                .ifPresent(data1 -> data1.getInventoryData().removePet(pet.getUuid()));

        //Add Pet to player
        PetEggUtil
                .createFromExistent(box.files().config().petEggItem, pet.getUuid())
                .ifPresent(player.getInventory()::addItem);
    }

    private void reOpen(Player player) {
        player.openInventory(new PetMainGUI(box, player).getInventory());
    }
}
