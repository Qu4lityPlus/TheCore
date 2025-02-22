package com.qualityplus.pets.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.pets.api.box.Box;
import com.qualityplus.pets.api.pet.Pets;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.util.PetEggUtil;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class GiveEggCommand extends AssistantCommand {
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 3) {
            Player toGive = Bukkit.getPlayer(args[1]);

            Pet pet = Pets.getByID(args[2]);

            if (toGive == null) {
                player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidPlayer));
                return false;
            }

            if (pet == null) {
                player.sendMessage(StringUtils.color(box.files().messages().petMessages.invalidPet));
                return false;
            }

            Optional<ItemStack> petEgg = PetEggUtil.createNewEgg(box.files().config().petEggItem, pet);

            if (!petEgg.isPresent()) {
                player.sendMessage(StringUtils.color(box.files().messages().petMessages.invalidPetEgg));
                return false;
            }

            toGive.getInventory().addItem(petEgg.get());

        } else {
            player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 2 ? null : args.length == 3 ? Pets.values().stream().map(Pet::getId).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().giveEggCommand));
    }
}