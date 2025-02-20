package com.qualityplus.dragon.base.game.parts;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.api.game.part.GameEnd;
import com.qualityplus.dragon.api.game.ranking.GameRanking;
import com.qualityplus.dragon.api.game.reward.DragonReward;
import com.qualityplus.dragon.api.service.UserDBService;
import com.qualityplus.dragon.base.configs.DragonRewardsFile;
import com.qualityplus.dragon.base.configs.Messages;
import com.qualityplus.dragon.base.game.player.EventPlayer;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;

import java.util.*;

@Component
public final class GameEndImpl implements GameEnd {
    private @Inject DragonRewardsFile rewards;
    private @Inject GameRanking gameRanking;
    private @Inject Messages messages;

    @Override
    public void sendFinishMessage(final List<EventPlayer> players) {
        this.gameRanking.refreshRanking();

        players.forEach(this::managePlayer);
    }

    private void managePlayer(final EventPlayer player) {
        final List<String> finalMessage = StringUtils.processMulti(messages.setupMessages.newGameEndMessage, gameRanking.getPlaceholders(player));

        player.sendMessage(finalMessage);

        final TheDragonEntity theDragonEntity = TheDragon.getApi().getDragonService().getActiveDragon();

        final List<IPlaceholder> placeholders = Arrays.asList(
                new Placeholder("thedragon_player_reward_xp", theDragonEntity.getXp()),
                new Placeholder("player", player.getName()));

        getRewardByDamage(player).ifPresent(r -> executeCommands(r.getCommands(), placeholders));
        getSpecificRewardByDamage(theDragonEntity, player).ifPresent(r -> executeCommands(r.getCommands(), placeholders));
    }


    private Optional<DragonReward> getRewardByDamage(EventPlayer eventPlayer) {
        List<DragonReward> dragonRewards = new ArrayList<>(rewards.dragonRewards);
        dragonRewards.sort((o1, o2) -> o2.getDamageDone() - o1.getDamageDone());
        return dragonRewards.stream()
                .filter(dragonReward -> eventPlayer.getDamage() >= dragonReward.getDamageDone())
                .findFirst();
    }

    private Optional<DragonReward> getSpecificRewardByDamage(TheDragonEntity entity, EventPlayer eventPlayer) {

        if (this.rewards.rewardsPerEachDragon == null) {
            return Optional.empty();
        }

        final List<DragonReward> dragonRewards = new ArrayList<>(this.rewards.rewardsPerEachDragon.getOrDefault(entity.getId(), Collections.emptyList()));

        dragonRewards.sort((o1, o2) -> o2.getDamageDone() - o1.getDamageDone());

        return dragonRewards.stream()
                .filter(dragonReward -> eventPlayer.getDamage() >= dragonReward.getDamageDone())
                .findFirst();
    }

    private void executeCommands(final List<String> commands, final List<IPlaceholder> placeholders) {
        commands.forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), StringUtils.processMulti(command, placeholders)));
    }
}
