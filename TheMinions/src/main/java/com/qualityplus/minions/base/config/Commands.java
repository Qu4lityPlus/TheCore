package com.qualityplus.minions.base.config;

import com.qualityplus.assistant.api.commands.details.CommandDetails;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Exclude;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;

@Configuration(path = "commands.yml")
@Header("================================")
@Header("       Commands      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Commands extends OkaeriConfig {
    public CommandDetails reloadCommand = new CommandDetails(Collections.singletonList("reload"), "Reload files", "/TheMinions reload", "theminions.reload", true, Duration.ZERO.getSeconds(), true, "theminions");
    public CommandDetails giveMinionCommand = new CommandDetails(Collections.singletonList("giveminion"), "Give Minion to players", "/TheMinions giveminion <player> <id> [<level>]", "theminions.giveminion", true, Duration.ZERO.getSeconds(), true, "theminions");
    public CommandDetails giveAutomatedShippingCommand = new CommandDetails(Collections.singletonList("giveautoship"), "Give Automated Shipping to players", "/TheMinions giveautoship <player> <id>", "theminions.giveautoship", true, Duration.ZERO.getSeconds(), true, "theminions");
    public CommandDetails giveFuelCommand = new CommandDetails(Collections.singletonList("givefuel"), "Give Fuel to players", "/TheMinions givefuel <player> <id>", "theminions.giveautoship", true, Duration.ZERO.getSeconds(), true, "theminions");
    public CommandDetails giveUpgradeCommand = new CommandDetails(Collections.singletonList("giveupgrade"), "Give Upgrade to players", "/TheMinions giveupgrade <player> <id>", "theminions.giveautoship", true, Duration.ZERO.getSeconds(), true, "theminions");

    public CommandDetails giveSkinCommand = new CommandDetails(Collections.singletonList("giveskin"), "Give minion skins to players", "/TheMinions giveskin <player> <id>", "theminions.giveskin", true, Duration.ZERO.getSeconds(), true, "theminions");
    public CommandDetails helpCommand = new CommandDetails(Collections.singletonList("help"), "Show all commands", "/TheMinions help", "theminions.help", true, Duration.ZERO.getSeconds(), true, "theminions");
    @Exclude
    public CommandDetails testCommand = new CommandDetails(Collections.singletonList("test"), "Test Purpose commands", "/TheMinions test", "theminions.test", true, Duration.ZERO.getSeconds(), true, "theminions");


    public CommandDetails setUpgradeItemToGiveCommand = new CommandDetails(Collections.singletonList("setupgradeitemtogive"), "Set the item that a upgrade will give for specific minion", "/TheMinions setupgradeitemtogive <minion> <upgrade>", "theminions.setupgradeitemtogive", true, Duration.ZERO.getSeconds(), true, "theminions");
    public CommandDetails setUpgradeRequiredItemCommand = new CommandDetails(Collections.singletonList("setupgraderequireditem"), "Set the item that a upgrade requires to create a new item", "/TheMinions setupgraderequireditem <minion> <upgrade>", "theminions.setupgraderequireditem", true, Duration.ZERO.getSeconds(), true, "theminions");
    public CommandDetails setDropItemCommand = new CommandDetails(Collections.singletonList("setdropitem"), "Set the item that a minion will drop", "/TheMinions setdropitem <minion>", "theminions.setdropitem", true, Duration.ZERO.getSeconds(), true, "theminions");

}
