package com.qualityplus.bank.base.upgrade;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.payable.ItemPayable;
import com.qualityplus.bank.api.gui.GUIDisplayItem;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data @EqualsAndHashCode(callSuper = true)
public final class BankUpgrade extends OkaeriConfig implements ItemPayable {
    private String id;
    private String displayName;
    private double bankLimit;
    private double coinsCost;
    private Map<XMaterial, Integer> itemCost;
    private List<UpgradeInterest> upgradeInterestList;
    private int hierarchy;
    private GUIDisplayItem displayItem;

    @Builder
    public BankUpgrade(String id, String displayName, double bankLimit, double coinsCost, Map<XMaterial, Integer> itemCost, List<UpgradeInterest> upgradeInterestList, GUIDisplayItem displayItem,
                       int hierarchy) {
        this.id = id;
        this.hierarchy = hierarchy;
        this.displayName = displayName;
        this.bankLimit = bankLimit;
        this.coinsCost = coinsCost;
        this.displayItem = displayItem;
        this.itemCost = itemCost;
        this.upgradeInterestList = upgradeInterestList;
    }

    public Optional<UpgradeInterest> getInterest(Double bankMoney) {
        return upgradeInterestList.stream().filter(interest -> bankMoney >= interest.getFrom() && bankMoney <= interest.getTo()).findFirst();
    }
}
