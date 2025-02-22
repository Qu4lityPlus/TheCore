package com.qualityplus.auction.base.config;

import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.util.time.HumanTime;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Utility class for messages
 */
@Getter
@Configuration(path = "messages.yml")
@Header("================================")
@Header("       Messages      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Messages extends OkaeriConfig {
    private PluginMessages pluginMessages = new PluginMessages();
    private AuctionMessages auctionMessages = new AuctionMessages();


    /**
     * Utility class dor auction messages
     */
    @Getter
    @Setter
    public class AuctionMessages extends OkaeriConfig {
        private String cantCancelAuctionDueToMoreThanOneBid = "&cYou can't cancel this auction due that this has more than one bid!";
        private List<String> startingBid = Arrays.asList("", "^^^^^^^^^^^^^^", "Your auction", "starting bid");
        private List<String> submitBid = Arrays.asList("", "^^^^^^^^^^^^^^", "Enter an amount", "to bid");
        private List<String> enterQuery = Arrays.asList("", "", "^^^^^^^^^^^^^^", "Enter Query");


        private String itemDurationFormat = "&e%duration_time% %duration_type%";

        private String auctionDurationFormat = "&e%auction_duration_time% %auction_duration_type%";
        private String auctionStarted = "&eAuction started for %auction_item_name%&e!";

        private String auctionEndsInFormat = "%days_placeholder% %hours_placeholder% %minutes_placeholder% %seconds_placeholder%";
        private String auctionHistoryTimeFormat = "%days_placeholder% %hours_placeholder% %minutes_placeholder% %seconds_placeholder% ago";

        private String days = "%days%d";
        private String hours = "%hours%h";
        private String minutes = "%minutes%m";
        private String seconds = "%seconds%s";
        private String noTimeFormat = "&b-";
        private boolean showNoTimeSymbol = true;

        private Map<HumanTime.TimeType, String> timeFormat = ImmutableMap.<HumanTime.TimeType, String>builder()
                .put(HumanTime.TimeType.DAYS, "Days")
                .put(HumanTime.TimeType.MINUTES, "Minutes")
                .put(HumanTime.TimeType.SECONDS, "Seconds")
                .put(HumanTime.TimeType.HOURS, "Hours")
                .build();

        private String invalidAmount = "&cInvalid Amount";
        private String mustBeHigherThanMin = "&cAmount to bid must be higher than %auction_new_bid%!";

        private List<String> noBidsInformation = Collections.singletonList("&7%auction_is_buy_it_now%: &6%auction_starting_bid% coins");
        private List<String> bidsInformation = Arrays.
                asList("&7Bids: &a%auction_bids_amount% bids", "",
                        "&7Top bid: &6%auction_top_bid% coins", "&7Bidder: %auction_bidder_name%");

        private List<String> soldFor = Arrays.asList("&7Buyer: &a%auction_buyer_name%", "", "&7Sold for: &6%auction_top_bid% coins");
        private List<String> notSoldYet = Collections.singletonList("&7%auction_is_buy_it_now%: &6%auction_starting_bid% coins");


        private List<String> itsYourAuction = Arrays.asList("&aThis is your own auction!", "");

        private List<String> bidHistoryFormat = Arrays.
                asList("&8————————————————————", "&7Bid: &6%auction_bid_amount% coins",
                        "&7By: %auction_owner_name%", "&b%auction_history_bid_time%");

        private String ownAuction = "&aThis is your own auction!";
        private String canSubmitBin = "&eClick to purchase!";
        private String canSubmit = "&eClick to bid!";
        private String cannotSubmit = "&cCannot afford bid!";
        private String alreadyTopBid = "&aAlready Top Bid!";

        private String youCannotAffordIt = "&cYou cannot afford it!";
        private String youAlreadyHaveTheHighestBid = "&aYou already have the highest bid on this auction!";
        private String yourBidIsLowestThanTopBid = "&cYour bid is lowest than top bid!";

        private String youCannotBidYourOwn = "&aYou cannot bid in your own auction!";
        private String auctionExpired = "&cThat auction just expired!";
        private String bidPlaced = "&eBid of &6%bid_amount% coins &eplaced for %auction_item_name%!";
        private String successfullyBought = "&eYou claimed &a%auction_item_name% &efrom %auction_owner_name%'s auction!";

        private String currentlyBrowsing = "&aCurrently browsing!";
        private String currentlyNotBrowsing = "&eClick to view items!";

        private String sortSelected = "&b► ";
        private String sortNotSelected = "";

        private String auctionIsNotBin = "Starting bid";
        private String auctionIsBin = "Buy It Now";

        private String buyItNowPlaceholder = "BIN Auction";
        private String auctionPlaceholder = "Auction";

        private String inProgressStatus = "&7Ends in: &e%auction_remaining_time%";
        private String normalAuctionEndedStatus = "&7Status: &aEnded!";
        private String binAuctionEndedStatus = "&7Status: &aSold!";
        private String expiredStatus = "&7Status: &cExpired!";

        private String collectedMoney =
                "&eYou collected &6%auction_top_bid% coins &efrom selling %auction_item_name% &eto %auction_buyer_name% &ein an auction!";
        private String pickUpBack = "&eYou claimed &f%auction_item_name% &eback from your expired auction!";

        private String youPaidMinimumPrice = "&7You paid the minimum price of &6%auction_top_bid% coins&7!";

        private String youHadTheTop = "&7You had the top bid for &6%auction_top_bid% coins&7!";
        private String youHadNotTheTop = "&6%auction_own_bid% coins&7 couldn't reach top bid!";
        private String youMayCollectItem = "&7You may collect the item.";
        private String youMayCollectMoney = "&7You may collect the money.";
        private String clickToPickupItem = "&eClick to pick up item!";
        private String clickToPickupCoins = "&eClick to pick up coins!";
        private String claimedAuctionItem = "&eYou collected &a%auction_item_name% &efrom %auction_owner_name%'s auction!";
        private String claimedMoneyBack = "&eYou claimed back your &6%auction_own_bid% coins &efrom %auction_owner_name%'s auction!";

    }

    /**
     * Utility class for plugin messages
     */
    @Getter
    public class PluginMessages extends OkaeriConfig {
        private String successfullyReloaded = "&aPlugin has been reloaded successfully!";

        private String invalidPlayer = "&cInvalid Player!";

        private String useSyntax = "&cUsage: %usage%!";

        private String noPermission = "&eYou don't have permission to do that!";
        private String unknownCommand = "&cUnknown command!";
        private String mustBeAPlayer = "&cYou must be a player to do that!";
        private String invalidArguments = "&cInvalid Arguments!";
        private String invalidAmount = "&cInvalid Amount!";
        private String useHelp = "&cUse: /auction help";

        private String helpMessage = "&7%command% - &e%description%";
        private String helpHeader = "      &6&lTheAuction   ";
        private String helpfooter = "&e<< &6Page %page% of %maxpage% &e>>";
        private String previousPage = "<<";
        private String nextPage = ">>";
        private String helpPageHoverMessage = "Click to go to page %page%";
    }


}
