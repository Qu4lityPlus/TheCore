package com.qualityplus.minions.base.config;

import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration(path = "messages.yml")
@Header("================================")
@Header("       Messages      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Messages extends OkaeriConfig {
    public PluginMessages pluginMessages = new PluginMessages();
    public MinionMessages minionMessages = new MinionMessages();


    public class MinionMessages extends OkaeriConfig {
        public String invalidItem = "&cInvalid Item!";
        public String invalidMinion = "&cInvalid Minion!";
        public String invalidAutoShip = "&cInvalid Automated Shipping!";
        public String invalidEgg = "&cInvalid Minion Egg!";
        public String invalidFuel = "&cInvalid Fuel!";
        public String invalidSkin = "&cInvalid Skin!";
        public String invalidUpgrade = "&cInvalid Upgrade!";
        public String canBeCraftedMinion = "&eClick to view recipe!";
        public String cantBeCraftedMinion = "&cThis minion can't be crafted!";
        public String cantBeCraftedMinionMessage = "&cYou can't craft this Minion!";
        public List<String> upgradeMaxLevelPlaceholder = Collections.singletonList("&aMax Level has been reached!");
        public List<String> upgradeStatusPlaceholder = Arrays.asList("&7Time Between Actions: %minion_time_between_actions_upgrade%", "&7Max Storage: %minion_max_storage_upgrade%");

        public String maxStorageWithDifferencePlaceholder = "&8%minion_max_storage% &8➜ &e%minion_next_level_max_storage%";
        public String maxStorageWithoutDifferencePlaceholder = "&e%minion_next_level_max_storage%";

        public String timeBetweenWithDifferencePlaceholder = "&8%minion_time_between_actions% &8➜ &a%minion_next_level_time_between_actions%";
        public String timeBetweenWithoutDifferencePlaceholder = "&a%minion_next_level_time_between_actions%";

        public String canUpgradeNoMaterials = "&cYou need &6%minion_upgrade_material_amount% &cmore %minion_upgrade_material_name%!";
        public String canUpgradeNoMoney = "&cYou need &6%minion_upgrade_money_amount% &cmore coins!";
        public String canUpgradeMaxLevel = "&cMax Tier reached!";
        public String canUpgrade = "&eClick to upgrade!";


        public String upgradeNoMaterialsMessage = "&cYou dont have enough items to craft that tier! You need &6%minion_upgrade_material_amount% &cmore. &7(%minion_upgrade_current_amount%/%minion_upgrade_required_amount%)";
        public String upgradeNoMoneyMessage = "&cYou need &6%minion_upgrade_money_amount% &cmore coins!";
        public String upgradeMaxLevelMessage = "&cYou already have the max tier for this minion!";
        public String upgradeMessage = "&aYou have upgraded your minion to Tier %minion_level_roman%!";

        public String fuelEndsInFormat = "%days_placeholder% %hours_placeholder% %minutes_placeholder% %seconds_placeholder%";
        public String fuelHistoryTimeFormat = "%days_placeholder% %hours_placeholder% %minutes_placeholder% %seconds_placeholder% ago";

        public String pickUpMinion = "&aYou picked up a minion! You currently have %minions_placed_amount% out of a maximum of %minions_max_amount_to_place% minions placed.";
        public String youPlacedAMinion = "&bYou placed a minion! (%minions_placed_amount%/%minions_max_amount_to_place%)";
        public String youCanOnlyPlaceAMaxOf = "&cYou can only can place a max of %minions_max_amount_to_place% minions!";

        public String days = "%days%d";
        public String hours = "%hours%h";
        public String minutes = "%minutes%m";
        public String seconds = "%seconds%s";
        public String noTimeFormat = "&b-";
        public boolean showNoTimeSymbol = true;
    }

    public class PluginMessages extends OkaeriConfig {
        public String successfullyReloaded = "&aPlugin has been reloaded successfully!";

        public String invalidPlayer = "&cInvalid Player!";

        public String useSyntax = "&cUsage: %usage%!";

        public String noPermission = "&eYou don't have permission to do that!";
        public String unknownCommand = "&cUnknown command!";
        public String mustBeAPlayer = "&cYou must be a player to do that!";
        public String invalidArguments = "&cInvalid Arguments!";
        public String invalidAmount = "&cInvalid Amount!";
        public String invalidLevel = "&cInvalid level!";
        public String useHelp = "&cUse: /theminions help";

        public String helpMessage = "&7%command% - &e%description%";
        public String helpHeader = "      &6&lTheMinions   ";
        public String helpfooter = "&e<< &6Page %page% of %maxpage% &e>>";
        public String previousPage = "<<";
        public String nextPage = ">>";
        public String helpPageHoverMessage = "Click to go to page %page%";
    }


}
