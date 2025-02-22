package com.qualityplus.bank.base.config;

import com.qualityplus.assistant.api.commands.details.CommandDetails;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
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
    public CommandDetails reloadCommand = new CommandDetails(Collections.singletonList("reload"), "Reload files", "/TheBank reload", "thebank.reload", true, Duration.ZERO.getSeconds(), true, "thebank");
    public CommandDetails bankCommand = new CommandDetails(Collections.singletonList("open"), "Open bank gui", "/TheBank open", "thebank.open", true, Duration.ZERO.getSeconds(), true, "thebank");
    public CommandDetails givePersonalBankCommand = new CommandDetails(Collections.singletonList("givepersonal"), "Give personal bank to players", "/TheBank givepersonal <player>", "thebank.givepersonal", true, Duration.ZERO.getSeconds(), true, "thebank");
    public CommandDetails helpCommand = new CommandDetails(Collections.singletonList("help"), "Show all commands", "/TheBank help", "thebank.help", true, Duration.ZERO.getSeconds(), true, "thebank");
    public CommandDetails depositBankCommand = new CommandDetails(Collections.singletonList("deposit"), "Deposit money from player bank", "/TheBank deposit <player> <amount>", "thebank.deposit", true, Duration.ZERO.getSeconds(), true, "thebank");
    public CommandDetails withdrawBankCommand = new CommandDetails(Collections.singletonList("withdraw"), "Withdraw money from player bank", "/TheBank withdraw <player> <amount>", "thebank.withdraw", true, Duration.ZERO.getSeconds(), true, "thebank");
    public CommandDetails resetBankCommand = new CommandDetails(Collections.singletonList("reset"), "Reset player bank", "/TheBank reset <player>", "thebank.reset", true, Duration.ZERO.getSeconds(), true, "thebank");
}
