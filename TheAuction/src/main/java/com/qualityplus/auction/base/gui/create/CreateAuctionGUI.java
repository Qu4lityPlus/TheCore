package com.qualityplus.auction.base.gui.create;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.de.rapha149.signgui.SignGUI;
import com.qualityplus.assistant.lib.de.rapha149.signgui.exception.SignGUIVersionException;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.assistant.util.time.HumanTime;
import com.qualityplus.auction.api.box.Box;
import com.qualityplus.auction.base.gui.AuctionGUI;
import com.qualityplus.auction.base.gui.main.MainAuctionGUI;
import com.qualityplus.auction.base.gui.time.AuctionTimeGUI;
import com.qualityplus.auction.base.gui.view.ViewOpener;
import com.qualityplus.auction.base.searcher.AuctionSearcher;
import com.qualityplus.auction.base.sign.SignGUIAPI;
import com.qualityplus.auction.persistence.data.AuctionBid;
import com.qualityplus.auction.persistence.data.AuctionItem;
import com.qualityplus.auction.persistence.data.User;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Utility class for create auction
 */
public final class CreateAuctionGUI extends AuctionGUI {
    private final CreateAuctionGUIConfig config;
    private final AuctionSearcher searcher;
    private final String name;

    /**
     * Adds create auction
     *
     * @param boxUtil  {@link Box}
     * @param player   {@link Player}
     * @param searcher {@link AuctionSearcher}
     */
    public CreateAuctionGUI(final Box boxUtil, final Player player, final AuctionSearcher searcher) {
        super(boxUtil.files().inventories().getCreateAuctionGUIConfig(), boxUtil);

        this.config = boxUtil.files().inventories().getCreateAuctionGUIConfig();
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.searcher = searcher;
    }

    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(this.config);

        final Optional<User> user = box.service().getUser(uuid);

        final String timer = box.files().config().getDurationPrices().keySet().stream().findFirst().orElse(null);

        if (timer == null) {
            return inventory;
        }

        final AuctionItem auctionItem = user.map(User::getTemporalAuction)
                .orElse(AuctionItem.builder()
                        .owner(uuid)
                        .ownerName(this.name)
                        .timer(timer)
                        .isBuyItNow(false)
                        .bids(defaultBid())
                        .build());

        final List<IPlaceholder> placeholders = getPlaceholders(auctionItem);

        if (auctionItem.getItemStack() == null) {
            setItem(this.config.getCurrentItemEmpty());
            setItem(this.config.getCreateAuctionEmpty(), Collections.singletonList(getIsBuyItNowPlaceholder(auctionItem)));
        } else {
            inventory.setItem(this.config.getCurrentItemFilled().getSlot(), ItemStackUtils.
                    makeItem(this.config.getCurrentItemFilled(), placeholders, auctionItem.getItemStack()));

            inventory.setItem(this.config.getCreateAuctionFilled().getSlot(), ItemStackUtils.
                    makeItem(this.config.getCreateAuctionFilled(), placeholders));
        }

        if (auctionItem.isBuyItNow()) {
            setItem(this.config.getAuctionItemPrice(), placeholders);
            setItem(this.config.getSwitchToAuction());
        } else {
            setItem(this.config.getAuctionInitialBid(), placeholders);
            setItem(this.config.getSwitchToBin());
        }


        setItem(this.config.getAuctionDuration(), placeholders);
        setItem(this.config.getGoBack(), placeholders);

        setItem(this.config.getCloseGUI());

        user.ifPresent(u -> u.setTemporalAuction(auctionItem));

        return inventory;
    }


    @Override
    public void onInventoryClick(final InventoryClickEvent e) {
        e.setCancelled(true);

        final Player player = (Player) e.getWhoClicked();

        final int slot = e.getSlot();

        final Optional<User> user = box.service().getUser(uuid);

        final AuctionItem auctionItem = user
                .map(User::getTemporalAuction)
                .orElse(null);

        if (auctionItem == null) {
            return;
        }

        if (getTarget(e).equals(ClickTarget.PLAYER)) {
            final ItemStack itemStack = e.getCurrentItem();

            if (BukkitItemUtil.isNull(itemStack)) {
                return;
            }
            Optional.ofNullable(auctionItem.getItemStack()).ifPresent(item -> player.getInventory().addItem(item));

            auctionItem.setItemStack(itemStack.clone());

            e.setCurrentItem(null);

            player.openInventory(new CreateAuctionGUI(box, player, this.searcher).getInventory());
        } else {

            if (isItem(slot, this.config.getCloseGUI())) {
                player.closeInventory();
            } else if (isItem(slot, this.config.getGoBack())) {
                player.openInventory(new MainAuctionGUI(box, this.searcher, uuid).getInventory());
            } else if (isItem(slot, this.config.getAuctionDuration())) {
                player.openInventory(new AuctionTimeGUI(box, auctionItem, this.searcher).getInventory());
            } else if (isItem(slot, this.config.getAuctionInitialBid())) {
                player.closeInventory();

                final Location location = player.getLocation().clone().add(0, 10, 0);

                Bukkit.getScheduler().runTaskLater(this.box.plugin(), () -> {
                    try {
                        if (XMaterial.getVersion() > 20) {
                            final SignGUI signGUI = SignGUI.builder()
                                    .setLocation(location)
                                    .setColor(DyeColor.BLACK)
                                    .setType(Material.OAK_SIGN)
                                    .setHandler((player1, signGUIResult) -> {
                                        changeBidPrice(player1, auctionItem, signGUIResult.getLine(0));
                                        return Collections.emptyList();
                                    })
                                    .setGlow(false)
                                    .setLines(box.files().messages().getAuctionMessages().getStartingBid().toArray(new String[0]))
                                    .build();
                            signGUI.open(player);
                        } else {
                            SignGUIAPI.builder()
                                    .action((result) -> {
                                        changeBidPrice(result.getPlayer(), auctionItem, (result.getLines().isEmpty() ?  "" : result.getLines().getFirst()));
                                    })
                                    .withLines(box.files().messages().getAuctionMessages().getStartingBid())
                                    .uuid(player.getUniqueId())
                                    .plugin(box.plugin())
                                    .build()
                                    .open();
                        }
                    } catch (SignGUIVersionException ex) {
                        ex.printStackTrace();
                    }
                }, 5);
            } else if (isItem(slot, this.config.getCreateAuctionFilled())) {
                if (auctionItem.getItemStack() == null) {
                    return;
                }

                final double fees = getPrice(auctionItem);

                //Set timer
                final HumanTime timer = box.files().config().getDurationPrices().get(auctionItem.getTimerId()).getTimer();

                auctionItem.setMarkable(new Markable(timer.getEffectiveTime(), System.currentTimeMillis()));

                //Add to all auction houses
                box.auctionService().getAuctionHouse().ifPresent(auctionHouse -> auctionHouse.getNormalItems().add(auctionItem));

                //Remove from player ah
                user.ifPresent(user1 -> user1.setTemporalAuction(null));

                //Remove Fees money
                TheAssistantPlugin.getAPI().getAddons().getEconomy().withdrawMoney(player, fees);

                final String name = BukkitItemUtil.getName(auctionItem.getItemStack());

                player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getAuctionStarted().replace("%auction_item_name%", name)));

                ViewOpener.open(player, auctionItem, box, this.searcher, -1);

                //Add Created Stat
                user.ifPresent(user1 -> user1.getAuctionStats().setAuctionsCreated(user1.getAuctionStats().getAuctionsCreated() + 1));
                user.ifPresent(user1 -> user1.getAuctionStats().setMoneySpentOnFees(user1.getAuctionStats().getMoneySpentOnFees() + fees));

            } else if (isItem(slot, this.config.getCurrentItemFilled())) {
                if (BukkitItemUtil.isNull(auctionItem.getItemStack())) {
                    return;
                }

                final ItemStack toGive = auctionItem.getItemStack().clone();

                auctionItem.setItemStack(null);

                player.openInventory(new CreateAuctionGUI(box, player, this.searcher).getInventory());

                player.getInventory().addItem(toGive);

            } else if (isItem(slot, this.config.getSwitchToAuction()) || isItem(slot, this.config.getSwitchToBin())) {
                user.ifPresent(user1 -> user1.getTemporalAuction().setBuyItNow(!auctionItem.isBuyItNow()));
                player.openInventory(new CreateAuctionGUI(box, player, this.searcher).getInventory());
            }
        }
    }

    private void changeBidPrice(final Player player, final AuctionItem auctionItem, final String input) {
        final double newPrice;

        if (auctionItem == null) {
            return;
        }

        try {
            newPrice = Double.parseDouble(input);

            if (newPrice <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            Bukkit.getScheduler().runTaskLater(box.plugin(), () -> player.openInventory(
                            new CreateAuctionGUI(box, player, this.searcher).getInventory()),
                    3);
            player.sendMessage(StringUtils.color(box.files().messages().getAuctionMessages().getInvalidAmount()));
            return;
        }

        auctionItem.getBid(uuid).ifPresent(bid -> bid.setBidAmount(newPrice));

        Bukkit.getScheduler().runTaskLater(box.plugin(), () -> player.openInventory(
                new CreateAuctionGUI(box, player, this.searcher).getInventory()),
                3);
    }


    private List<AuctionBid> defaultBid() {
        return new ArrayList<>(Collections.singletonList(new AuctionBid(uuid, box.files().config().getStartBidPrice(), System.currentTimeMillis(), false)));
    }

    private List<IPlaceholder> getPlaceholders(final AuctionItem auctionItem) {

        final double startingBid = auctionItem.getBid(uuid).map(AuctionBid::getBidAmount).orElse(0D);

        final double timeFee = box.files().config().getDurationPrices().get(auctionItem.getTimerId()).getFee();

        final int percentage = (int) ((5 * startingBid) / 100);

        final int creationPrice = (int) (percentage + timeFee);

        return Arrays.asList(
                new Placeholder("auction_item_name", BukkitItemUtil.getName(auctionItem.getItemStack())),
                new Placeholder("auction_item_lore", BukkitItemUtil.getItemLore(auctionItem.getItemStack())),
                new Placeholder("auction_creation_price", creationPrice),
                new Placeholder("auction_fee", percentage),
                new Placeholder("auction_duration_fee", timeFee),
                new Placeholder("auction_duration", getDuration(auctionItem)),
                new Placeholder("auction_starting_bid", startingBid),
                getIsBuyItNowPlaceholder(auctionItem)

        );
    }

    private Placeholder getIsBuyItNowPlaceholder(final AuctionItem item) {
        return new Placeholder("auction_is_buy_it_now_placeholder", item.isBuyItNow() ? box.files().
                messages().getAuctionMessages().getBuyItNowPlaceholder() : box.files().
                messages().getAuctionMessages().getAuctionPlaceholder());
    }

    private double getPrice(final AuctionItem auctionItem) {
        final double startingBid = auctionItem.getBid(uuid).map(AuctionBid::getBidAmount).orElse(0D);

        final double timeFee = box.files().config().getDurationPrices().get(auctionItem.getTimerId()).getFee();

        final int percentage = (int) ((5 * startingBid) / 100);

        return  (int) (percentage + timeFee);
    }

    private String getDuration(final AuctionItem auctionItem) {
        final HumanTime timer = box.files().config().getDurationPrices().get(auctionItem.getTimerId()).getTimer();

        final List<IPlaceholder> placeholders = Arrays.asList(
                new Placeholder("auction_duration_type", box.files().messages().getAuctionMessages().getTimeFormat().get(timer.getType())),
                new Placeholder("auction_duration_time", timer.getAmount())
        );

        return StringUtils.processMulti(box.files().messages().getAuctionMessages().getAuctionDurationFormat(), placeholders);

    }
}
