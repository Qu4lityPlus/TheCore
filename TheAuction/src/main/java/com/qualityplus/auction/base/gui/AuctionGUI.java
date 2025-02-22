package com.qualityplus.auction.base.gui;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.GUI;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.inventory.SimpleGUI;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.player.PlayerUtils;
import com.qualityplus.assistant.util.time.RemainingTime;
import com.qualityplus.assistant.util.time.TimeUtils;
import com.qualityplus.auction.api.box.Box;
import com.qualityplus.auction.persistence.data.AuctionBid;
import com.qualityplus.auction.persistence.data.AuctionItem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Utility class for auction gui
 */
public abstract class AuctionGUI extends GUI {
    protected final Box box;
    protected UUID uuid;

    /**
     * Makes a actions gui
     *
     * @param size  Size
     * @param title Title
     * @param box   {@link Box}
     */
    public AuctionGUI(final int size, final String title, final Box box) {
        super(size, title);

        this.box = box;
    }

    /**
     * Makes a Auctions gui
     *
     * @param simpleGUI {@link SimpleGUI}
     * @param box       {@link Box}
     */
    public AuctionGUI(final SimpleGUI simpleGUI, final Box box) {
        super(simpleGUI);

        this.box = box;
    }

    /**
     * Adds content
     */
    public void addContent() { }

    protected String getRemainingTime(final AuctionItem auctionItem) {
        final RemainingTime time = auctionItem.getMarkable().getRemainingTime();

        return TimeUtils.getParsedTime(
                time,
                this.box.files().messages().getAuctionMessages().getAuctionEndsInFormat(),
                this.box.files().messages().getAuctionMessages().getDays(),
                this.box.files().messages().getAuctionMessages().getHours(),
                this.box.files().messages().getAuctionMessages().getMinutes(),
                this.box.files().messages().getAuctionMessages().getSeconds(),
                this.box.files().messages().getAuctionMessages().getNoTimeFormat(),
                this.box.files().messages().getAuctionMessages().isShowNoTimeSymbol()
        );
    }

    protected List<IPlaceholder> getAuctionItemPlaceholders(final AuctionItem auctionItem) {
        return PlaceholderBuilder.create(new Placeholder("auction_item_name", BukkitItemUtil.getName(auctionItem.getItemStack())),
                new Placeholder("auction_item_lore", BukkitItemUtil.getItemLore(auctionItem.getItemStack())),
                new Placeholder("auction_owner_name", auctionItem.getOwnerName()),
                new Placeholder("auction_bids_amount", auctionItem.getBidsWithoutOwner().size()),
                new Placeholder("auction_status", getStatusPlaceholder(auctionItem)))
                .with(getBidPlaceholders(auctionItem))
                .get();
    }

    protected List<IPlaceholder> getBidPlaceholders(final AuctionItem auctionItem) {
        return Arrays.asList(
                new Placeholder("bid_information", getBidInformation(auctionItem)),
                new Placeholder("auction_is_buy_it_now", auctionItem.isBuyItNow() ? this.box.files().messages().
                        getAuctionMessages().getAuctionIsBin() : this.box.files().messages().
                        getAuctionMessages().getAuctionIsNotBin()),
                new Placeholder("auction_is_your_bid", auctionItem.getOwner().equals(this.uuid) ? StringUtils.
                        color(this.box.files().messages().getAuctionMessages().getItsYourAuction()) : Collections.emptyList())
        );
    }

    protected String getStatusPlaceholder(final AuctionItem auctionItem) {
        if (auctionItem.isBuyItNow()) {
            if (auctionItem.getWhoBought() == null) {
                return auctionItem.isExpired() ? this.box.files().messages().getAuctionMessages().getExpiredStatus() :
                        this.box.files().messages().getAuctionMessages().getInProgressStatus()
                                .replace("%auction_remaining_time%", getRemainingTime(auctionItem));
            } else {
                return this.box.files().messages().getAuctionMessages().getBinAuctionEndedStatus();
            }
        } else {
            if (auctionItem.getWhoBought() == null) {
                return auctionItem.isExpired() ? this.box.files().messages().getAuctionMessages().getExpiredStatus() :
                        this.box.files().messages().getAuctionMessages().getInProgressStatus()
                                .replace("%auction_remaining_time%", getRemainingTime(auctionItem));
            } else {
                return this.box.files().messages().getAuctionMessages().getNormalAuctionEndedStatus();
            }
        }
    }

    /**
     * Make list history placeholders
     *
     * @param auctionItem {@link AuctionItem}
     * @return            Get History Placeholders
     */
    public List<String> getHistoryPlaceholders(final AuctionItem auctionItem) {
        final List<AuctionBid> withoutOwner = auctionItem.getBidsWithoutOwner();

        withoutOwner.sort((o1, o2) -> (int) (o1.getBidAmount() - o2.getBidAmount()));

        final List<String> toReturn = new ArrayList<>();

        for (AuctionBid auctionBid : withoutOwner) {

            final RemainingTime time = TimeUtils.getTimeWhenAgo(auctionBid.getTimeAgo());

            final String timeAgoMsg = TimeUtils.getParsedTime(
                    time, this.box.files().messages().getAuctionMessages().getAuctionHistoryTimeFormat(),
                    this.box.files().messages().getAuctionMessages().getDays(),
                    this.box.files().messages().getAuctionMessages().getHours(),
                    this.box.files().messages().getAuctionMessages().getMinutes(),
                    this.box.files().messages().getAuctionMessages().getSeconds(),
                    this.box.files().messages().getAuctionMessages().getNoTimeFormat(),
                    this.box.files().messages().getAuctionMessages().isShowNoTimeSymbol()
            );

            final List<IPlaceholder> placeholders1 = Arrays.asList(
                    new Placeholder("auction_bid_amount", auctionBid.getBidAmount()),
                    new Placeholder("auction_owner_name", PlayerUtils.getPlayerName(auctionBid.getBidder())),
                    new Placeholder("auction_history_bid_time", timeAgoMsg)
            );

            toReturn.addAll(StringUtils.processMulti(this.box.files().messages().getAuctionMessages().getBidHistoryFormat(), placeholders1));
        }

        return toReturn;
    }

    private List<String> getBidInformation(final AuctionItem auctionItem) {
        if (auctionItem.isBuyItNow()) {
            final List<IPlaceholder> placeholders = Arrays.asList(
                    new Placeholder("auction_buyer_name", PlayerUtils.getPlayerName(auctionItem.getWhoBought())),
                    new Placeholder("auction_top_bid", auctionItem.getHighestBid()),
                    new Placeholder("auction_starting_bid", auctionItem.getHighestBid())
            );
            return StringUtils.processMulti(auctionItem.getWhoBought() == null ? this.box.files().
                    messages().getAuctionMessages().getNotSoldYet() : this.box.files().messages().getAuctionMessages().getSoldFor(), placeholders);
        } else {
            if (auctionItem.getBids().size() == 1) {
                final double bid = auctionItem.getBid(auctionItem.getOwner())
                        .map(AuctionBid::getBidAmount)
                        .orElse(0D);

                return StringUtils.processMulti(this.box.files().messages().getAuctionMessages().getNoBidsInformation(),
                        Collections.singletonList(new Placeholder("auction_starting_bid", bid)));
            } else {

                final Optional<AuctionBid> bid = auctionItem.getBidsWithoutOwner()
                        .stream()
                        .max(Comparator.comparingInt(p -> (int) p.getBidAmount()));

                final double ownerBid = auctionItem.getBid(auctionItem.getOwner())
                        .map(AuctionBid::getBidAmount)
                        .orElse(0D);

                if (!bid.isPresent()) {
                    return Collections.emptyList();
                }

                return StringUtils.processMulti(this.box.files().messages().getAuctionMessages().getBidsInformation(), Arrays.asList(
                        new Placeholder("auction_starting_bid", ownerBid),
                        new Placeholder("auction_top_bid", bid.map(AuctionBid::getBidAmount).orElse(0D)),
                        new Placeholder("auction_bids_amount", auctionItem.getBidsWithoutOwner().size()),
                        new Placeholder("auction_bidder_name", PlayerUtils.getPlayerName(bid.map(AuctionBid::getBidder).orElse(null)))
                ));
            }
        }

    }

    protected List<AuctionItem> getAuctionsWherePlayerBid(final UUID uuid) {
        return this.box.auctionService().getItems().stream()
                .filter(auctionItem -> !auctionItem.getOwner().equals(uuid))
                .filter(auctionItem -> auctionItem.getBid(uuid).isPresent())
                .filter(auctionItem -> !isClaimed(auctionItem, uuid))
                .collect(Collectors.toList());
    }

    protected List<AuctionItem> getNotClaimedOwned(final UUID uuid) {
        return this.box.auctionService().getItems().stream()
                .filter(auctionItem -> auctionItem.getOwner().equals(uuid))
                .filter(auctionItem -> !isClaimed(auctionItem, uuid))
                .collect(Collectors.toList());
    }

    private boolean isClaimed(final AuctionItem auctionItem, final UUID uuid) {
        final Optional<AuctionBid> bid = auctionItem.getBid(uuid);

        return bid.isPresent() && bid.get().isClaimedBack();
    }

    /**
     * Adds item
     *
     * @param item {@link Item}
     */
    public void setItem(final Item item) {
        setItem(item, this.box.files().config().getLoreWrapper());
    }

    /**
     * Adds an  item
     *
     * @param item {@link Item}
     * @param placeholderList {@link IPlaceholder}
     */
    public void setItem(final Item item, final List<IPlaceholder> placeholderList) {
        setItem(item, placeholderList, this.box.files().config().getLoreWrapper());
    }
}
