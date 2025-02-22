package com.qualityplus.dragon.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.game.guardian.Guardian;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Component
public final class AddGuardianCommand extends AssistantCommand {
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 2) {
            String id = args[1];

            Guardian guardian = box.files().guardians().getGuardianById(id);

            if (guardian == null) {

                box.files().guardians().addGuardian(id);

                player.sendMessage(StringUtils.color(box.files().messages().setupMessages.guardianAdded
                        .replace("%thedragon_guardian_id%", id)
                        .replace("%prefix%", box.files().config().prefix)));

            } else {
                player.sendMessage(StringUtils.color(box.files().messages().setupMessages.guardianAlreadyExist
                        .replace("%prefix%", box.files().config().prefix)));
            }

    } else {
            player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return null;
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().addGuardianCommand));
    }
}
