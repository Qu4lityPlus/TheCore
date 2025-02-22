package com.qualityplus.skills.base.reward;

import com.qualityplus.assistant.api.common.rewards.Reward;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class StatReward extends OkaeriConfig implements Reward {
    private String stat;
    private double amount;

    @Override
    public void execute(final Player player) {
        TheSkills.getApi()
                .getSkillsService()
                .getData(player.getUniqueId())
                .ifPresent(userData -> userData.getSkills().addLevel(stat, amount));
    }
}
