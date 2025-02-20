package com.qualityplus.anvil.base.session;

import com.qualityplus.anvil.api.session.AnvilSession;
import com.qualityplus.anvil.util.AnvilFinderUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@AllArgsConstructor
public final class AnvilSessionImpl implements AnvilSession {
    private ItemStack result;
    private ItemStack itemToUpgrade;
    private ItemStack itemToSacrifice;

    @Override
    public SessionResult getSessionResult() {
        return AnvilFinderUtil.getAnswer(this);
    }
}
