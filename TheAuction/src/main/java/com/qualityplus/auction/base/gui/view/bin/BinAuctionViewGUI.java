package com.qualityplus.auction.base.gui.view.bin;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.player.PlayerUtils;
import com.qualityplus.auction.api.box.Box;
import com.qualityplus.auction.base.gui.AuctionGUI;
import com.qualityplus.auction.base.gui.all.AllAuctionsGUI;
import com.qualityplus.auction.base.gui.confirm.ConfirmAuctionGUI;
import com.qualityplus.auction.base.gui.manage.ManageAuctionGUI;
import com.qualityplus.auction.base.gui.manage.sort.ManageAuctionSortType;
import com.qualityplus.auction.base.searcher.AuctionSearcher;
import com.qualityplus.auction.persistence.data.AuctionBid;
import com.qualityplus.auction.persistence.data.AuctionItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Utility class for bin auction
 */
public final class BinAuctionViewGUI extends AuctionGUI {
    private final BinAuctionViewGUIConfig config;
    private final AuctionSearcher searcher;
    private final AuctionItem auctionItem;

    /**
     * makes a bin auction view
     *
     * @param boxUtil     {@link Box}
     * @param uuid        {@link UUID}
     * @param auctionItem {@link AuctionItem}
     * @param searcher    {@link AuctionSearcher}
     */
    public BinAuctionViewGUI(final Box boxUtil, final UUID uuid, final AuctionItem auctionItem, final AuctionSearcher searcher) {
        super(boxUtil.files().inventories().getBinAuctionViewGUIConfig(), boxUtil);

        this.config = boxUtil.files().inventories().getBinAuctionViewGUIConfig();
        this.auctionItem = auctionItem;
        this.searcher = searcher;
        this.uuid = uuid;
    }

    @Override
    public @NotNull Inventory getInventory() {
        addContent();

        return inventory;
    }

    @Override
    public void addContent() {
        fillInventory(this.config);

        final List<IPlaceholder> placeholders = getPlaceholders();

        inventory.setItem(this.config.getAuctionItem().getSlot(), ItemStackUtils.makeItem(this.config.getAuctionItem(), PlaceholderBuilder.create(placeholders)
                .with(getBidPlaceholders(this.auctionItem))
                .get(), this.auctionItem.getItemStack()));

        if (isOwnAuction()) {
            if (!this.auctionItem.isExpired() && this.auctionItem.getWhoBought() == null) {
                setItem(this.config.getCancelAuctionItem(), placeholders);
                setItem(this.config.getOwnBuyItNowItem(), placeholders);
            } else {
                if (this.auctionItem.getWhoBought() == null) {
                    setItem(this.config.getCollectAuctionEmptyItem(), placeholders);
                } else {
                    setItem(this.config.getCollectAuctionItem(), placeholders);
                }
            }
        } else {
            if (this.auctionItem.getWhoBought() == null && !this.auctionItem.isExpired()) {
                setItem(this.config.getBuyItNowItem(), PlaceholderBuilder.create(placeholders)
                        .get());
            } else if (this.auctionItem.getWhoBought().equals(uuid)) {
                setItem(this.config.getCollectItemAuctionItem(), PlaceholderBuilder.create(placeholders)
                        .with(getCollectItemPlaceholders())
                        .get());
            }
        }


        setItem(this.config.getCloseGUI());
        setItem(this.config.getGoBack());
    }

    private List<IPlaceholder> getCollectItemPlaceholders() {
        final double ownBid = getPlayerHighest(uuid)
                .map(AuctionBid::getBidAmount)
                .orElse(0D);

        final List<IPlaceholder> placeholders = Arrays.asList(new Placeholder("auction_own_bid", ownBid), new Placeholder("auction_top_bid", getTopPrice()));

        final String status = StringUtils.processMulti(box.files().messages().getAuctionMessages().getYouPaidMinimumPrice(), placeholders);
        final String canCollect = box.files().messages().getAuctionMessages().getYouMayCollectItem();
        final String auctionCollectItem = box.files().messages().getAuctionMessages().getClickToPickupItem();

        return Arrays.asList(
                new Placeholder("auction_bid_status", status),
                new Placeholder("auction_can_collect_item", canCollect),
                new Placeholder("auction_collect_item_placeholder", auctionCollectItem)
        );
    }

    private boolean isTopBidder() {
        return uuid.equals(this.auctionItem.getBids().stream()
                .max(Comparator.comparingDouble(AuctionBid::getBidAmount))
                .map(AuctionBid::getBidder)
                .orElse(null));
    }

    private boolean isTopBinner() {
        return uuid.equals(this.auctionItem.getBids()
                .stream()
                .filter(bid -> !bid.getBidder().equals(this.auctionItem.getOwner()))
                .max(Comparator.comparingDouble(AuctionBid::getBidAmount))
                .map(AuctionBid::getBidder)
                .orElse(null));
    }

    protected Optional<AuctionBid> getPlayerHighest(final UUID uuid) {
        return this.auctionItem.getBids()
                .stream()
                .max(Comparator.comparingDouble(AuctionBid::getBidAmount))
                .filter(auctionItem -> auctionItem.getBidder().equals(uuid));
    }

    private void markAndRemoveIfNeeded() {
        this.auctionItem.getBids().stream().filter(bid -> bid.getBidder().equals(uuid)).forEach(bid -> bid.setClaimedBack(true));

        final int size = (int) this.auctionItem.getBids().stream().filter(AuctionBid::isClaimedBack).count();

        if (size != this.auctionItem.getBids().size()) {
            return;
        }

        box.auctionService().getAuctionHouse().ifPresent(auctionHouse -> auctionHouse.getNormalItems().remove(this.auctionItem));
    }


    @Override
    public void onInventoryClick(final InventoryClickEvent event) {
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();

        final int slot = event.getSlot();

        if (isItem(slot, this.config.getCloseGUI())) {
            player.closeInventory();
        } else if (isOwnAuction() ) {

            if (isItem(slot, this.config.getGoBack())) {
                player.openInventory(new ManageAuctionGUI(box, player.getUniqueId(), this.searcher, 1, ManageAuctionSortType.RECENTLY_UPDATED).getInventory());

            } else if (isItem(slot, this.config.getCancelAuctionItem())) {
                if (!this.auctionItem.getBidsWithoutOwner().isEmpty()) {
                    final String message = Optional.ofNullable(box.files().messages().getAuctionMessages().getCantCancelAuctionDueToMoreThanOneBid())
                            .orElse("&cYou can't cancel this auction due that this has more than one bid!");
                    player.sendMessage(StringUtils.color(message));
                    return;
                }
                player.closeInventory();

                final double topPrice = getTopPrice();

                final List<IPlaceholder> placeholders = Arrays.asList(
                        new Placeholder("auction_item_name", BukkitItemUtil.getName(this.auctionItem.getItemStack())),
                        new Placeholder("auction_buyer_name", PlayerUtils.getPlayerName(this.auctionItem.getWhoBought())),
                        new Placeholder("auction_top_bid", topPrice)
                );

                if (this.auctionItem.getWhoBought() == null) {
                    player.sendMessage(StringUtils.processMulti(box.files().messages().getAuctionMessages().getPickUpBack(), placeholders));
                    player.getInventory().addItem(this.auctionItem.getItemStack().clone());
                } else {
                    player.sendMessage(StringUtils.processMulti(box.files().messages().getAuctionMessages().getCollectedMoney(), placeholders));
                    TheAssistantPlugin.getAPI().getAddons().getEconomy().depositMoney(player, topPrice);
                }

                markAndRemoveIfNeeded();
            } else if (
                    (isItem(slot, this.config.getCollectAuctionEmptyItem())) && this.auctionItem.isExpired()
            ) {
                //Esto es para agarrar las monedas o el item en caso de que nadie lo haya comprado (seller side)
                if (this.auctionItem.getWhoBought() == null && !this.auctionItem.isExpired()) {
                    return;
                }

                player.closeInventory();

                final double topPrice = getTopPrice();

                final List<IPlaceholder> placeholders = Arrays.asList(
                        new Placeholder("auction_item_name", BukkitItemUtil.getName(this.auctionItem.getItemStack())),
                        new Placeholder("auction_buyer_name", PlayerUtils.getPlayerName(this.auctionItem.getWhoBought())),
                        new Placeholder("auction_top_bid", topPrice)
                );

                if (this.auctionItem.getWhoBought() == null) {
                    player.sendMessage(StringUtils.processMulti(box.files().messages().getAuctionMessages().getPickUpBack(), placeholders));
                    player.getInventory().addItem(this.auctionItem.getItemStack().clone());
                } else {
                    player.sendMessage(StringUtils.processMulti(box.files().messages().getAuctionMessages().getCollectedMoney(), placeholders));
                    TheAssistantPlugin.getAPI().getAddons().getEconomy().depositMoney(player, topPrice);
                }

                markAndRemoveIfNeeded();
            } else if (isItem(slot, this.config.getOwnBuyItNowItem())) {
                player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getYouCannotBidYourOwn()));
            }

        } else {

            if (isItem(slot, this.config.getCollectItemAuctionItem()) && this.auctionItem.getBid(uuid).isPresent()) {
                //Esto es para agarrar las monedas o el item en caso de que lo haya ganado (buyer side)
                player.closeInventory();

                if (isTopBinner()) {
                    player.getInventory().addItem(this.auctionItem.getItemStack().clone());
                    player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getClaimedAuctionItem()
                            .replace("%auction_owner_name%", this.auctionItem.getOwnerName())
                            .replace("%auction_item_name%", BukkitItemUtil.getName(this.auctionItem.getItemStack()))
                    ));
                } else {
                    final double ownBid = this.auctionItem.getBids()
                            .stream().map(AuctionBid::getBidAmount)
                            .max(Comparator.comparingDouble(auctionItem -> auctionItem))
                            .orElse(0D);

                    final List<IPlaceholder> placeholders = Arrays.asList(
                            new Placeholder("auction_owner_name", this.auctionItem.getOwnerName()),
                            new Placeholder("auction_own_bid", ownBid));

                    player.sendMessage(StringUtils.processMulti(box.files().messages().getAuctionMessages().getClaimedMoneyBack(), placeholders));

                    TheAssistantPlugin.getAPI().getAddons().getEconomy().depositMoney(player, ownBid);
                }

                markAndRemoveIfNeeded();
            } else if (isItem(slot, this.config.getBuyItNowItem())) {
                if (this.auctionItem.getMarkable().getRemainingTime().isZero() || this.auctionItem.hasBeenBought()) {
                    player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getAuctionExpired()));
                    return;
                }

                if (!canSubmit()) {
                    player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getYouCannotAffordIt()));
                    return;
                }

                final double newTopPrice = getTopPrice();

                player.openInventory(new ConfirmAuctionGUI(box, uuid, this.searcher, this.auctionItem, newTopPrice).getInventory());
            } else if (isItem(slot, this.config.getGoBack())) {
                player.openInventory(new AllAuctionsGUI(box, 1, player.getUniqueId(), this.searcher).getInventory());
            }

        }
    }


    private List<IPlaceholder> getPlaceholders() {
        final String canSubmit = isOwnAuction() ? box.files().messages().getAuctionMessages().getOwnAuction() :
                canSubmit() ? box.files().messages().getAuctionMessages().getCanSubmitBin() : box.files().messages().getAuctionMessages().getCannotSubmit();

        return Arrays.asList(
                new Placeholder("auction_owner_name", PlayerUtils.getPlayerName(uuid)),
                new Placeholder("auction_status", getStatusPlaceholder(this.auctionItem)),
                new Placeholder("auction_item_name", BukkitItemUtil.getName(this.auctionItem.getItemStack())),
                new Placeholder("auction_item_lore", BukkitItemUtil.getItemLore(this.auctionItem.getItemStack())),
                new Placeholder("auction_bids_amount", this.auctionItem.getBidsWithoutOwner().size()),
                new Placeholder("auction_bid_history", getHistoryPlaceholders(this.auctionItem)),
                new Placeholder("auction_buyer_name", PlayerUtils.getPlayerName(this.auctionItem.getWhoBought())),
                new Placeholder("auction_top_bid", getTopPrice()),
                new Placeholder("auction_can_submit", canSubmit)
        );
    }

    private boolean isOwnAuction() {
        return this.auctionItem.getOwner().equals(uuid);
    }

    private boolean canSubmit() {

        final double money = TheAssistantPlugin.getAPI().getAddons().getEconomy().getMoney(Bukkit.getOfflinePlayer(uuid));

        final double toCheckPrice = getTopPrice();

        return money >= toCheckPrice;
    }

    private double getTopPrice() {
        return this.auctionItem.getBids().stream()
                .max(Comparator.comparingDouble(AuctionBid::getBidAmount))
                .map(AuctionBid::getBidAmount)
                .orElse(0D);
    }
}
