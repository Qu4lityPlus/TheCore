package com.qualityplus.collections.base.collection.category;

import com.qualityplus.collections.base.collection.gui.GUIOptions;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class CollectionCategory extends OkaeriConfig {
    private final String id;
    private final String displayName;
    private final GUIOptions guiOptions;

    @Builder
    public CollectionCategory(String id, String displayName, GUIOptions options) {
        this.id = id;
        this.guiOptions = options;
        this.displayName = displayName;
    }
}
