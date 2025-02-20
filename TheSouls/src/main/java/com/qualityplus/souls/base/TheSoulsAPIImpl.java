package com.qualityplus.souls.base;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.souls.api.TheSoulsAPI;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Component
public final class TheSoulsAPIImpl implements TheSoulsAPI {
    private @Inject @Getter Plugin plugin;
}
