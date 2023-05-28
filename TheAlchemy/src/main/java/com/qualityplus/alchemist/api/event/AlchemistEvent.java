package com.qualityplus.alchemist.api.event;

import com.qualityplus.assistant.api.event.PlayerHelperEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract alchemy event
 */
public abstract class AlchemistEvent extends PlayerHelperEvent {
    /**
     *
     * @param who {@link Player}
     */
    public AlchemistEvent(@NotNull final Player who) {
        super(who);
    }
}
