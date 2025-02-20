package com.qualityplus.collections.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.collections.api.box.Box;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.collection.registry.CollectionsRegistry;
import com.qualityplus.collections.persistance.data.UserData;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public final class SetXPCommand extends AssistantCommand {
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 4) {

            Player player = Bukkit.getPlayer(args[1]);

            if (player == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidPlayer));
                return false;
            }

            Collection collection = CollectionsRegistry.getByID(args[2]);

            Double xp = NumberUtil.doubleOrNull(args[3]);

            if (collection == null) {
                player.sendMessage(StringUtils.color(box.files().messages().collectionsMessages.invalidCollection));
                return false;
            }

            if (xp == null) {
                player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidAmount));
                return false;
            }

            Optional<UserData> data = box.service().getData(player.getUniqueId());

            data.ifPresent(userData -> userData.getCollections().setXp(collection.getId(), xp));

            //Set xp to required xp of 1 level lowest
            int newLevel = IntStream.range(0, collection.getMaxLevel())
                    .filter(number -> collection.getLevelRequirement(number) <= xp)
                    .max().orElse(0);

            data.ifPresent(userData -> userData.getCollections().setLevel(collection.getId(), newLevel));

            List<IPlaceholder> placeholders = Arrays.asList(new Placeholder("amount", xp)
                    , new Placeholder("object", collection.getId())
                    , new Placeholder("player", player.getName())
                    , new Placeholder("new_level", newLevel)
                    , new Placeholder("new_xp", data.map(d -> d.getCollections().getXp(collection.getId())).orElse(0D)));

            sender.sendMessage(StringUtils.processMulti(box.files().messages().collectionsMessages.setXP, placeholders));

        } else
            sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 2 ? null :
                args.length == 3 ? box.files().collections().collections.stream().map(Collection::getId).collect(Collectors.toList()) :
                        args.length == 4 ? NumberUtil.stringSecuence(1,10) : Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().setXPCommand));
    }
}
