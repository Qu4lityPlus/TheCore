package com.qualityplus.minions.persistance.data.upgrade;

import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class AutomatedShippingEntity extends Document {
    private String id;
    private long soldItems;
    private double heldCoins;

    public void addSoldItems(long soldItems) {
        this.soldItems+=soldItems;
    }

    public void addHeldCoins(double heldCoins) {
        this.heldCoins+=heldCoins;
    }
}
