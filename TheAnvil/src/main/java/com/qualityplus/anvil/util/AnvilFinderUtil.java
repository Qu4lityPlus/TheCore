package com.qualityplus.anvil.util;

import com.qualityplus.anvil.TheAnvil;
import com.qualityplus.anvil.api.session.AnvilSession;
import com.qualityplus.anvil.api.session.AnvilSession.SessionResult;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


@UtilityClass
public class AnvilFinderUtil {
    public SessionResult getAnswer(AnvilSession session) {
        return TheAnvil.getApi().getProvider().getAnswer(session);
    }

    public double getMoneyCost(AnvilSession session) {
        return TheAnvil.getApi().getProvider().getMoneyCost(session);
    }

    public double getLevelsCost(AnvilSession session) {
        return TheAnvil.getApi().getProvider().getLevelsCost(session);
    }

    public boolean dontHavePermission(AnvilSession session, Player player) {
        return TheAnvil.getApi().getProvider().dontHavePermission(session, player);
    }

    public ItemStack getFinalItem(AnvilSession session) {
        return TheAnvil.getApi().getProvider().getFinalItem(session);
    }
}
