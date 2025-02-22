package com.qualityplus.collections.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.collections.api.box.Box;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.collection.executor.ExecutorType;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public final class DropItemCommand extends AssistantCommand {
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            Player player = (Player) sender;

            String id = args[1];

            Collection collection = box.files().collections().collections.stream().filter(collection1 -> collection1.getId().equals(id)).findFirst().orElse(null);

            ItemStack itemStack = player.getItemInHand();

            if (collection == null) {
                player.sendMessage(StringUtils.color(box.files().messages().collectionsMessages.invalidCollection));
                return false;
            }

            Item item = player.getWorld().spawn(player.getLocation().clone().add(5, 0, 5), Item.class);

            if (collection.getCollectionExecutor().getExecutorType().equals(ExecutorType.BY_PICK_UP_MATERIAL)) {
                item.setItemStack(Objects.requireNonNull(collection.getCollectionExecutor().getMaterial().parseItem()));
            } else {
                item.setItemStack(collection.getCollectionExecutor().getItem());
            }


            List<IPlaceholder> placeholders = Arrays.asList(new Placeholder("collection_item_type", BukkitItemUtil.getName(itemStack)), new Placeholder("collection_id", id));

            player.sendMessage(StringUtils.processMulti(box.files().messages().collectionsMessages.successfullyDroppedItem, placeholders));

        } else
            sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 2 ? box.files().collections().collections.stream().map(Collection::getId).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().dropItem));
    }
}
