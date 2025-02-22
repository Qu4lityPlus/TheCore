package com.qualityplus.skills.api.registry;

import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.api.listener.ExtraListener;
import com.qualityplus.skills.base.skill.Skill;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Objects;

public interface ListenerRegistrable extends Listener{
    List<Class<? extends ExtraListener>> getExtraListeners();
    List<Listener> getRegisteredListeners();

    default void registerListeners(Box box) {
        Bukkit.getPluginManager().registerEvents(this, box.plugin());

        getRegisteredListeners().add(this);

        for (Class<? extends ExtraListener> listener : getExtraListeners()) {
            ExtraListener instance = box.inject().createInstance(listener);

            instance.applySkill((Skill) this);

            Bukkit.getPluginManager().registerEvents(instance, box.plugin());

            getRegisteredListeners().add(instance);
        }
    }

    default void unregisterListeners() {
        getRegisteredListeners().stream().filter(Objects::nonNull).forEach(HandlerList::unregisterAll);

        getRegisteredListeners().clear();
    }

    void addExtraListener(Class<? extends ExtraListener> listener);
}
