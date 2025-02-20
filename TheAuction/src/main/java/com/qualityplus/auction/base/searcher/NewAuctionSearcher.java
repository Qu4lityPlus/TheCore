package com.qualityplus.auction.base.searcher;

import com.qualityplus.auction.TheAuction;
import com.qualityplus.auction.api.filter.AuctionFilter;
import com.qualityplus.auction.api.searcher.Searcher;
import com.qualityplus.auction.persistence.data.AuctionItem;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Utility class for new auction searcher
 */
@Component
@RequiredArgsConstructor
public class NewAuctionSearcher implements Searcher<AuctionItem> {
    private final AuctionFilter auctionFilter;

    @Override
    public List<AuctionItem> getFiltered() {


        return this.auctionFilter.filter(TheAuction.getApi().getAuctionService().getItems());
    }



   /* private Predicate<AuctionItem> stringFilter() {
        if (stringFilter == null || stringFilter.getToSearch() == null) return auctionItem -> true;

        return auctionItem -> BukkitItemUtil.getItemLore(auctionItem.getItemStack()).contains(stringFilter.getToSearch()) ||
                BukkitItemUtil.getName(auctionItem.getItemStack()).equalsIgnoreCase(stringFilter.getToSearch()) ||
                BukkitItemUtil.getName(auctionItem.getItemStack()).contains(stringFilter.getToSearch());
    }

    private Predicate<AuctionItem> binFilter() {
        if (binFilter.equals(BinFilter.BIN_ONLY)) {
            return AuctionItem::isBuyItNow;
        } else if (binFilter.equals(BinFilter.AUCTION_ONLY)) {
            return auctionItem -> !auctionItem.isBuyItNow();
        } else
            return auctionItem -> true;
    }

    private Predicate<AuctionItem> categoryFilter() {
        if (categoryFilter == null || categoryFilter.getCategory() == null) return auctionItem -> true;

        Optional<AuctionCategory> category = categoryFilter.getBox().files().bankUpgrades().getById(categoryFilter.getCategory());

        return category.<Predicate<AuctionItem>>map(auctionCategory -> auctionItem -> auctionCategory.getMaterials()
                        .stream()
                        .anyMatch(material -> checkIfItEquals(auctionItem, material)))
                .orElseGet(() -> auctionItem -> true);
    }

    private boolean checkIfItEquals(AuctionItem auctionItem, XMaterial material) {
        return XMaterial.matchXMaterial(auctionItem.getItemStack()).equals(material);
    }

    public String getCategory() {
        return categoryFilter.getCategory();
    }*/
}
