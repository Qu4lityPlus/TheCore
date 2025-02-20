package com.qualityplus.dragon.base.game.ranking;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.TheDragonAPI;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.api.game.ranking.GameRanking;
import com.qualityplus.dragon.api.service.UserDBService;
import com.qualityplus.dragon.base.configs.Messages;
import com.qualityplus.dragon.base.game.player.EventPlayer;
import com.qualityplus.dragon.persistance.data.UserData;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public final class GameRankingImpl implements GameRanking {
    private final Map<Integer, EventPlayer> eventPlayerMap = new HashMap<>();
    private @Inject UserDBService userDBService;
    private @Inject Messages messages;

    @Override
    public List<IPlaceholder> getPlaceholders(EventPlayer player) {
        return PlaceholderBuilder.create()
                .with(new Placeholder("the_dragon_player_rank", getRankByPlayer(player)),
                      new Placeholder("the_dragon_player_got_new_record", getRecordMessage(player)),
                      new Placeholder("the_dragon_player_damage", player.getDamage()),
                      new Placeholder("the_dragon_player_reward_xp", getDragonXp()),
                      new Placeholder("the_dragon_last_attacker", getLastAttacker()))
                .with(getCommonPlaceholders())
                .get();
    }


    @Override
    public Optional<EventPlayer> getByRank(int ranking) {
        return Optional.ofNullable(eventPlayerMap.getOrDefault(ranking, null));
    }

    @Override
    public void refreshRanking() {
        final TheDragonAPI api = TheDragon.getApi();

        final List<EventPlayer> eventPlayers = new ArrayList<>(api.getUserService().getUsers());

        eventPlayers.sort((o1, o2) -> (int) (o2.getDamage() - o1.getDamage()));

        this.eventPlayerMap.clear();

        int ranking = 0;

        for (EventPlayer eventPlayer : eventPlayers) {
            this.eventPlayerMap.put(ranking, eventPlayer);
            ranking++;
        }
    }

    private String getRecordMessage(final EventPlayer player) {
        final Optional<UserData> dragonPlayer = userDBService.getData(player.getUuid());

        final double oldRecord = dragonPlayer.map(UserData::getRecord).orElse(0.0);

        return player.getDamage() > oldRecord ? messages.gameMessages.newRecordPlaceholder : "";
    }

    private List<IPlaceholder> getCommonPlaceholders() {

        return PlaceholderBuilder
                .create(IntStream.of(0,1,2)
                        .boxed()
                        .map(this::getNameByTopNumber)
                        .collect(Collectors.toList()))
                .with(IntStream.of(0, 1, 2)
                        .boxed()
                        .map(this::getDamageByTopNumber)
                        .collect(Collectors.toList()))
                .get();
    }

    private Placeholder getDamageByTopNumber(final int number) {
        final String key = String.format("the_dragon_player_top_%d_damage", number + 1);
        final double value = getByRank(number)
                .map(EventPlayer::getDamage)
                .orElse(0D);

        return new Placeholder(key, value);
    }

    private Placeholder getNameByTopNumber(final int number) {
        String notFound = messages.gameMessages.forbiddenPlayer;

        final String key = String.format("the_dragon_player_top_%d_name", number + 1);
        final String value = getByRank(number)
                .map(EventPlayer::getName)
                .orElse(notFound);

        return new Placeholder(key, value);
    }

    private String getLastAttacker() {
        return Optional.ofNullable(TheDragon.getApi().getUserService().getLast())
                .filter(Objects::nonNull)
                .map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                .orElse("");
    }


    private double getDragonXp() {
        return Optional.ofNullable(TheDragon.getApi().getDragonService().getActiveDragon())
                .map(TheDragonEntity::getXp)
                .orElse(0D);
    }

    private Integer getRankByPlayer(final EventPlayer eventPlayer) {
        return eventPlayerMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getName().equals(eventPlayer.getName()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0) + 1;
    }

}
