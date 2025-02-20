package com.qualityplus.bank.base.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.bank.api.gui.GUIDisplayItem;
import com.qualityplus.bank.base.upgrade.BankUpgrade;
import com.qualityplus.bank.base.upgrade.UpgradeInterest;
import com.qualityplus.bank.persistence.data.BankData;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Configuration(path = "upgrades.yml")
@Header("================================")
@Header("       Upgrades      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class BankUpgrades extends OkaeriConfig {
    public List<BankUpgrade> bankUpgrades = Arrays.asList(
            getStarter(),
            getGoldAccount(),
            getDeluxeAccount()
    );

    public Optional<BankUpgrade> getUpgrade(BankData data) {
        return bankUpgrades.stream()
                .filter(bankUpgrade -> bankUpgrade.getId().equals(data.getBankUpgrade()))
                .findFirst();
    }

    public Optional<BankUpgrade> getLowest() {
        return bankUpgrades.stream()
                .map(BankUpgrade::getHierarchy)
                .min(Integer::compare)
                .flatMap(v -> bankUpgrades.stream().filter(bankUpgrade -> bankUpgrade.getHierarchy() == v).findFirst());
    }

    public Optional<BankUpgrade> getHighest() {
        return bankUpgrades.stream()
                .map(BankUpgrade::getHierarchy)
                .max(Integer::compare)
                .flatMap(v -> bankUpgrades.stream().filter(bankUpgrade -> bankUpgrade.getHierarchy() == v).findFirst());
    }

    private BankUpgrade getStarter() {
        return BankUpgrade.builder()
                .id("starter")
                .displayName("&aStarter Account")
                .bankLimit(50000000)
                .coinsCost(0)
                .itemCost(new HashMap<>())
                .upgradeInterestList(Arrays.asList(
                        new UpgradeInterest(2, 250000, 0, 10000000),
                        new UpgradeInterest(1, 250000, 10000000, 15000000)
                ))
                .displayItem(GUIDisplayItem.builder()
                        .item(XMaterial.WHEAT_SEEDS)
                        .slot(12)
                        .description(Arrays.asList("&8Not upgraded",
                                "",
                                "&a>——————— &6Interest Tranches &a———————<",
                                " &eFirst &610M &ecoins yields &b2% &einterest.",
                                " &eFrom &610M &eto &615M &ecoins yields &b1% &einterest.",
                                "",
                                "&7Max interest: &6250.000",
                                "&a>————————————————————————————————<",
                                "",
                                "&7Max balance: &650 Million Coins",
                                "",
                                "&7Cost: &aComplimentary"
                        )).build())
                .hierarchy(1)
                .build();
    }

    private BankUpgrade getGoldAccount() {
        return BankUpgrade.builder()
                .id("gold")
                .displayName("&6Gold Account")
                .bankLimit(100000000)
                .coinsCost(5000000)
                .itemCost(new HashMap<>())
                .upgradeInterestList(Arrays.asList(
                        new UpgradeInterest(2, 250000, 0, 9999999),
                        new UpgradeInterest(1, 250000, 10000000, 20000000)
                ))
                .displayItem(GUIDisplayItem.builder()
                        .item(XMaterial.GOLD_NUGGET)
                        .slot(13)
                        .description(Arrays.asList("&8Bank upgrade",
                                "",
                                "&a>——————— &6Interest Tranches &a———————<",
                                " &eFirst &610M &ecoins yields &b2% &einterest.",
                                " &eFrom &610M &eto &620M &ecoins yields &b1% &einterest.",
                                "",
                                "&7Max interest: &6300.000",
                                "&a>————————————————————————————————<",
                                "",
                                "&7Max balance: &6100 Million Coins",
                                "",
                                "&7Cost: &65.000.000 coins"
                        )).build())
                .hierarchy(2)
                .build();
    }

    private BankUpgrade getDeluxeAccount() {
        return BankUpgrade.builder()
                .id("deluxe")
                .displayName("&dDeluxe Account")
                .bankLimit(250000000)
                .coinsCost(10000000)
                .itemCost(new HashMap<>())
                .upgradeInterestList(Arrays.asList(
                        new UpgradeInterest(2, 350000, 0, 9999999),
                        new UpgradeInterest(1, 350000, 10000000, 19999999),
                        new UpgradeInterest(0.5, 350000, 20000000, 29999999)

                ))
                .displayItem(GUIDisplayItem.builder()
                        .item(XMaterial.GOLD_INGOT)
                        .slot(14)
                        .description(Arrays.asList("&8Bank upgrade",
                                "",
                                "&a>——————— &6Interest Tranches &a———————<",
                                " &eFirst &610M &ecoins yields &b2% &einterest.",
                                " &eFrom &610M &eto &620M &ecoins yields &b1% &einterest.",
                                " &eFrom &620M &eto &630M &ecoins yields &b0.5% &einterest.",
                                "",
                                "&7Max interest: &6300.000",
                                "&a>————————————————————————————————<",
                                "",
                                "&7Max balance: &6250 Million Coins",
                                "",
                                "&7Cost: &610.000.000 coins"
                        )).build())
                .hierarchy(3)
                .build();
    }
}
