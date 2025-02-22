package com.qualityplus.auction.base.gui.manage.sort;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum ManageAuctionSortType {
    RECENTLY_UPDATED(0),
    HIGHEST_BID(1),
    MOST_BID(2);

    private final int level;

    public ManageAuctionSortType getNext() {
        return Arrays.stream(values())
                .filter(sortBy -> sortBy.level == this.level + 1)
                .findFirst()
                .orElse(ManageAuctionSortType.RECENTLY_UPDATED);
    }
}
