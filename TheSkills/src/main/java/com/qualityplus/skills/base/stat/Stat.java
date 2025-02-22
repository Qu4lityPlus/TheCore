package com.qualityplus.skills.base.stat;

import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.api.effect.CommonObject;
import com.qualityplus.skills.api.listener.ExtraListener;
import com.qualityplus.skills.api.registry.ListenerRegistrable;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.stat.registry.Stats;
import com.qualityplus.skills.persistance.data.UserData;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

@NoArgsConstructor
public abstract class Stat extends CommonObject implements ListenerRegistrable {
    public Stat(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double baseAmount) {
        super(id, enabled, displayName, description, skillGUIOptions, baseAmount);
    }

    @Override
    public void register() {
        Stats.registerNewStat(this);
    }

    @Override
    public void addExtraListener(Class<? extends ExtraListener> listener) {
        extraListeners.add(listener);
    }

    public double getStat(Player player, String id) {
        return TheSkills.getApi().getSkillsService().getData(player.getUniqueId())
                .map(UserData::getSkills)
                .map(userPerks -> userPerks.getLevel(id))
                .orElse(1D);
    }

    public double getStat(Player player) {
        return getStat(player, id);
    }
}
