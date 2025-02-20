package com.qualityplus.minions.base.gui.preview;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.recipe.Recipe;
import com.qualityplus.minions.base.gui.MinionGUI;
import com.qualityplus.minions.base.gui.main.MainMinionGUI;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.recipes.MinionRecipeConfig;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.util.MinionPlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MinionRecipePreviewGUI extends MinionGUI {
    private final MinionRecipePreviewGUIConfig config;
    private final MinionEntity minionEntity;
    private final int level;


    public MinionRecipePreviewGUI(Box box, MinionEntity minionEntity, int level) {
        super(box.files().inventories().recipePreviewGUIConfig.getSize(), getTitle(box.files().inventories().recipePreviewGUIConfig.getTitle(), minionEntity), box);

        this.config = box.files().inventories().recipePreviewGUIConfig;
        this.minionEntity = minionEntity;
        this.level = level;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        Minion minion = getMinion();

        if (minion != null) {
            List<IPlaceholder> upgradePlaceholders = MinionPlaceholderUtil
                    .getMinionPlaceholders(minionEntity.getMinionUniqueId())
                    .with(MinionPlaceholderUtil.getMinionPlaceholders(minion))
                    .get();

            setRecipeItems(minion, config.getRecipeSlots(), config.getResultSlot());
            setItem(config.getGoBack(), upgradePlaceholders);
            setItem(config.getCloseGUI());
        }

        return inventory;
    }

    private void setRecipeItems(Minion minion, List<Integer> recipeSlots, int resultSlot) {
        MinionRecipeConfig minionRecipe = minion.getRecipe(level);

        if (minionRecipe == null) return;

        Recipe recipe = TheMinions.getApi()
                .getRecipeProvider()
                .getRecipe(minionRecipe.getRecipeId());

        recipe.getIngredients().entrySet().stream()
                .filter(entry -> !BukkitItemUtil.isNull(entry.getValue()))
                .forEach(entry -> inventory.setItem(recipeSlots.get(entry.getKey() - 1), entry.getValue()));

        inventory.setItem(resultSlot, recipe.getResult());
    }

    private Minion getMinion() {
        String id = minionEntity.getData().map(MinionData::getMinionId).orElse(null);

        if (id == null) return null;

        return Minions.getByID(id);
    }


    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        event.setCancelled(true);

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBack())) {
            player.openInventory(new MainMinionGUI(box, minionEntity).getInventory());
        }
    }
}
