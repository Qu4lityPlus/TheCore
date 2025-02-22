package com.qualityplus.bank.persistence.data;

import com.qualityplus.bank.base.config.BankUpgrades;
import com.qualityplus.bank.base.upgrade.BankUpgrade;
import com.qualityplus.bank.base.upgrade.UpgradeInterest;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data @EqualsAndHashCode(callSuper = true)
public final class BankData extends Document {
    private UUID uuid;
    private String name;
    private double money;
    private String bankUpgrade;
    private List<BankTransaction> transactionList;
    private double lastInterest;
    private long lastInterestTime;
    private long lastTimeUsedPersonal;

    public void addMoney(double amount) {
        money+=amount;
    }

    public void removeMoney(double amount) {
        money-=amount;
    }

    public void reset() {
        money = 0;
    }

    public double getCalculatedInterest(BankUpgrades upgrades) {
        BankUpgrade upgrade = upgrades.getUpgrade(this).orElse(null);

        if (upgrade == null) return 0;

        UpgradeInterest interest = upgrade.getInterest(money).orElse(null);

        if (interest == null) return 0;

        return Math.min(((interest.getInterestPercentage() * money) / 100), interest.getInterestLimitInMoney());
    }

    public double getLimit(BankUpgrades upgrades) {
        BankUpgrade upgrade = upgrades.getUpgrade(this).orElse(null);

        if (upgrade == null) return 0;

        return upgrade.getBankLimit();
    }

    public double getInterest(BankUpgrades upgrades) {
        return upgrades.getUpgrade(this)
                .map(upgrade -> upgrade.getInterest(money).map(UpgradeInterest::getInterestPercentage).orElse(0D))
                .orElse(0D);
    }

    public List<BankTransaction> getTransactionList() {
        if (transactionList == null) transactionList = new ArrayList<>();

        return transactionList;
    }

    public void addTrx(BankTransaction trx) {
        if (transactionList == null) transactionList = new ArrayList<>();
        transactionList.add(trx);
    }
}
