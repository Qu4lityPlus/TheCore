package com.qualityplus.skills.gui.main;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.skill.registry.Skills;
import com.qualityplus.skills.gui.SkillsGUI;
import com.qualityplus.skills.gui.stats.StatsAndPerksGUI;
import com.qualityplus.skills.gui.sub.SkillGUI;
import com.qualityplus.skills.persistance.data.UserData;
import com.qualityplus.skills.util.SkillsItemStackUtil;
import com.qualityplus.skills.util.SkillsPlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class MainGUI extends SkillsGUI {
    private final MainGUIConfig config;
    private final String name;

    public MainGUI(Box box, Player player) {
        super(box.files().inventories().mainGUIConfig, box);

        this.config = box.files().inventories().mainGUIConfig;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(config);

        setItem(config.getCloseGUI());

        UserData data = box.service().getData(uuid).orElse(new UserData());

        for (Skill skill : Skills.values(Skill::isEnabled))
            Optional.ofNullable(skill.getGuiOptions()).ifPresent(option -> inventory.setItem(option.getSlot(), getItem(data, skill, option)));

        setItem(config.getPlayerInfoItem(), Collections.singletonList(new Placeholder("player", name)));

        Optional.ofNullable(config.getCustomGoBackItem()).ifPresent(this::setItem);

        return inventory;
    }

    private ItemStack getItem(UserData data, Skill skill, GUIOptions option) {
        return SkillsItemStackUtil.makeItem(config.getSkillsItem(), getPlaceholders(data, skill, option), option);
    }

    private List<IPlaceholder> getPlaceholders(UserData data, Skill skill, GUIOptions option) {
        return SkillsPlaceholderUtil.getAllPlaceholders(data, skill)
                .with(new Placeholder("skill_info_gui", StringUtils.processMulti(option.getMainMenuLore(), SkillsPlaceholderUtil.getAllPlaceholders(data, skill).get())))
                .get();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getPlayerInfoItem())) {
            player.openInventory(new StatsAndPerksGUI(box, player, 1, StatsAndPerksGUI.GUIType.STAT).getInventory());
        } else if (isItem(slot, config.getCustomGoBackItem())) {
            handleItemCommandClick(player, config.getCustomGoBackItem());
        } else {
            Optional<Skill> optionalSkill = Skills.values(Skill::isEnabled).stream().filter(s -> s.getGuiOptions().getSlot() == slot).findFirst();

            optionalSkill.ifPresent(s -> player.openInventory(new SkillGUI(box, player, s, 1).getInventory()));
        }

    }
}
