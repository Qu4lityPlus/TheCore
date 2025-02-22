package com.qualityplus.skills.gui.stats;

import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.player.PlayerUtils;
import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.api.effect.CommonObject;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.registry.Perks;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.stat.Stat;
import com.qualityplus.skills.base.stat.registry.Stats;
import com.qualityplus.skills.gui.SkillsGUI;
import com.qualityplus.skills.gui.main.MainGUI;
import com.qualityplus.skills.persistance.data.UserData;
import com.qualityplus.skills.util.SkillsItemStackUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public final class StatsAndPerksGUI extends SkillsGUI {
    private final StatsAndPerksGUIConfig config;
    private final GUIType type;
    private final UUID uuid;

    public StatsAndPerksGUI(Box box, Player player, int page, GUIType type) {
        super(box.files().inventories().statsAndPerksGUI, box);

        this.hasNext = Stats.values(CommonObject::isEnabled).stream().anyMatch(stat -> stat.getGuiOptions().getPage() > page);
        this.config = box.files().inventories().statsAndPerksGUI;
        this.uuid = player.getUniqueId();
        this.type = type;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(config);

        setItem(config.getCloseGUI());
        setItem(config.goBack);

        if (page > 1)
            setItem(config.previousPage);
        if (hasNext)
            setItem(config.nextPage);

        UserData data = box.service().getData(uuid).orElse(new UserData());

        if (type.equals(GUIType.STAT)) {
            setItem(config.statInfoItem, Collections.singletonList(new Placeholder("player", PlayerUtils.getPlayerName(uuid))));

            for (Stat stat : Stats.values(CommonObject::isEnabled)) {
                GUIOptions options = stat.getGuiOptions();

                if (options.getPage() != this.page) continue;

                inventory.setItem(options.getSlot(), SkillsItemStackUtil.makeItem(config.statItem, Arrays.asList(
                        new Placeholder("stat_displayname", stat.getDisplayName()),
                        new Placeholder("stat_description", stat.getFormattedDescription(data.getSkills().getLevel(stat.getId())))
                ), options));
            }
        } else {
            setItem(config.perkInfoItem, Collections.singletonList(new Placeholder("player", PlayerUtils.getPlayerName(uuid))));

            for (Perk perk : Perks.values(CommonObject::isEnabled)) {
                GUIOptions options = perk.getGuiOptions();

                if (options.getPage() != this.page) continue;

                inventory.setItem(options.getSlot(), SkillsItemStackUtil.makeItem(config.perkItem, Arrays.asList(
                        new Placeholder("perk_displayname", perk.getDisplayName()),
                        new Placeholder("perk_description", perk.getFormattedDescription(data.getSkills().getLevel(perk.getId())))
                ), options));
            }
        }

        setItem(config.switchMode, Collections.singletonList(new Placeholder("current_mode", type.equals(GUIType.STAT) ? box.files().messages().skillsMessages.statMode : box.files().messages().skillsMessages.perkMode)));

        return inventory;
    }


    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.goBack)) {
            player.openInventory(new MainGUI(box, player).getInventory());
        } else if (isItem(slot, config.previousPage) && page > 1) {
            player.openInventory(new StatsAndPerksGUI(box, player, page - 1, type).getInventory());
        } else if (isItem(slot, config.nextPage) && hasNext) {
            player.openInventory(new StatsAndPerksGUI(box, player, page + 1, type).getInventory());
        } else if (isItem(slot, config.nextPage) && hasNext) {
            player.openInventory(new StatsAndPerksGUI(box, player, page + 1, type).getInventory());
        } else if (isItem(slot, config.switchMode)) {
            player.openInventory(new StatsAndPerksGUI(box, player, 1, type.equals(GUIType.STAT) ? GUIType.PERK : GUIType.STAT).getInventory());
        }
    }

    public enum GUIType{
        PERK,
        STAT
    }
}
