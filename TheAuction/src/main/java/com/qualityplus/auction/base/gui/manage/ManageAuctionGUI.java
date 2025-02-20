package com.qualityplus.auction.base.gui.manage;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.auction.api.box.Box;
import com.qualityplus.auction.base.gui.AuctionGUI;
import com.qualityplus.auction.base.gui.create.CreateAuctionGUI;
import com.qualityplus.auction.base.gui.main.MainAuctionGUI;
import com.qualityplus.auction.base.gui.manage.sort.ManageAuctionSortType;
import com.qualityplus.auction.base.gui.view.ViewOpener;
import com.qualityplus.auction.base.searcher.AuctionSearcher;
import com.qualityplus.auction.persistence.data.AuctionBid;
import com.qualityplus.auction.persistence.data.AuctionItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class manage auction
 */
public final class ManageAuctionGUI extends AuctionGUI {
    private final Map<Integer, AuctionItem> allAuctions = new HashMap<>();
    private final ManageAuctionSortType sortType;
    private final ManageAuctionGUIConfig config;
    private final AuctionSearcher searcher;

    /**
     * Adds manage auction gui
     *
     * @param boxUtil  {@link Box}
     * @param uuid     {@link UUID}
     * @param searcher {@link AuctionSearcher}
     * @param page     Page
     */
    public ManageAuctionGUI(final Box boxUtil,
                            final UUID uuid,
                            final AuctionSearcher searcher,
                            final int page,
                            final ManageAuctionSortType sortType) {
        super(boxUtil.files().inventories().getManageAuctionGUINewConfig(), boxUtil);

        final List<AuctionItem> auctionItems = getNotClaimedOwned(uuid);
        final List<AuctionItem> filteredList = getSortedList(auctionItems, sortType);

        this.maxPerPage = box.files().inventories().getManageAuctionGUINewConfig().getAuctionSlots().size();
        this.hasNext = filteredList.size() > maxPerPage * page;
        this.config = boxUtil.files().inventories().getManageAuctionGUINewConfig();
        this.searcher = searcher;
        this.sortType = sortType;
        this.page = page;
        this.uuid = uuid;
    }

    /**
     * Add inventory
     *
     * @return {@link Inventory}
     */
    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(this.config);

        addContent();

        setItem(this.config.getCloseGUI());
        setItem(this.config.getGoBackItem());

        setItem(this.config.getSortItem(), getSortPlaceholders());
        setItem(this.config.getCreateAnAuction());

        if (hasNext) {
            setItem(this.config.getNextPageItem());
        }
        if (page > 1) {
            setItem(this.config.getPreviousPageItem());
        }

        return inventory;
    }



    @Override
    public void addContent() {
        final List<AuctionItem> allAuctions = getNotClaimedOwned(uuid);
        final List<AuctionItem> auctions = getSortedList(allAuctions, sortType);
        try {
            int slot = 0;
            int i = maxPerPage * (page - 1);
            if (!auctions.isEmpty()) {
                while (slot < maxPerPage) {
                    if (auctions.size() > i && i >= 0) {
                        final AuctionItem auctionItem = auctions.get(i);
                        final int finalSlot = this.config.getAuctionSlots().get(slot);
                        inventory.setItem(finalSlot,
                                ItemStackUtils.makeItem(this.config.getAuctionItem(),
                                getAuctionItemPlaceholders(auctionItem),
                                auctionItem.getItemStack()));

                        this.allAuctions.put(finalSlot, auctionItem);
                        slot++;
                        i++;
                        continue;
                    }
                    slot++;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInventoryClick(final InventoryClickEvent event) {
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();

        final int slot = event.getSlot();

        if (isItem(slot, this.config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, this.config.getGoBackItem())) {
            player.openInventory(new MainAuctionGUI(box, this.searcher, player.getUniqueId()).getInventory());
        } else if (isItem(slot, this.config.getCreateAnAuction())) {
            player.openInventory(new CreateAuctionGUI(box, player, this.searcher).getInventory());
        } else if (this.allAuctions.containsKey(slot)) {
            final AuctionItem auctionItem = this.allAuctions.get(slot);

            if (auctionItem == null) {
                return;
            }

            ViewOpener.open(player, auctionItem, box, this.searcher, -1);
        } else if (isItem(slot, this.config.getPreviousPageItem()) && page > 1) {
            player.openInventory(new ManageAuctionGUI(this.box, this.uuid, this.searcher, this.page - 1, this.sortType).getInventory());
        } else if (isItem(slot, this.config.getNextPageItem()) && hasNext) {
            player.openInventory(new ManageAuctionGUI(this.box, this.uuid, this.searcher, this.page + 1, this.sortType).getInventory());
        } else if (isItem(slot, this.config.getSortItem())) {
            player.openInventory(new ManageAuctionGUI(this.box, this.uuid, this.searcher, 1, this.sortType.getNext()).getInventory());
        }
    }

    private List<AuctionItem> getSortedList(final List<AuctionItem> items, final ManageAuctionSortType sortType) {
        return switch (sortType) {
            case RECENTLY_UPDATED -> items.stream()
                    .sorted(Comparator.comparingLong(item -> item.getMarkable().remainingTime()))
                    .toList();
            case MOST_BID -> items.stream()
                    .sorted(Comparator.comparingInt(i -> ((AuctionItem)i).getBidsWithoutOwner().size()).reversed())
                    .toList();
            case HIGHEST_BID -> items.stream()
                    .sorted(Comparator.comparingDouble(i -> ((AuctionItem)i).getHighestBid()))
                    .toList();
        };
    }


    private List<IPlaceholder> getSortPlaceholders() {
        final ManageAuctionSortType filter = this.sortType;
        return Arrays.asList(
                new Placeholder("auction_browser_is_by_recently_updated", getAnswer(filter.equals(ManageAuctionSortType.RECENTLY_UPDATED))),
                new Placeholder("auction_browser_is_by_highest_bid", getAnswer(filter.equals(ManageAuctionSortType.HIGHEST_BID))),
                new Placeholder("auction_browser_is_by_most_bid", getAnswer(filter.equals(ManageAuctionSortType.MOST_BID)))
        );
    }

    private String getAnswer(final boolean value) {
        return value ? this.box.files().messages().getAuctionMessages().getSortSelected() : this.box.files().messages().getAuctionMessages().getSortNotSelected();
    }
}
