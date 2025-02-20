package com.qualityplus.auction.base.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.inventory.CommonGUI;
import com.qualityplus.assistant.inventory.background.Background;
import com.qualityplus.assistant.inventory.background.DefaultBackgrounds;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.CustomKey;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.auction.base.gui.all.AllAuctionsGUIConfig;
import com.qualityplus.auction.base.gui.confirm.ConfirmAuctionGUIConfig;
import com.qualityplus.auction.base.gui.create.CreateAuctionGUIConfig;
import com.qualityplus.auction.base.gui.main.MainAuctionGUIConfig;
import com.qualityplus.auction.base.gui.manage.ManageAuctionGUIConfig;
import com.qualityplus.auction.base.gui.pending.PendingAuctionGUIConfig;
import com.qualityplus.auction.base.gui.stats.AuctionStatsGUIConfig;
import com.qualityplus.auction.base.gui.time.AuctionTimeConfigGUI;
import com.qualityplus.auction.base.gui.view.bin.BinAuctionViewGUIConfig;
import com.qualityplus.auction.base.gui.view.normal.NormalAuctionViewGUIConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;

/**
 * Utility class for inventories
 */
@Getter
@Configuration(path = "inventories.yml")
@Header("================================")
@Header("       Inventories      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Inventories extends OkaeriConfig implements DefaultBackgrounds {
    @CustomKey("auctionGUIConfig")
    private MainAuctionGUIConfig auctionGUIConfig = MainAuctionGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Auction House",
                    36,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  31, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .auctionsBrowser(ItemBuilder.of(XMaterial.GOLD_BLOCK, 11, 1, "&6Auctions Browser",
                    Arrays.asList("&7Find items for sale by players", "&7across Hypixel Skyblock!", "", "&7Items offered here are for",
                            "&6auction&7, meaning you have to",
                            "&7place the top bid to acquire",
                            "&7them!", "", "&eClick to browse!")).build())
            .viewBidsEmpty(ItemBuilder.of(XMaterial.GOLDEN_CARROT, 13, 1, "&eView Bids",
                    Arrays.asList("&7You don't have any outstanding", "&7bids.", "", "&7Use the &6Auctions Browser &7to", "&7find some cool items.")).build())
            .viewBids(ItemBuilder.of(XMaterial.GOLDEN_CARROT, 13, 1, "&eView Bids",
                    Arrays.asList("", "&b* You have %auction_pending_amount% items/coins to pickup!", "", "&eClick to view!")).build())

            .createAnAuction(ItemBuilder.of(XMaterial.GOLDEN_HORSE_ARMOR, 15, 1, "&aCreate Auction",
                    Arrays.asList("&7Set your own items on auction", "&7for other players to purchase.", "", "&eClick to become rich!")).build())
            .manageAuctions(ItemBuilder.of(XMaterial.GOLDEN_HORSE_ARMOR, 15, 1, "&aManage Auctions",
                    Arrays.asList("&7You own &e%auction_bids_amount% auction &7in", "&7progress or which recently", "&7ended.", "",
                            "&7Player can find your auctions",
                            "&7using the &6Auctions Browser",
                            "&7or typing &a/ah",
                            "&a%player%&7!", "", "&eClick to manage!")).build())
            .auctionStats(ItemBuilder.of(XMaterial.MAP, 32, 1, "&aAuction Stats",
                    Arrays.asList("&7View various statistics about", "&7you and the Auction House.", "", "&eClick to view!")).build())
            .build();

    @CustomKey("manageAuctionGUINewConfig")
    private ManageAuctionGUIConfig manageAuctionGUINewConfig = ManageAuctionGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Manage Auctions",
                    36,
                    getBackgroundWith4RowsDecoratedAround(),
                    ItemBuilder.of(XMaterial.BARRIER,  31, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))
            .auctionItem(ItemBuilder.of(XMaterial.STONE, 1, "&f%auction_item_name%",
                    Arrays.asList("%auction_item_lore%",
                            "",
                            "&e————————————————————",
                            "&7Seller: %auction_owner_name%",
                            "%bid_information%",
                            "",
                            "%auction_is_your_bid%",
                            "%auction_status%",
                            "",
                            "&6» &eClick to inspect")).build())
            .goBackItem(ItemBuilder.of(XMaterial.ARROW,  31, 1, "&aGo Back", Arrays.asList("&7To Auction House")).build())
            .sortItem(ItemBuilder.of(XMaterial.HOPPER, 32, 1, "&eSort",
                    Arrays.asList("&7%auction_browser_is_by_recently_updated%Recently Updated",
                            "&7%auction_browser_is_by_highest_bid%Highest Bid",
                            "&7%auction_browser_is_by_most_bid%Most Bid")).build())
            .createAnAuction(ItemBuilder.of(XMaterial.GOLDEN_HORSE_ARMOR, 33, 1, "&aCreate Auction",
                    Arrays.asList("&7Set your own items on auction", "&7for other players to purchase.", "", "&eClick to become rich!")).build())

            .nextPageItem(ItemBuilder.of(XMaterial.ARROW,  35, 1, "&dNext Page", Arrays.asList("", "&e» Click to go next Page!")).build())
            .previousPageItem(ItemBuilder.of(XMaterial.ARROW, 27, 1, "&dBack Page", Arrays.asList("", "&e» Click to go to back Page!")).build())
            .auctionSlots(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25))
            .build();

    @CustomKey("confirmAuctionGUIConfig")
    private ConfirmAuctionGUIConfig confirmAuctionGUIConfig = ConfirmAuctionGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Confirm Purchase",
                    27,
                    new Background(),
                    ItemBuilder.of(XMaterial.BARRIER,  31, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))
            .auctionItem(ItemBuilder.of(XMaterial.STONE, 13, 1, "&e&l&nBUYING ITEM:",
                    Arrays.asList("&f%auction_item_name%", "%auction_item_lore%")).build())
            .confirmItem(ItemBuilder.of(XMaterial.GREEN_TERRACOTTA,  11, 1, "&aConfirm",
                    Arrays.asList("&7Purchasing: &f%auction_item_name%", "&7Cost: &6%auction_to_confirm_price% Coins")).build())
            .cancelItem(ItemBuilder.of(XMaterial.RED_TERRACOTTA, 15, 1, "&cCancel",
                    Collections.emptyList()).build())
            .build();

    @CustomKey("pendingAuctionGUIConfig")
    private PendingAuctionGUIConfig pendingAuctionGUIConfig = PendingAuctionGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Your bids",
                    36,
                    getBackgroundWith4RowsDecoratedAround(),
                    ItemBuilder.of(XMaterial.BARRIER,  31, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))
            .auctionItem(ItemBuilder.of(XMaterial.STONE, 1, "&f%auction_item_name%",
                    Arrays.asList("%auction_item_lore%",
                            "",
                            "&e————————————————————",
                            "&7Seller: %auction_owner_name%",
                            "%bid_information%",
                            "",
                            "%auction_is_your_bid%",
                            "%auction_status%",
                            "",
                            "&6» &eClick to inspect")).build())
            .goBackItem(ItemBuilder.of(XMaterial.ARROW,  31, 1, "&aGo Back", Arrays.asList("&7To Auction House")).build())

            .nextPageItem(ItemBuilder.of(XMaterial.ARROW,  35, 1, "&dNext Page", Arrays.asList("", "&e» Click to go next Page!")).build())
            .previousPageItem(ItemBuilder.of(XMaterial.ARROW, 27, 1, "&dBack Page", Arrays.asList("", "&e» Click to go to back Page!")).build())
            .auctionSlots(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25))
            .build();

    @CustomKey("auctionStatsGUIConfig")
    private AuctionStatsGUIConfig auctionStatsGUIConfig = AuctionStatsGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Your stats",
                    36,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  31, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))
            .buyerStatsItem(ItemBuilder.of(XMaterial.MAP, 11, 1, "&6Buyer Stats:",
                    Arrays.asList("&7Auctions Created: &6%auctions_data_auctions_created%",
                            "&7Auctions completed with bids: &6%auctions_data_auctions_completed_with_bids%",
                            "&7Auctions completed without bids: &6%auctions_data_auctions_completed_without_bids%",
                            "",
                            "&7Total money earned: &6%auctions_data_total_money_earned%",
                            "&7Money spent on fees: &6%auctions_data_total_money_spent_on_fees%"
                    )).build())
            .sellerStatsItem(ItemBuilder.of(XMaterial.FILLED_MAP, 15, 1, "&aSeller Stats:",
                    Arrays.asList("&7Auctions won: &a%auctions_data_auctions_won%",
                            "&7Total bids: &a%auctions_data_total_bids%",
                            "&7Highest Bid: &a%auctions_data_highest_bid%",
                            "",
                            "&7Total money spent: &a%auctions_data_total_money_spent%")).build())
            .goBackItem(ItemBuilder.of(XMaterial.ARROW,  31, 1, "&aGo Back", Collections.singletonList("&7To Auction House")).build())

            .build();

    @CustomKey("allAuctionsGUIConfig")
    private AllAuctionsGUIConfig allAuctionsGUIConfig = AllAuctionsGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Auction Browser",
                    54,
                    new Background(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build())
            )
            .nextPage(ItemBuilder.of(XMaterial.BOOK,  53, 1, "&dNext Page", Arrays.asList("", "&e» Click to go next Page!")).build())
            .previousPage(ItemBuilder.of(XMaterial.BOOK, 46, 1, "&dBack Page", Arrays.asList("", "&e» Click to go to back Page!")).build())
            .goBack(ItemBuilder.of(XMaterial.ARROW, 49, 1, "&aGo back", Arrays.asList("", "&e» Click to go to back!")).build())
            .auctionSlots(Arrays.asList(11, 12, 13, 14, 15, 16, 20, 21, 22, 23, 24, 25, 29, 30, 31, 32, 33, 34, 38, 39, 40, 41, 42, 43))
            .auctionItem(ItemBuilder.of(XMaterial.STONE, 1, "&f%auction_item_name%",
                    Arrays.asList("%auction_item_lore%",
                            "",
                            "&e————————————————————",
                            "&7Seller: %auction_owner_name%",
                            "%bid_information%",
                            "",
                            "%auction_is_your_bid%",
                            "%auction_status%",
                            "",
                            "&6» &eClick to make a offer")).build())
            .sortItem(ItemBuilder.of(XMaterial.HOPPER, 50, 1, "&aSort", Arrays.asList(
                    "",
                    "&7%auction_browser_is_by_highest%Highest Price",
                    "&7%auction_browser_is_by_lowest%Lowest Price",
                    "&7%auction_browser_is_by_ending_soon%Ending Soon",
                    "&7%auction_browser_is_by_random%Random",
                    "",
                    "&eClick to switch sort!")).build())
            .binFilterItem(ItemBuilder.of(XMaterial.GOLD_INGOT, 52, 1, "&aBIN Filter", Arrays.asList(
                    "",
                    "&7%auction_browser_show_all%Show All",
                    "&7%auction_browser_bin_only%BIN Only",
                    "&7%auction_browser_auctions_only%Auctions Only",
                    "",
                    "&eClick to switch filter!")).build())
            .byNameFilterEmptyItem(ItemBuilder.of(XMaterial.OAK_SIGN, 48, 1, "&aSearch", Arrays.asList(
                    "&7Find items by name, type, lore",
                    "&7or enchants.",
                    "",
                    "&eClick to search")).build())

            .byNameFilterFilledItem(ItemBuilder.of(XMaterial.OAK_SIGN, 48, 1, "&aSearch", Arrays.asList(
                    "&7Find items by name, type, lore",
                    "&7or enchants.",
                    "",
                    "&7Filtered: &e%auction_by_name_search%",
                    "",
                    "&bRight-Click to clear!",
                    "&eClick to edit filter!")).build())
            .resetSettings(ItemBuilder.of(XMaterial.ANVIL, 47, 1, "&aReset Settings", Arrays.asList(
                    "&7Clears your auction browsing",
                    "&7settings except BIN Filter.",
                    "",
                    "&eClick to clear!")).build())
            .categoryItem(ItemBuilder.of(XMaterial.STONE, 1, "%auction_category_displayname%", Arrays.asList(
                    "&8Category",
                    "",
                    "%auction_category_lore%",
                    "",
                    "%auction_is_current_category%")).build())
            .build();

    @CustomKey("createAuctionGUIConfig")
    private CreateAuctionGUIConfig createAuctionGUIConfig = CreateAuctionGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Create Auction",
                    54,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))
            .currentItemEmpty(ItemBuilder.of(XMaterial.STONE_BUTTON, 13, 1, "&eClick an item in your inventory!",
                    Collections.singletonList("&7Selects it for auction")).build())
            .currentItemFilled(ItemBuilder.of(XMaterial.STONE, 13, 1, "&eClick an item in your inventory!",
                    Arrays.asList("&a&l&nAUCTION FOR ITEM:", "", "&f%auction_item_name%", "%auction_item_lore%", "", "&eClick to pickup!")).build())
            .createAuctionEmpty(ItemBuilder.of(XMaterial.RED_TERRACOTTA,  29, 1, "&cCreate %auction_is_buy_it_now_placeholder%",
                    Arrays.asList("&7No item selected!", "", "&7Click an item in your inventory", "&7to select it for this auction.")).build())
            .createAuctionFilled(ItemBuilder.of(XMaterial.GREEN_TERRACOTTA,  29, 1, "&aCreate %auction_is_buy_it_now_placeholder%",
                    Arrays.asList("&7This Item will be added to the", "&7auction house for other players",
                            "&7to purchase", "",
                            "&7Item: %auction_item_name%",
                            "&7Auction duration: %auction_duration%",
                            "&7Starting bid: &6%auction_starting_bid% coins",
                            "",
                            "&7Creation fee: &6%auction_creation_price% coins",
                            "",
                            "&eClick to submit"
                    )).build())
            .auctionInitialBid(ItemBuilder.of(XMaterial.POWERED_RAIL,  31, 1, "&fStarting bid: &6%auction_starting_bid% coins",
                    Arrays.asList("&7The minimun price a player can",
                            "&7offer to obtain your item.",
                            "",
                            "&7Once a player bids for your",
                            "&7item, other players will have",
                            "&7until the auction ends to make a",
                            "&7higher bid.",
                            "",
                            "&7Extra Fee: &6+%auction_fee% coins &e(5%)",
                            "",
                            "&eClick to edit")).build())
            .auctionDuration(ItemBuilder.of(XMaterial.CLOCK,  33, 1, "&fDuration: &e%auction_duration%",
                    Arrays.asList("&7How long players will be able to",
                            "&7place bigs for.",
                            "",
                            "&7Note: Bids automatically",
                            "&7increase the duration of",
                            "&7auctions.",
                            "",
                            "&7Extra Fee: &6+%auction_duration_fee% coins",
                            "",
                            "&eClick to edit")).build())
            .goBack(ItemBuilder.of(XMaterial.ARROW, 49, 1, "&eGo back",
                    Arrays.asList("", "&e» Click to go to back!")).build())
            .auctionItemPrice(ItemBuilder.of(XMaterial.GOLD_INGOT,  31, 1, "&fItem Price: &6%auction_starting_bid% coins",
                    Arrays.asList("&7The price at which you want to",
                            "&7sell this item.",
                            "",
                            "&7Extra Fee: &6+%auction_fee% coins &e(1%)",
                            "",
                            "&eClick to edit")).build())
            .switchToAuction(ItemBuilder.of(XMaterial.POWERED_RAIL,  48, 1, "&aSwitch to Auction",
                    Arrays.asList("&7With traditional auctions,",
                            "&7multiple buyers compete for the",
                            "&7item by bidding turn by turn.",
                            "",
                            "&eClick to switch!")).build())
            .switchToBin(ItemBuilder.of(XMaterial.GOLD_INGOT,  48, 1, "&aSwitch to BIN",
                    Arrays.asList("&7BIN Auctions are simple.",
                            "&7Set a price, then one player may",
                            "&7buy the item at that price.",
                            "",
                            "&8(BIN means Buy It Now)",
                            "",
                            "&eClick to switch!")).build())
            .build();

    @CustomKey("auctionTimeConfigGUI")
    private AuctionTimeConfigGUI auctionTimeConfigGUI = new AuctionTimeConfigGUI(
            new CommonGUI(
                    "&bCREATE AUCTION",
                    27,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  22, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ),
            ItemBuilder.of(XMaterial.STONE, 1, "&e%duration_format%",
                    Arrays.asList("&7Extra Fee: &6%duration_fee% coins", "", "&eClick to pick!")).build(),
            ItemBuilder.of(XMaterial.ARROW, 22, 1, "&eGo back",
                    Arrays.asList("", "&e» Click to go back!")).build(),
            Arrays.asList(10, 11, 12, 13, 14, 15, 16)
    );


    @CustomKey("submitGUIBuyerConfig")
    private NormalAuctionViewGUIConfig submitGUIBuyerConfig = NormalAuctionViewGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Auction Preview",
                    54,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))
            .auctionItem(ItemBuilder.of(XMaterial.STONE, 13, 1, "&f%auction_item_name%",
                    Arrays.asList("%auction_item_lore%",
                            "",
                            "&e————————————————————",
                            "&7Seller: %auction_owner_name%",
                            "%bid_information%",
                            "",
                            "%auction_is_your_bid%",
                            "%auction_status%")).build())
            .bidItem(ItemBuilder.of(XMaterial.GOLD_NUGGET, 29, 1, "&6Submit Bid",
                    Arrays.asList("", "&7New bid: &6%auction_new_bid% coins", "", "%auction_can_submit%")).build())
            .collectAuctionItem(ItemBuilder.of(XMaterial.GOLD_BLOCK, 29, 1, "&6Collect Auction",
                    Arrays.asList("", "&7Item sold to %auction_buyer_name%", "&7for &6%auction_top_bid% coins&7!", "", "&eClick to collect coins!")).build())

            .collectAuctionEmptyItem(ItemBuilder.of(XMaterial.GOLD_BLOCK, 29, 1, "&6Collect Auction",
                    Arrays.asList("", "&7No one has bid on your item.", "&aYou may pick it back up", "", "&eClick to píck up item!")).build())
            .collectItemAuctionItem(ItemBuilder.of(XMaterial.GOLD_BLOCK, 29, 1, "&6Collect Auction",
                    Arrays.asList("", "%auction_bid_status%", "%auction_can_collect_item%", "", "%auction_collect_item_placeholder%")).build())
            .bidAmount(ItemBuilder.of(XMaterial.GOLD_INGOT, 31, 1, "&fBid Amount: &6%auction_new_bid% coins",
                    Arrays.asList("&7You need to bid at least &6%auction_required_bid%",
                            "&6coins &7to hold the top big on",
                            "&7this auction.",
                            "&7The &etop bid &7on auction end",
                            "&7wins the item.",
                            "",
                            "&7If you do not win, you can claim",
                            "&7your bid coins back.",
                            "",
                            "&eClick to edit amount!"
                    )).build())
            .bidHistoryEmpty(ItemBuilder.of(XMaterial.MAP, 33, 1, "&fBid History",
                    Arrays.asList("&7No bids have been placed on this", "&7item yet.", "", "&7Be the first to bid on it!")).build())
            .bidHistoryFilled(ItemBuilder.of(XMaterial.FILLED_MAP, 33, 1, "&fBid History",
                    Arrays.asList("&7Total bids: &a%auction_bids_amount% bids", "%auction_bid_history%")).build())
            .goBack(ItemBuilder.of(XMaterial.ARROW, 49, 1, "&eGo back",
                    Arrays.asList("", "&e» Click to go back!")).build())
            .build();

    @CustomKey("binAuctionViewGUIConfig")
    private BinAuctionViewGUIConfig binAuctionViewGUIConfig = BinAuctionViewGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "BIN Auction Preview",
                    54,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))
            .auctionItem(ItemBuilder.of(XMaterial.STONE, 13, 1, "&f%auction_item_name%",
                    Arrays.asList("%auction_item_lore%",
                            "",
                            "&e————————————————————",
                            "&7Seller: %auction_owner_name%",
                            "%bid_information%",
                            "",
                            "%auction_is_your_bid%",
                            "%auction_status%")).build())
            .buyItNowItem(ItemBuilder.of(XMaterial.GOLD_NUGGET, 31, 1, "&6Buy Item Right Now",
                    Arrays.asList("", "&7Price: &6%auction_top_bid% coins", "", "%auction_can_submit%")).build())
            .ownBuyItNowItem(ItemBuilder.of(XMaterial.GOLD_NUGGET, 29, 1, "&6Buy Item Right Now",
                    Arrays.asList("", "&7Price: &6%auction_top_bid% coins", "", "&aThis is your own auction!")).build())
            .cancelAuctionItem(ItemBuilder.of(XMaterial.RED_TERRACOTTA, 33, 1, "&cCancel Auction",
                    Arrays.asList("", "&7You may cancel auction as", "&7long as they have &c0 &7bids!", "", "&eClick to cancel auction!")).build())
            .collectAuctionItem(ItemBuilder.of(XMaterial.GOLD_BLOCK, 31, 1, "&6Collect Auction",
                    Arrays.asList("", "&7Item sold to %auction_buyer_name%", "&7for &6%auction_top_bid% coins&7!", "", "&eClick to collect coins!")).build())

            .collectAuctionEmptyItem(ItemBuilder.of(XMaterial.GOLD_BLOCK, 29, 1, "&6Collect Auction",
                    Arrays.asList("", "&7No one has bid on your item.", "&aYou may pick it back up", "", "&eClick to píck up item!")).build())
            .collectItemAuctionItem(ItemBuilder.of(XMaterial.GOLD_BLOCK, 31, 1, "&6Collect Auction",
                    Arrays.asList("", "%auction_bid_status%", "%auction_can_collect_item%", "", "%auction_collect_item_placeholder%")).build())
            .goBack(ItemBuilder.of(XMaterial.ARROW, 49, 1, "&eGo back",
                    Arrays.asList("", "&e» Click to go back!")).build())
            .build();
}
