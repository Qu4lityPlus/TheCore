package com.qualityplus.auction.base.gui.all;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.de.rapha149.signgui.SignGUI;
import com.qualityplus.assistant.lib.de.rapha149.signgui.SignGUIFinishHandler;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.auction.api.box.Box;
import com.qualityplus.auction.api.category.AuctionCategory;
import com.qualityplus.auction.base.gui.AuctionGUI;
import com.qualityplus.auction.base.gui.main.MainAuctionGUI;
import com.qualityplus.auction.base.gui.view.ViewOpener;
import com.qualityplus.auction.base.searcher.AuctionSearcher;
import com.qualityplus.auction.base.searcher.filters.BinFilter;
import com.qualityplus.auction.base.searcher.filters.SortFilter;
import com.qualityplus.auction.base.searcher.filters.StringFilter;
import com.qualityplus.auction.base.sign.SignGUIAPI;
import com.qualityplus.auction.persistence.data.AuctionItem;
import com.qualityplus.auction.util.AuctionItemStackUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Utility class for all auction gui
 */
public final class AllAuctionsGUI extends AuctionGUI {
    private final Map<Integer, AuctionCategory> categoriesMap = new HashMap<>();
    private final Map<Integer, AuctionItem> allAuctions = new HashMap<>();
    private final AllAuctionsGUIConfig config;
    private final AuctionSearcher searcher;
    private final int itemsSize;

    /**
     * Adds all auctions
     *
     * @param boxUtil         {@link Box}
     * @param page            Page
     * @param uuid            {@link UUID}
     * @param auctionSearcher {@link AuctionSearcher}
     */
    public AllAuctionsGUI(final Box boxUtil, final int page, final UUID uuid, final AuctionSearcher auctionSearcher) {
        super(boxUtil.files().inventories().getAllAuctionsGUIConfig(), boxUtil);

        final List<AuctionItem> items = getNoExpiredItems(auctionSearcher);

        this.maxPerPage = box.files().inventories().getAllAuctionsGUIConfig().getAuctionSlots().size();
        this.config = boxUtil.files().inventories().getAllAuctionsGUIConfig();
        this.hasNext = items.size() > maxPerPage * page;
        this.searcher = auctionSearcher;
        this.itemsSize = items.size();
        this.page = page;
        this.uuid = uuid;
    }

    private static List<AuctionItem> getNoExpiredItems(final AuctionSearcher searcher) {
        return searcher.getFiltered().stream()
                .filter(auctionItem -> !auctionItem.isExpired())
                .filter(auctionItem -> !auctionItem.hasBeenBought())
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(this.config);

        setCategoryItems();

        setItem(this.config.getCloseGUI());

        setItem(this.config.getGoBack());

        addContent();

        if (this.page > 1) {
            inventory.setItem(this.config.getPreviousPage().getSlot(), ItemStackUtils.makeItem(this.config.getPreviousPage()));
        }


        if (hasNext) {
            inventory.setItem(this.config.getNextPage().getSlot(), ItemStackUtils.makeItem(this.config.getNextPage()));
        }

        setItem(this.config.getSortItem(), getSortPlaceholders());
        setItem(this.config.getBinFilterItem(), getBinPlaceholders());

        if (this.searcher.getStringFilter().getToSearch() == null) {
            setItem(this.config.getByNameFilterEmptyItem());
        } else {
            setItem(this.config.getByNameFilterFilledItem(), Collections.
                    singletonList(new Placeholder("auction_by_name_search", this.searcher.getStringFilter().getToSearch())));
        }

        setItem(this.config.getResetSettings());


        return inventory;
    }

    private void deleteExpired() {
        if (this.itemsSize == getNoExpiredItems(this.searcher).size()) {
            return;
        }

        Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.
                openInventory(new AllAuctionsGUI(box, page, uuid, this.searcher).getInventory()));
    }

    @Override
    public void addContent() {
        deleteExpired();

        final List<AuctionItem> auctions = getNoExpiredItems(this.searcher);

        try {

            int slot = 0;
            int i = maxPerPage * (page - 1);
            if (auctions.size() > 0) {
                while (slot < maxPerPage) {
                    if (auctions.size() > i && i >= 0) {
                        final AuctionItem auctionItem = auctions.get(i);
                        final int finalSlot = this.config.getAuctionSlots().get(slot);

                        inventory.setItem(finalSlot, ItemStackUtils.makeItem(this.config.getAuctionItem(),
                                getAuctionItemPlaceholders(auctionItem), auctionItem.getItemStack()));
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

    private List<IPlaceholder> getBinPlaceholders() {
        final BinFilter filter = this.searcher.getBinFilter();
        return Arrays.asList(
                new Placeholder("auction_browser_show_all", getAnswer(filter.equals(BinFilter.SHOW_ALL))),
                new Placeholder("auction_browser_bin_only", getAnswer(filter.equals(BinFilter.BIN_ONLY))),
                new Placeholder("auction_browser_auctions_only", getAnswer(filter.equals(BinFilter.AUCTION_ONLY)))
        );
    }

    private List<IPlaceholder> getSortPlaceholders() {
        final SortFilter filter = this.searcher.getSortFilter();
        return Arrays.asList(
                new Placeholder("auction_browser_is_by_highest", getAnswer(filter.equals(SortFilter.HIGHEST_PRICE))),
                new Placeholder("auction_browser_is_by_lowest", getAnswer(filter.equals(SortFilter.LOWEST_PRICE))),
                new Placeholder("auction_browser_is_by_ending_soon", getAnswer(filter.equals(SortFilter.ENDING_SOON))),
                new Placeholder("auction_browser_is_by_random", getAnswer(filter.equals(SortFilter.RANDOM)))
        );
    }

    private String getAnswer(final boolean value) {
        return value ? box.files().messages().getAuctionMessages().getSortSelected() : box.files().messages().getAuctionMessages().getSortNotSelected();
    }

    private void setCategoryItems() {
        for (AuctionCategory category : box.files().bankUpgrades().getCategoryList()) {
            if (category.getId().equals(this.searcher.getCategory())) {
                InventoryUtils.fillInventory(inventory, category.getDisplayInfo().getBackground());
            }

            this.categoriesMap.put(category.getDisplayInfo().getSlot(), category);

            inventory.setItem(category.getDisplayInfo().getSlot(), AuctionItemStackUtils.makeCategoryItem(this.config.getCategoryItem(),
                    Arrays.asList(new Placeholder("auction_category_displayname", category.getDisplayName()),
                                  new Placeholder("auction_category_lore", category.getDisplayInfo().getDescription()),
                                  new Placeholder("auction_is_current_category", category.getId().
                                          equals(this.searcher.getCategory()) ? box.files().messages().getAuctionMessages().getCurrentlyBrowsing() :
                                          box.files().messages().getAuctionMessages().getCurrentlyNotBrowsing())),
                    category.getDisplayInfo(), box.files().config().getLoreWrapper()));
        }
    }

    @Override
    public void onInventoryClick(final InventoryClickEvent event) {
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();

        final int slot = event.getSlot();

        if (isItem(slot, this.config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, this.config.getNextPage()) && hasNext) {
            player.openInventory(new AllAuctionsGUI(box, page + 1, uuid, this.searcher).getInventory());
        } else if (isItem(slot, this.config.getPreviousPage()) && page > 1) {
            player.openInventory(new AllAuctionsGUI(box, page - 1, uuid, this.searcher).getInventory());
        } else if (this.allAuctions.containsKey(slot)) {
            final AuctionItem auctionItem = this.allAuctions.get(slot);

            if (auctionItem == null) {
                return;
            }

            ViewOpener.open(player, auctionItem, box, this.searcher, -1);
        } else if (this.categoriesMap.containsKey(slot)) {
            final AuctionCategory category = this.categoriesMap.getOrDefault(slot, null);

            if (category == null) {
                return;
            }

            player.openInventory(new AllAuctionsGUI(box, 1, uuid, getBuilder()
                    .category(category.getId())
                    .build()).getInventory());
        } else if (isItem(slot, this.config.getSortItem())) {
            player.openInventory(new AllAuctionsGUI(box, 1, uuid, getBuilder()
                    .sortFilter(this.searcher.getSortFilter().getNext())
                    .build()).getInventory());
        } else if (isItem(slot, this.config.getBinFilterItem())) {
            player.openInventory(new AllAuctionsGUI(box, 1, uuid, getBuilder()
                    .binFilter(this.searcher.getBinFilter().getNext())
                    .build()).getInventory());
        } else if (isItem(slot, this.config.getByNameFilterEmptyItem()) || isItem(slot, this.config.getByNameFilterFilledItem())) {
            if (this.searcher.getStringFilter().getToSearch() != null && event.isRightClick()) {
                player.openInventory(new AllAuctionsGUI(box, 1, uuid, getBuilder()
                        .stringFilter(new StringFilter(null))
                        .build()).getInventory());
                return;
            }
            player.closeInventory();

            final Location location = player.getLocation().clone().add(0, 10, 0);

            try {
                if (XMaterial.getVersion() > 20) {
                    final SignGUIFinishHandler signGUIFinishHandler = (player1, signGUIResult) -> {
                        handleSignQuery(player1, signGUIResult.getLines());
                        return Collections.emptyList();
                    };
                    final SignGUI signGUI = SignGUI.builder().
                            setLocation(location).
                            setColor(DyeColor.BLACK).
                            setType(Material.OAK_SIGN).
                            setHandler(signGUIFinishHandler).setGlow(false).
                            setLines(box.files().messages().getAuctionMessages().getEnterQuery().toArray(new String[0])).
                            build();
                    signGUI.open(player);
                } else {
                    SignGUIAPI.builder()
                            .action((result) -> {
                                handleSignQuery(result.getPlayer(), result.getLines().toArray(new String[0]));
                            })
                            .withLines(box.files().messages().getAuctionMessages().getEnterQuery())
                            .uuid(player.getUniqueId())
                            .plugin(box.plugin())
                            .build()
                            .open();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isItem(slot, this.config.getGoBack())) {
            player.openInventory(new MainAuctionGUI(box, this.searcher, uuid).getInventory());
        }
    }

    private void handleSignQuery(final Player player, final String[] lines) {
        Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> {
            final AuctionSearcher filter = getBuilder().
                    stringFilter(lines.length > 0 ? new StringFilter(lines[0]) : new StringFilter("")).
                    build();
            final Inventory inventory1 = new AllAuctionsGUI(box, 1, uuid, filter).getInventory();
            Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> player.openInventory(inventory1), 5);
        }, 5);
    }

    private AuctionSearcher.AuctionSearcherBuilder getBuilder() {
        return AuctionSearcher.builder()
                .stringFilter(this.searcher.getStringFilter())
                .sortFilter(this.searcher.getSortFilter())
                .binFilter(this.searcher.getBinFilter())
                .category(box.files().config().getDefaultCategory())
                .box(box);
    }

}
