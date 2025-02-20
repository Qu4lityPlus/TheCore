package com.qualityplus.runes.api.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface RuneSession {
    UUID getUuid();
    RuneInstance getRuneInstance();
    ItemStack getResult();
    ItemStack getItemToUpgrade();
    ItemStack getItemToSacrifice();
    SessionResult getSessionResult();

    boolean isFusing();

    void setFusing(boolean fusing);

    default boolean bothItemsAreSet() {
        return getSessionResult().equals(SessionResult.BOTH_RUNES_SET) || getSessionResult().equals(SessionResult.RUNE_AND_ITEM_SET);
    }

    @AllArgsConstructor
    public enum SessionResult{
        ONLY_ITEM_TO_UPGRADE(false),
        ONLY_ITEM_TO_SACRIFICE(false),
        RUNE_AND_ITEM_SET(false),

        BOTH_RUNES_SET(false),

        NOTHING_SET(false),
        /**
         * To Sacrifice item is not a rune
         */
        ERROR_CANNOT_COMBINE_THOSE_ITEMS(true),

        /**
         * Rune apply is different to item
         */
        INCOMPATIBLE_RUNE_APPLY(true),


        NO_RUNE_CRAFTING_LEVEL(true),

        /**
         * Runes are not of the same level
         */
        MUST_BE_SAME_TIER(true),
        /**
         * Runes are different
         */
        MUST_BE_SAME_TYPE(true),
        /**
         * Rune is incompatible
         */
        CANNOT_ADD_THAT_RUNE(true);


        private final @Getter boolean error;
    }
}
