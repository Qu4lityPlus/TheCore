package com.qualityplus.minions.base.gui.recipes;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.api.recipe.Recipe;
import com.qualityplus.minions.base.config.Messages;
import com.qualityplus.minions.base.gui.MinionGUI;
import com.qualityplus.minions.base.gui.main.MainMinionGUI;
import com.qualityplus.minions.base.gui.preview.MinionRecipePreviewGUI;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.recipes.MinionRecipeConfig;
import com.qualityplus.minions.util.MinionEggUtil;
import com.qualityplus.minions.util.MinionPlaceholderUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MinionsRecipesGUI extends MinionGUI {
    private final Map<Integer, RecipeSlot> slotsAndLevels = new HashMap<>();
    private final MinionRecipesGUIConfig config;
    private final MinionEntity minionEntity;

    public MinionsRecipesGUI(Box box, MinionEntity minionEntity) {
        super(box.files().inventories().minionRecipesGUIConfig.getSize(), getTitle(box.files().inventories().minionRecipesGUIConfig.getTitle(), minionEntity), box);

        this.config = box.files().inventories().minionRecipesGUIConfig;
        this.minionEntity = minionEntity;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        Minion minion = getMinion(minionEntity);

        if (minion != null) {
            List<IPlaceholder> upgradePlaceholders = MinionPlaceholderUtil
                    .getMinionPlaceholders(minionEntity.getMinionUniqueId())
                    .with(MinionPlaceholderUtil.getMinionPlaceholders(minion))
                    .get();

            setRecipes(minion);
            setItem(config.getGoBack(), upgradePlaceholders);
            setItem(config.getCloseGUI());
        }

        return inventory;
    }

    private void setRecipes(Minion minion) {

        PlaceholderBuilder minionPlaceholders = MinionPlaceholderUtil.getMinionPlaceholders(minion);

        for (Integer slot : config.getLevelSlotsMap().keySet()) {
            int level = config.getLevelSlotsMap().get(slot);

            RecipeStatus status = getRecipeStatus(minion, level);

            String message = getRecipeMessageStatus(status);

            List<IPlaceholder> placeholders = MinionPlaceholderUtil
                    .getMinionPlaceholders(minionEntity.getMinionUniqueId(), level)
                    .with(minionPlaceholders)
                    .with(new Placeholder("minion_recipe_status", message))
                    .get();

            setMinionItem(slot, placeholders);

            slotsAndLevels.put(slot, new RecipeSlot(status, level));
        }
    }

    private String getRecipeMessageStatus(RecipeStatus status) {
        Messages.MinionMessages messages = box.files().messages().minionMessages;

        return status.equals(RecipeStatus.CAN_BE_CRAFTED) ? messages.cantBeCraftedMinion : messages.canBeCraftedMinion;
    }

    private RecipeStatus getRecipeStatus(Minion minion, int level) {
        MinionRecipeConfig recipeConfig = minion.getRecipe(level);

        if (recipeConfig == null) return RecipeStatus.CANNOT_BE_CRAFTED;

        Recipe recipe = TheMinions.getApi().getRecipeProvider().getRecipe(recipeConfig.getRecipeId());

        return !recipeConfig.isEnabled() || recipe == null ? RecipeStatus.CANNOT_BE_CRAFTED : RecipeStatus.CAN_BE_CRAFTED;
    }

    private void setMinionItem(int slot, List<IPlaceholder> placeholders) {
        Optional<ItemStack> itemStack = MinionEggUtil.createFromExistent(box.files().config().minionEggItem, minionEntity.getMinionUniqueId());

        itemStack.ifPresent(item -> inventory.setItem(slot, ItemStackUtils.makeItem(config.getMinionItem(), placeholders, item)));
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
        } else if (slotsAndLevels.containsKey(slot)) {
            RecipeSlot recipeSlot = slotsAndLevels.getOrDefault(slot, null);

            if (recipeSlot == null) return;

            if (recipeSlot.getStatus().equals(RecipeStatus.CAN_BE_CRAFTED))
                player.openInventory(new MinionRecipePreviewGUI(box, minionEntity, recipeSlot.getLevel()).getInventory());
            else {
                player.closeInventory();
                player.sendMessage(StringUtils.color(box.files().messages().minionMessages.cantBeCraftedMinionMessage));
            }
        }
    }

    enum RecipeStatus{
        CAN_BE_CRAFTED,
        CANNOT_BE_CRAFTED
    }

    @Data
    @AllArgsConstructor
    static class RecipeSlot{
        private final RecipeStatus status;
        private final int level;
    }
}
