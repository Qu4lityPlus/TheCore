package com.qualityplus.minions.base.commands;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.api.gui.FakeInventory;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.util.faster.FastStack;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.base.minions.entity.tracker.MinionEntityTracker;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

@Component
public final class TestCommand extends AssistantCommand {
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
           // FakeInventory fakeInventory = TheAssistantPlugin.getAPI().getNms().getFakeInventory(player, 4);
            //FakeInventory fakeInventory = MinionEntityTracker.TRACKED_ENTITIES.values().stream().findFirst().get().getFakeInventory();

            //fakeInventory.getInventory().addItem(FastStack.fast(XMaterial.DIAMOND, 64));
            //fakeInventory.getInventory().addItem(FastStack.fast(XMaterial.COAL, 22));
            //fakeInventory.getInventory().addItem(FastStack.fast(XMaterial.COAL, 22));

            /*fakeInventory.setItems(FastMap.builder(Integer.class, ItemStack.class)
                    .put(0, FastStack.fast(XMaterial.DIAMOND, 64))
                    .put(1, FastStack.fast(XMaterial.COAL, 2))
                    .put(2, FastStack.fast(XMaterial.DIAMOND, 3))
                    .put(3, FastStack.fast(XMaterial.DIAMOND, 1))
                    .build()
            );

            fakeInventory.removeItems(XMaterial.DIAMOND.parseItem(), 30);
            fakeInventory.removeItems(XMaterial.COAL.parseItem(), 3);*/

            //player.openInventory(TheAssistantPlugin.getAPI().getNms().getFakeInventory(player, fakeInventory).getInventoryView().getTopInventory());
        } else
            player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().testCommand));
    }
}