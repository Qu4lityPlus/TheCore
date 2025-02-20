package com.qualityplus.auction.persistence.data;

import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Makes an auction items
 */
@NoArgsConstructor
@Getter @Setter
public final class AuctionItem extends OkaeriConfig {

    private UUID owner;
    private String ownerName;
    private ItemStack itemStack;
    private Markable markable;
    private String timerId;
    private List<AuctionBid> bids;
    private boolean isBuyItNow;
    private UUID whoBought;
    private Boolean hasBids;

    /**
     * Makes an Auctions item
     *
     * @param owner      {@link UUID}
     * @param ownerName  Owner Name
     * @param itemStack  {@link ItemStack}
     * @param markable   {@link Markable}
     * @param timer      Timer
     * @param bids       {@link AuctionBid}
     * @param isBuyItNow If Is Buy It Now
     */
    @Builder
    public AuctionItem(final UUID owner, final String ownerName, final ItemStack itemStack,
                       final Markable markable, final String timer,
                       final List<AuctionBid> bids, final boolean isBuyItNow) {
        this.owner = owner;
        this.ownerName = ownerName;
        this.itemStack = itemStack;
        this.markable = markable;
        this.timerId = timer;
        this.bids = bids;
        this.isBuyItNow = isBuyItNow;
    }

    /**
     * Makes a bought been
     *
     * @return a HasBeenBought
     */
    public boolean hasBeenBought() {
        return this.whoBought != null;
    }

    /**
     * Makes an items stack
     *
     * @return an {@link ItemStack}
     */
    public ItemStack getItemStack() {
        return Optional.ofNullable(this.itemStack)
                .map(ItemStack::clone)
                .orElse(null);
    }

    /**
     * Makes an auctions bids
     *
     * @param uuid {@link UUID}
     * @return a {@link AuctionBid}
     */
    public Optional<AuctionBid> getBid(final UUID uuid) {
        return this.bids.stream().filter(bid -> bid.getBidder().equals(uuid)).findFirst();
    }

    /**
     * Makes an owners without bids
     * @return list of {@link AuctionBid}
     */
    public List<AuctionBid> getBidsWithoutOwner() {
        return this.bids.stream().filter(bid -> !bid.getBidder().equals(this.owner)).collect(Collectors.toList());
    }

    /**
     * Makes an expired
     * @return a IsExpired
     */
    public boolean isExpired() {
        return this.markable.getRemainingTime().isZero();
    }


    /**
     * Adds a bid to an auction
     *
     * @param bid {@link AuctionBid}
     */
    public void addBid(final AuctionBid bid) {
        if (this.isBuyItNow) {
            Bukkit.getConsoleSender().sendMessage("Couldn't add bid to auction item");
            return;
        }
        this.bids.add(bid);
    }

    /**
     * Makes a Bids highest
     * @return a HighestBid
     */
    public double getHighestBid() {
        return new ArrayList<>(this.bids).stream()
                .min((o1, o2) -> (int) (o1.getBidAmount() - o2.getBidAmount()))
                .map(AuctionBid::getBidAmount)
                .orElse(0D);
    }

    /**
     * makes a Bids Highest
     * @param uuid {@link UUID}
     * @return a HighestBid
     */
    public double getHighestBid(final UUID uuid) {
        return new ArrayList<>(this.bids).stream()
                .min((o1, o2) -> (int) (o1.getBidAmount() - o2.getBidAmount()))
                .filter(bid -> bid.getBidder().equals(uuid))
                .map(AuctionBid::getBidAmount)
                .orElse(0D);
    }

    /**
     * Makes a bidders
     * @return a {@link UUID}
     */
    public Set<UUID> getBidders() {
        return this.bids.stream()
                .map(AuctionBid::getBidder)
                .filter(bidder -> !bidder.equals(this.owner))
                .collect(Collectors.toSet());
    }

    /**
     * Makes a Owner uuid
     * @param uuid {@link UUID}
     * @return an IsOwner
     */
    public boolean isOwner(final UUID uuid) {
        return this.owner.equals(uuid);
    }
}
