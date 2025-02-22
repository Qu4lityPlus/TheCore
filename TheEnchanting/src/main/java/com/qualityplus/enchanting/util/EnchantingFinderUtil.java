package com.qualityplus.enchanting.util;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.block.BlockUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class EnchantingFinderUtil {
    public int getItemLevel(final ItemStack itemStack, final Enchantment enchantment) {
        if (itemStack == null || enchantment == null) return 0;

        ItemMeta meta = itemStack.getItemMeta();

        return Optional.ofNullable(meta).map(m -> m.getEnchantLevel(enchantment)).orElse(0);
    }

    public int getBookShelfPower(Player player) {
        return getBookShelfPower(player.getLocation());
    }

    public int getBookShelfPower(Location location) {
        return (int) getBlocksInRadius(location, 6).stream()
                .filter(block -> !BlockUtils.isNull(block))
                .filter(block -> block.getType().equals(XMaterial.BOOKSHELF.parseMaterial()))
                .count();
    }

    private static List<Block> getBlocksInRadius(Location location, int radius) {
        List<Block> inRadius = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++)
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++)
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++)
                    if (location.getWorld() != null) inRadius.add(location.getWorld().getBlockAt(x, y, z));
        return inRadius;
    }

    private Block getBlockAt(World world, int x, int y, int z) {
        return world.getBlockAt(x, y, z);
    }
}
