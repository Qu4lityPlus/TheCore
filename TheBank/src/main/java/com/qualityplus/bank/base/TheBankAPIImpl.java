package com.qualityplus.bank.base;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.bank.api.TheBankAPI;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Component
public final class TheBankAPIImpl implements TheBankAPI {
    private @Inject @Getter Plugin plugin;
}
