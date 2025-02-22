package com.qualityplus.collections.persistance.data.user;

import com.qualityplus.assistant.api.data.Levellable;
import com.qualityplus.assistant.api.data.Progressable;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.collection.registry.CollectionsRegistry;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public final class UserCollections extends Document implements Levellable<String, Integer>, Progressable<String, Double> {
    private Map<String, Integer> level = new HashMap<>();
    private Map<String, Double> xp = new HashMap<>();

    public void fillIfEmpty() {
        CollectionsRegistry.values().stream().map(Collection::getId).forEach(skill -> level.putIfAbsent(skill, 0));
        CollectionsRegistry.values().stream().map(Collection::getId).forEach(skill -> xp.putIfAbsent(skill, 0D));
    }

    @Override
    public Integer getDefault() {
        return 0;
    }

    @Override
    public Double getDefaultXp() {
        return 0D;
    }
}