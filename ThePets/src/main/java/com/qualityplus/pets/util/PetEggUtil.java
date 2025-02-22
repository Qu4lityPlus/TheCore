package com.qualityplus.pets.util;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.pets.ThePets;
import com.qualityplus.pets.api.pet.Pets;
import com.qualityplus.pets.base.event.PetCreateEvent;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.egg.PetEgg;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class PetEggUtil {
    private final String PET_ID_KEY = "PET_ID_KEY";
    private final String PET_UNIQUE_ID_KEY = "PET_UUID_KEY";

    public Optional<PetData> dataFromEgg(ItemStack itemStack) {
        if (BukkitItemUtil.isNull(itemStack)) return Optional.empty();

        NBTItem nbtItem = new NBTItem(itemStack);

        if (!nbtItem.hasKey(PET_ID_KEY) || !nbtItem.hasKey(PET_UNIQUE_ID_KEY)) return Optional.empty();

        Pet pet = Pets.getByID(nbtItem.getString(PET_ID_KEY));

        if (pet == null) return Optional.empty();

        UUID uuid = UUID.fromString(nbtItem.getString(PET_UNIQUE_ID_KEY));

        return ThePets.getApi().getPetsService().getData(uuid);
    }

    public Optional<ItemStack> createNewEgg(Item item, Pet pet) {

        UUID petUuid = UUID.randomUUID();

        PetData petData = new PetData(petUuid, 0, 0D, pet.getId());

        PetCreateEvent petCreateEvent = new PetCreateEvent(petData);

        Bukkit.getServer().getPluginManager().callEvent(petCreateEvent);

        return getItemStack(item, pet, petData);

    }

    public Optional<ItemStack> createFromExistent(Item item, UUID petUuid) {
        Optional<PetData> petData = ThePets.getApi().getPetsService().getData(petUuid);

        if (!petData.isPresent()) return Optional.empty();

        Optional<Pet> pet = Optional.ofNullable(Pets.getByID(petData.get().getPetId()));

        if (!pet.isPresent()) return Optional.empty();

        return getItemStack(item, pet.get(), petUuid);
    }

    private Optional<ItemStack> getItemStack(Item item, Pet pet, UUID petUuid) {
        PetEgg petEgg = pet.getPetEgg();

        List<IPlaceholder> placeholderList = PetPlaceholderUtil.getPetPlaceholders(pet)
                .with(PetPlaceholderUtil.getPetPlaceholders(petUuid))
                .get();

        ItemStack itemStack = ItemBuilder.of()
                .headData(petEgg.getTexture())
                .displayName(StringUtils.processMulti(item.getDisplayName(), placeholderList))
                .lore(StringUtils.processMulti(item.getLore(), placeholderList))
                .material(petEgg.getMaterial())
                .amount(1)
                .buildStack();

        NBTItem nbtItem = new NBTItem(itemStack);

        nbtItem.setString(PET_ID_KEY, petEgg.getPetId());
        nbtItem.setString(PET_UNIQUE_ID_KEY, petUuid.toString());

        return Optional.ofNullable(nbtItem.getItem());
    }

    private Optional<ItemStack> getItemStack(Item item, Pet pet, PetData petData) {
        PetEgg petEgg = pet.getPetEgg();

        List<IPlaceholder> placeholderList = PetPlaceholderUtil.getPetPlaceholders(pet)
                .with(PetPlaceholderUtil.getPetPlaceholders(petData))
                .get();

        ItemStack itemStack = ItemBuilder.of()
                .headData(petEgg.getTexture())
                .displayName(StringUtils.processMulti(item.getDisplayName(), placeholderList))
                .lore(StringUtils.processMulti(item.getLore(), placeholderList))
                .material(petEgg.getMaterial())
                .amount(1)
                .buildStack();

        NBTItem nbtItem = new NBTItem(itemStack);

        nbtItem.setString(PET_ID_KEY, petEgg.getPetId());
        nbtItem.setString(PET_UNIQUE_ID_KEY, petData.getUuid().toString());

        return Optional.ofNullable(nbtItem.getItem());
    }

}
