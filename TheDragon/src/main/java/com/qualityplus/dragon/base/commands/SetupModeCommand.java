package com.qualityplus.dragon.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.service.AltarSetupService;
import com.qualityplus.assistant.lib.eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Delayed;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public final class SetupModeCommand extends AssistantCommand {
    private @Inject AltarSetupService setupService;
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1){
            UUID uuid = player.getUniqueId();

            if(setupService.playerIsInEditMode(uuid)){
                setupService.removePlayer(uuid);
                player.sendMessage(StringUtils.color(box.files().messages().setupMessages.setupModeLeft.replace("%prefix%", box.files().config().prefix)));
            }else{
                setupService.addPlayer(uuid);
                box.files().messages().setupMessages.altarSetupMode.forEach(message -> player.sendMessage(StringUtils.color(message.replace("%prefix%", box.files().config().prefix))));
            }
        }else{
            player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }


    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box){
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().setupModeCommand));
    }

}
