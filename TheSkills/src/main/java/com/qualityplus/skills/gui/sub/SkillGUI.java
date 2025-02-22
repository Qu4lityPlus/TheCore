package com.qualityplus.skills.gui.sub;

import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.skills.api.box.Box;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.gui.SkillsGUI;
import com.qualityplus.skills.gui.main.MainGUI;
import com.qualityplus.skills.persistance.data.UserData;
import com.qualityplus.skills.util.SkillsItemStackUtil;
import com.qualityplus.skills.util.SkillsPlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class SkillGUI extends SkillsGUI {
    private final SubGUIConfig config;
    private final Skill skill;

    private static int getMaxPage(Skill skill, Box box) {
        return (int)Math.ceil((double)skill.getMaxLevel() / (double)box.files().inventories().subGUIConfig.getLevelSlots().size());
    }

    public SkillGUI(Box box, Player player, Skill skill, int page) {
        super(box.files().inventories().subGUIConfig.getSize(),
              StringUtils.processMulti(box.files().inventories().subGUIConfig.getTitle(), Arrays.asList(
                      new Placeholder("category", skill.getDisplayName()),
                      new Placeholder("current_page", page),
                      new Placeholder("max_page", getMaxPage(skill, box) )
              )), box);

        this.hasNext = skill.getMaxLevel() > box.files().inventories().subGUIConfig.getLevelSlots().size() * page;
        this.config = box.files().inventories().subGUIConfig;
        this.uuid = player.getUniqueId();
        this.skill = skill;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        fillInventory(config);

        setItem(config.getCloseGUI());

        UserData data = box.service().getData(uuid).orElse(new UserData());

        double level = data.getSkills().getLevel(skill.getId());

        int maxPerPage = config.getLevelSlots().size();

        int count = (maxPerPage * page) - maxPerPage;

        inventory.setItem(config.getCategoryItem().getSlot(), SkillsItemStackUtil.makeItem(
                config.getCategoryItem(),
                SkillsPlaceholderUtil.getSkillsPlaceholders(data, skill),
                skill.getGuiOptions()));

        for (Integer slot : config.getLevelSlots()) {
            count++;

            if (count > skill.getMaxLevel()) break;

            Item item = count == level + 1 ? config.getInProgressItem() :
                        count > level ? config.getLockedItem() : config.getUnlockedItem();

            inventory.setItem(slot, getItem(item, data, skill, count));

        }

        if (page > 1)
            setItem(config.getPreviousPage());

        if (hasNext)
            setItem(config.getNextPage());

        setItem(config.getGoBack());

        return inventory;
    }

    private ItemStack getItem(Item item, UserData data, Skill skill, int level) {
        PlaceholderBuilder builder = SkillsPlaceholderUtil.getAllPlaceholders(data, skill, level);

        List<String> loreInGui = skill.getCachedGUI(level);

        return ItemStackUtils.makeItem(item, builder
                .with(new Placeholder("skill_info_gui", StringUtils.processMulti(loreInGui, builder.get())))
                .get());
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBack())) {
            player.openInventory(new MainGUI(box, player).getInventory());
        } else if (isItem(slot, config.getPreviousPage()) && page > 1) {
            player.openInventory(new SkillGUI(box, player, skill, page - 1).getInventory());
        } else if (isItem(slot, config.getNextPage()) && hasNext) {
            player.openInventory(new SkillGUI(box, player, skill, page + 1).getInventory());
        }
    }
}
