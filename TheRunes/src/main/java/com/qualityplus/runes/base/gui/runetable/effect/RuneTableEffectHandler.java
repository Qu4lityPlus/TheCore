package com.qualityplus.runes.base.gui.runetable.effect;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.time.Markable;
import com.qualityplus.assistant.util.time.HumanTime;
import com.qualityplus.runes.TheRunes;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.session.RuneSession;
import com.qualityplus.runes.base.gui.runetable.RuneTableGUI;
import com.qualityplus.runes.base.rune.Rune;
import com.qualityplus.runes.base.rune.RuneLevel;
import com.qualityplus.runes.base.session.RuneSessionImpl;
import com.qualityplus.runes.base.table.effects.RuneTableEffects;
import com.qualityplus.runes.util.RuneFinderUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class RuneTableEffectHandler implements EffectHandler<RuneTableGUI> {
    private final CompletableFuture<Void> future = new CompletableFuture<>();
    private final Map<UUID, Integer> tasksMap = new HashMap<>();
    private @Setter boolean hasBeenClosed = false;
    private final RuneSession session;
    private final Box box;
    private Markable time;
    private int effect;


    @Override
    public void handle(Player player, RuneTableGUI gui, Inventory inventory) {

        startBrewing(inventory);

        checkIfEnd(player, session, inventory);

        future.thenRun(() ->{

            if (hasBeenClosed) return;

            session.setFusing(false);

            player.openInventory(new RuneTableGUI(box,
                new RuneSessionImpl(player.getUniqueId(), getResult(player, session),
                        null,
                        null,
                        session.getRuneInstance())).getInventory());

        });

    }

    @Override
    public ItemStack getResult(Player player, RuneSession session) {
        ItemStack itemStack = RuneFinderUtil.getFinalItem(box, session);

        boolean bothRunes = session.getSessionResult().equals(RuneSession.SessionResult.BOTH_RUNES_SET);

        int level = bothRunes ? session.getRuneInstance().getLevel() * 2 : session.getRuneInstance().getLevel();

        boolean succeed = true;

        if (bothRunes) {
            int random = new Random().nextInt(100);

            Rune rune = session.getRuneInstance().getRune();

            if (random <= rune.getOptRuneLevel(level).map(RuneLevel::getSuccessChance).orElse(0D))
                succeed = false;
        }

        double xp = bothRunes ? box.files().config().xpForRuneAndRune.getOrDefault(level, 0D) :  box.files().config().xpForItemAndRune.getOrDefault(level, 0D);

        TheRunes.getApi().getLevelProvider().addXp(player, xp);

        List<IPlaceholder> placeholders = Arrays.asList(
                new Placeholder("rune_session_xp", xp),
                new Placeholder("rune_to_sacrifice_displayname", BukkitItemUtil.getName(session.getItemToSacrifice())),
                new Placeholder("rune_session_result_displayname", BukkitItemUtil.getName(session.getItemToSacrifice())));

        player.sendMessage(StringUtils.processMulti(bothRunes ? box.files().messages().runeMessages.addedXpFusing : box.files().messages().runeMessages.addedXpApplying, placeholders));

        if (!succeed)
            player.sendMessage(StringUtils.color(box.files().messages().runeMessages.youDidntSucceed));

        return !succeed ? null : itemStack;
    }

    @Override
    public void cancelFusing() {
        this.tasksMap.keySet().forEach(this::cancelTask);
    }

    private void startBrewing(Inventory inventory) {
        UUID uuid = UUID.randomUUID();

        RuneTableEffects standEffects = box.files().inventories().runeTableGUIConfig.getRuneTableEffects();

        this.time = new Markable(new HumanTime(4, HumanTime.TimeType.SECONDS).getEffectiveTime(), System.currentTimeMillis());

        this.tasksMap.put(uuid, Bukkit.getScheduler().runTaskTimer(box.plugin(), () -> {
            if (time.remainingTime() > 0) {
                standEffects
                        .getEffectList()
                        .get(effect)
                        .forEach(ef -> inventory.setItem(ef.getSlot(), ItemStackUtils.makeItem(ef.getItem())));

                effect = standEffects.getEffectList().containsKey(effect + 1) ? effect + 1 : 0;
            } else
                cancelTask(uuid);

        }, 0L, 3L).getTaskId());
    }

    private void cancelTask(UUID uuid) {
        Optional.ofNullable(tasksMap.getOrDefault(uuid, null)).ifPresent(Bukkit.getScheduler()::cancelTask);
    }

    private void checkIfEnd(Player player, RuneSession recipe, Inventory inventory) {
        UUID uuid = UUID.randomUUID();

        this.tasksMap.put(uuid, Bukkit.getScheduler().runTaskTimer(box.plugin(), () -> {
            if (time.remainingTime() < 0) {
                finish(player, recipe);
                clearAll(inventory);
                future.complete(null);
                cancelTask(uuid);
            }
        }, 0L, 0L).getTaskId());
    }

    private void clearAll(Inventory inventory) {
        this.tasksMap.keySet().forEach(this::cancelTask);

        this.time = new Markable(0,0);
        this.effect = 0;

        InventoryUtils.fillInventory(inventory, box.files().inventories().runeTableGUIConfig.getBackground());
    }

    private void finish(Player player, RuneSession session) {
        /*AlchemistBrewEvent event = new AlchemistBrewEvent(player, recipe);

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;
        */
    }
}
