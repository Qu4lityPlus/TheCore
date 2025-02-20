package com.qualityplus.auction.base.gui.main;

import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.player.PlayerUtils;
import com.qualityplus.auction.api.box.Box;
import com.qualityplus.auction.base.gui.AuctionGUI;
import com.qualityplus.auction.base.gui.all.AllAuctionsGUI;
import com.qualityplus.auction.base.gui.create.CreateAuctionGUI;
import com.qualityplus.auction.base.gui.manage.ManageAuctionGUI;
import com.qualityplus.auction.base.gui.manage.sort.ManageAuctionSortType;
import com.qualityplus.auction.base.gui.pending.PendingAuctionGUI;
import com.qualityplus.auction.base.gui.stats.AuctionStatsGUI;
import com.qualityplus.auction.base.searcher.AuctionSearcher;
import com.qualityplus.auction.persistence.data.AuctionItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Utility class dof main auction
 */
public final class MainAuctionGUI extends AuctionGUI {
    private final MainAuctionGUIConfig config;
    private final AuctionSearcher searcher;
    private final int ownAuctions;

    /**
     * Make auction main
     *
     * @param boxUtil  {@link Box}
     * @param searcher {@link AuctionSearcher}
     * @param uuid     {@link UUID}
     */
    public MainAuctionGUI(final Box boxUtil, final AuctionSearcher searcher, final UUID uuid) {
        super(boxUtil.files().inventories().getAuctionGUIConfig(), boxUtil);

        this.config = boxUtil.files().inventories().getAuctionGUIConfig();
        this.ownAuctions = getNotClaimedOwned(uuid).size();
        this.searcher = searcher;
        this.uuid = uuid;
    }

    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(this.config);

        setItem(this.config.getCloseGUI());

        setItem(this.config.getAuctionsBrowser());

        final List<AuctionItem> whereBid = getAuctionsWherePlayerBid(uuid);

        if (whereBid.size() <= 0) {
            setItem(this.config.getViewBidsEmpty());
        } else {
            setItem(this.config.getViewBids(), Collections.singletonList(new Placeholder("auction_pending_amount", whereBid.size())));
        }
        if (this.ownAuctions <= 0) {
            setItem(this.config.getCreateAnAuction());
        } else {
            setItem(this.config.getManageAuctions(), Arrays.asList(
                    new Placeholder("player", PlayerUtils.getPlayerName(uuid)),
                    new Placeholder("auction_bids_amount", this.ownAuctions)
            ));
        }

        setItem(this.config.getAuctionStats());

        return inventory;
    }

    @Override
    public void onInventoryClick(final InventoryClickEvent event) {
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();

        final int slot = event.getSlot();

        if (isItem(slot, this.config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, this.config.getAuctionStats())) {
            player.openInventory(new AuctionStatsGUI(box, uuid, this.searcher).getInventory());
        } else if (isItem(slot, this.config.getAuctionsBrowser())) {
            player.openInventory(new AllAuctionsGUI(box, 1, uuid, this.searcher).getInventory());
        } else if (isItem(slot, this.config.getManageAuctions()) && this.ownAuctions > 0) {
            player.openInventory(new ManageAuctionGUI(box, uuid, this.searcher, 1, ManageAuctionSortType.RECENTLY_UPDATED).getInventory());
        } else if (isItem(slot, this.config.getCreateAnAuction()) && this.ownAuctions <= 0) {
            player.openInventory(new CreateAuctionGUI(box, player, this.searcher).getInventory());
        } else if (isItem(slot, this.config.getViewBids())) {
            final List<AuctionItem> whereBid = getAuctionsWherePlayerBid(uuid);

            if (whereBid.size() <= 0) {
                return;
            }

            player.openInventory(new PendingAuctionGUI(box, uuid, this.searcher, 1).getInventory());
        }
    }

}
