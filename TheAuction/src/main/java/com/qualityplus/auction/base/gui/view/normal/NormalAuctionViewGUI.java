package com.qualityplus.auction.base.gui.view.normal;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.de.rapha149.signgui.SignGUI;
import com.qualityplus.assistant.lib.de.rapha149.signgui.SignGUIFinishHandler;
import com.qualityplus.assistant.lib.de.rapha149.signgui.exception.SignGUIVersionException;
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
import com.qualityplus.auction.base.gui.view.ViewOpener;
import com.qualityplus.auction.base.searcher.AuctionSearcher;
import com.qualityplus.auction.base.sign.SignGUIAPI;
import com.qualityplus.auction.persistence.data.AuctionBid;
import com.qualityplus.auction.persistence.data.AuctionItem;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Utility class for normal auction view
 */
public final class NormalAuctionViewGUI extends AuctionGUI {
    private final NormalAuctionViewGUIConfig config;
    private final AuctionSearcher searcher;
    private final AuctionItem auctionItem;
    private double newCost = -1;

    /**
     * Makes a normal auction view gui
     *
     * @param boxUtil     {@link Box}
     * @param uuid        {@link UUID}
     * @param auctionItem {@link AuctionItem}
     * @param newCost     If New Cost
     * @param searcher    {@link AuctionSearcher}
     */
    public NormalAuctionViewGUI(final Box boxUtil, final UUID uuid, final AuctionItem auctionItem, final double newCost, final AuctionSearcher searcher) {
        super(boxUtil.files().inventories().getSubmitGUIBuyerConfig(), boxUtil);

        this.config = boxUtil.files().inventories().getSubmitGUIBuyerConfig();
        this.auctionItem = auctionItem;
        this.searcher = searcher;
        this.newCost = newCost;
        this.uuid = uuid;
    }


    @Override
    public @NotNull Inventory getInventory() {
        addContent();

        return inventory;
    }

    private List<IPlaceholder> getCollectItemPlaceholders() {
        final double ownBid = getPlayerHighestBid(uuid)
                .map(AuctionBid::getBidAmount)
                .orElse(0D);

        final List<IPlaceholder> placeholders = Arrays.asList(new Placeholder("auction_own_bid", ownBid), new Placeholder("auction_top_bid", getTopPrice()));

        final String status = StringUtils.processMulti(isTopBidder() ? box.files().messages().getAuctionMessages().getYouHadTheTop() : box.
                files().messages().getAuctionMessages().getYouHadNotTheTop(), placeholders);
        final String canCollect = isTopBidder() ? box.files().messages().
                getAuctionMessages().getYouMayCollectItem() : box.files().messages().getAuctionMessages().getYouMayCollectMoney();
        final String auctionCollectItem = isTopBidder() ? box.files().messages().getAuctionMessages().
                getClickToPickupItem() : box.files().messages().getAuctionMessages().getClickToPickupCoins();

        return Arrays.asList(
                new Placeholder("auction_bid_status", status),
                new Placeholder("auction_can_collect_item", canCollect),
                new Placeholder("auction_collect_item_placeholder", auctionCollectItem)
        );
    }

    @Override
    public void addContent() {
        fillInventory(this.config);

        setItem(this.config.getCloseGUI());

        final List<IPlaceholder> placeholders = getPlaceholders();

        inventory.setItem(this.config.getAuctionItem().getSlot(), ItemStackUtils.makeItem(this.config.getAuctionItem(), PlaceholderBuilder.create(placeholders)
                .with(getBidPlaceholders(this.auctionItem))
                .get(), this.auctionItem.getItemStack()));

        if (this.auctionItem.isExpired()) {
            if (isOwnAuction()) {
                if (this.auctionItem.getWhoBought() == null) {
                    setItem(this.config.getCollectAuctionEmptyItem(), placeholders);
                } else {
                    setItem(this.config.getCollectAuctionItem(), placeholders);
                }
            } else {
                setItem(this.config.getCollectItemAuctionItem(), PlaceholderBuilder.create(placeholders)
                        .with(getCollectItemPlaceholders())
                        .get());
            }

        } else {
            setItem(this.config.getSubmitBid(), PlaceholderBuilder.create(placeholders)
                    .with(getCollectItemPlaceholders())
                    .get());
        }

        if (!isOwnAuction() && canSubmit() && !this.auctionItem.isExpired()) {
            setItem(this.config.getBidAmount(), placeholders);
        }



        if (this.auctionItem.getBids().size() <= 1) {
            inventory.setItem(this.config.getBidHistoryEmpty().getSlot(), ItemStackUtils.makeItem(this.config.getBidHistoryEmpty()));
        } else {
            inventory.setItem(this.config.getBidHistoryFilled().getSlot(), ItemStackUtils.makeItem(this.config.getBidHistoryFilled(), getPlaceholders()));
        }
        setItem(this.config.getGoBack());
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
        } else if (isOwnAuction()) {
            if (isCollectItemOrMoneyItem(slot)) {
                //Esto es para agarrar las monedas o el item en caso de que nadie lo haya comprado (seller side)
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
            } else if (isItem(slot, this.config.getGoBack())) {
                player.openInventory(new ManageAuctionGUI(box, uuid, this.searcher, 1, ManageAuctionSortType.RECENTLY_UPDATED).getInventory());

            }
        } else {
            if (isItem(slot, this.config.getCollectItemAuctionItem()) && this.
                    auctionItem.isExpired() && this.auctionItem.getBid(uuid).isPresent()) {
                //Esto es para agarrar las monedas o el item en caso de que lo haya ganado (buyer side)
                player.closeInventory();

                if (isTopBidder()) {
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
            } else if (isItem(slot, this.config.getSubmitBid())) {
                if (this.auctionItem.getMarkable().getRemainingTime().isZero()) {
                    player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getAuctionExpired()));
                    return;
                }

                if (!canSubmit()) {
                    player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getYouCannotAffordIt()));
                    return;
                }

                if (isTopBidder()) {
                    player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getYouAlreadyHaveTheHighestBid()));
                    return;
                }

                final double newTopPrice = Math.max(getAutomaticBidPrice(), this.newCost);

                player.openInventory(new ConfirmAuctionGUI(box, uuid, this.searcher, this.auctionItem, newTopPrice).getInventory());

            } else if (isItem(slot, this.config.getGoBack())) {
                player.openInventory(new AllAuctionsGUI(box, 1, player.getUniqueId(), this.searcher).getInventory());

            } else if (isItem(slot, this.config.getBidAmount()) && canSubmit()) {
                player.closeInventory();

                final Location location = player.getLocation().clone().add(0, 10, 0);

                Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> {
                    try {
                        if (XMaterial.getVersion() > 20) {
                            final SignGUIFinishHandler signGUIFinishHandler = (player1, signGUIResult) -> {
                                Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> {
                                    this.changeBid(player1, signGUIResult.getLine(0));
                                }, 5);
                                return Collections.emptyList();
                            };
                            final SignGUI signGUI = SignGUI.builder().
                                    setLocation(location).
                                    setColor(DyeColor.BLACK).
                                    setType(Material.OAK_SIGN).
                                    setHandler(signGUIFinishHandler).setGlow(false).
                                    setLines(box.files().messages().getAuctionMessages().getSubmitBid().toArray(new String[0])).
                                    build();
                            signGUI.open(player);
                        } else {
                            SignGUIAPI.builder()
                                    .action((result) -> {
                                        changeBid(result.getPlayer(), (result.getLines().isEmpty() ?  "" : result.getLines().getFirst()));
                                    })
                                    .withLines(box.files().messages().getAuctionMessages().getSubmitBid())
                                    .uuid(player.getUniqueId())
                                    .plugin(box.plugin())
                                    .build()
                                    .open();
                        }
                    } catch (SignGUIVersionException e) {
                        e.printStackTrace();
                    }
                }, 5);
            }
        }
    }

    //Cambiar el bid a uno diferente
    private void changeBid(final Player player, final String input) {
        final double newBid;

        if (this.auctionItem == null) {
            return;
        }

        try {
            newBid = Double.parseDouble(input);

            if (newBid <= 0) {
                throw new NumberFormatException();
            }

            if (newBid < getAutomaticBidPrice()) {
                player.sendMessage(StringUtils.color(box.files().messages().
                        getAuctionMessages().getMustBeHigherThanMin().replace("%auction_new_bid%", String.valueOf(getAutomaticBidPrice()))));
                return;
            }

        } catch (NumberFormatException e) {
            player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getInvalidAmount()));
            return;
        }

        ViewOpener.open(player, this.auctionItem, box, this.searcher, newBid);
    }

    private List<IPlaceholder> getPlaceholders() {
        final String canSubmit = isOwnAuction() ? box.files().messages().getAuctionMessages().getOwnAuction() :
                isTopBidder() ? box.files().messages().getAuctionMessages().getAlreadyTopBid() :
                        canSubmit() ? box.files().messages().getAuctionMessages().
                                getCanSubmit() : box.files().messages().getAuctionMessages().getCannotSubmit();

        return Arrays.asList(
                new Placeholder("auction_owner_name", PlayerUtils.getPlayerName(uuid)),
                new Placeholder("auction_status", getStatusPlaceholder(this.auctionItem)),
                new Placeholder("auction_item_name", BukkitItemUtil.getName(this.auctionItem.getItemStack())),
                new Placeholder("auction_item_lore", BukkitItemUtil.getItemLore(this.auctionItem.getItemStack())),
                new Placeholder("auction_bids_amount", this.auctionItem.getBidsWithoutOwner().size()),
                new Placeholder("auction_bid_history", getHistoryPlaceholders(this.auctionItem)),
                new Placeholder("auction_can_submit", canSubmit),
                new Placeholder("auction_required_bid", getAutomaticBidPrice()),
                new Placeholder("auction_new_bid", Math.max(getAutomaticBidPrice(), this.newCost)),
                new Placeholder("auction_buyer_name", PlayerUtils.getPlayerName(this.auctionItem.getWhoBought())),
                new Placeholder("auction_top_bid", getTopPrice()));
    }

    private boolean isOwnAuction() {
        return this.auctionItem.getOwner().equals(uuid);
    }

    private boolean canSubmit() {
        final double money = TheAssistantPlugin.getAPI().getAddons().getEconomy().getMoney(Bukkit.getOfflinePlayer(uuid));

        final double toCheckPrice = Math.max(getAutomaticBidPrice(), this.newCost);

        return money >= toCheckPrice;
    }

    private double getAutomaticBidPrice() {
        return getTopPrice() + ((box.files().config().getMinPercentageToSubmitNewBid() * getTopPrice()) / 100);
    }

    private double getTopPrice() {
        return this.auctionItem.getBids().stream()
                .max(Comparator.comparingDouble(AuctionBid::getBidAmount))
                .map(AuctionBid::getBidAmount)
                .orElse(0D);
    }

    private boolean isTopBidder() {
        return uuid.equals(this.auctionItem.getBids().stream()
                .max(Comparator.comparingDouble(AuctionBid::getBidAmount))
                .map(AuctionBid::getBidder)
                .orElse(null));
    }

    protected Optional<AuctionBid> getPlayerHighestBid(final UUID uuid) {
        return this.auctionItem.getBids()
                .stream()
                .max(Comparator.comparingDouble(AuctionBid::getBidAmount))
                .filter(auctionItem -> auctionItem.getBidder().equals(uuid));
    }

    private boolean isCollectItemOrMoneyItem(final int slot) {
        final boolean isCollectAuctionSlot = (
                isItem(slot, this.config.getCollectAuctionItem()) || isItem(slot, this.config.getCollectAuctionEmptyItem())
        );

        final boolean isOwnAuctionExpired = this.auctionItem.isExpired() && isOwnAuction();

        /* TODO CHECK THIS
        && !getPlayerHighestBid(uuid).
                map(AuctionBid::isClaimedBack).
                orElse(false)
         */
        return isCollectAuctionSlot && isOwnAuctionExpired;
    }
}
