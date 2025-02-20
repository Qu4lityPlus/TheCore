package com.qualityplus.collections.base.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Exclude;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.util.list.ListBuilder;
import com.qualityplus.collections.base.collection.Collection;
import com.qualityplus.collections.base.collection.category.Categories;
import com.qualityplus.collections.base.collection.executor.CollectionExecutor;
import com.qualityplus.collections.base.collection.gui.GUIOptions;
import com.qualityplus.collections.base.collection.rewards.CollectionsCommandRewards;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
@Configuration(path = "collections.yml")
@Header("================================")
@Header("       Collections      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class CollectionsFile extends OkaeriConfig {
    @Exclude
    private  static final String FARMING = Categories.FARMING.getId();
    @Exclude
    private static final String MINING = Categories.MINING.getId();
    @Exclude
    private static final String COMBAT = Categories.COMBAT.getId();
    @Exclude
    private static final String FORAGING = Categories.FORAGING.getId();
    @Exclude
    private static final String FISHING = Categories.FISHING.getId();

    public List<Collection> collections = new ListBuilder<Collection>()
            .with(getFarmingCategory())
            .with(getMiningCollection())
            .with(getCombatCollections())
            .with(getForagingCollections())
            .with(getFishingCollections())
            .get();

    public void modifyCollectionItemStack(String id, ItemStack itemStack) {
        collections.stream()
                .filter(collection -> collection.getId().equals(id))
                .findFirst()
                .ifPresent(collection -> collection.setCollectionExecutor(CollectionExecutor.ofItemStack(itemStack)));
    }


    private List<Collection> getFarmingCategory() {
        return Arrays.asList(
                fastCollection("cactus", "Cactus", XMaterial.CACTUS, FARMING).guiOptions(fastOptions(XMaterial.CACTUS, 10)).build(),
                fastCollection("carrot", "Carrot", XMaterial.CARROT, FARMING).guiOptions(fastOptions(XMaterial.CARROT, 11)).build(),
                fastCollection("cocoaBeans", "Cocoa Beans", XMaterial.COCOA_BEANS, FARMING).guiOptions(fastOptions(XMaterial.COCOA_BEANS, 12)).build(),
                fastCollection("leather", "Leather", XMaterial.LEATHER, FARMING).guiOptions(fastOptions(XMaterial.LEATHER, 13)).build(),
                fastCollection("feather", "Feather", XMaterial.FEATHER, FARMING).guiOptions(fastOptions(XMaterial.FEATHER, 14)).build(),
                fastCollection("melon", "Melon", XMaterial.MELON, FARMING).guiOptions(fastOptions(XMaterial.MELON, 15)).build(),
                fastCollection("redMushroom", "Red Mushroom", XMaterial.RED_MUSHROOM, FARMING).guiOptions(fastOptions(XMaterial.RED_MUSHROOM, 16)).build(),
                fastCollection("mutton", "Mutton", XMaterial.MUTTON, FARMING).guiOptions(fastOptions(XMaterial.MUTTON, 19)).build(),
                fastCollection("netherWart", "Nether Wart", XMaterial.NETHER_BRICK, FARMING).guiOptions(fastOptions(XMaterial.NETHER_BRICK, 20)).build(),
                fastCollection("potato", "Potato", XMaterial.POTATO, FARMING).guiOptions(fastOptions(XMaterial.POTATO, 21)).build(),
                fastCollection("pumpkin", "Pumpkin", XMaterial.PUMPKIN, FARMING).guiOptions(fastOptions(XMaterial.PUMPKIN, 22)).build(),
                fastCollection("rawChicken", "Raw Chicken", XMaterial.CHICKEN, FARMING).guiOptions(fastOptions(XMaterial.CHICKEN, 23)).build(),
                fastCollection("porkChop", "Raw Porkchop", XMaterial.PORKCHOP, FARMING).guiOptions(fastOptions(XMaterial.PORKCHOP, 24)).build(),
                fastCollection("porkChop", "Raw Rabbit", XMaterial.RABBIT, FARMING).guiOptions(fastOptions(XMaterial.RABBIT, 25)).build(),
                fastCollection("wheatSeeds", "Wheat Seeds", XMaterial.WHEAT_SEEDS, FARMING).guiOptions(fastOptions(XMaterial.WHEAT_SEEDS, 28)).build(),
                fastCollection("sugarCane", "Sugar Cane", XMaterial.SUGAR_CANE, FARMING).guiOptions(fastOptions(XMaterial.SUGAR_CANE, 29)).build()
        );
    }

    private List<Collection> getMiningCollection() {
        return Arrays.asList(
                fastCollection("coal", "Coal", XMaterial.COAL, MINING).guiOptions(fastOptions(XMaterial.COAL, 10)).build(),
                fastCollection("cobblestone", "Cobblestone", XMaterial.COBBLESTONE, MINING).guiOptions(fastOptions(XMaterial.COBBLESTONE, 11)).build(),
                fastCollection("diamond", "Diamond", XMaterial.DIAMOND, MINING).guiOptions(fastOptions(XMaterial.DIAMOND, 12)).build(),
                fastCollection("emerald", "Emerald", XMaterial.EMERALD, MINING).guiOptions(fastOptions(XMaterial.EMERALD, 13)).build(),
                fastCollection("endStone", "End Stone", XMaterial.END_STONE, MINING).guiOptions(fastOptions(XMaterial.END_STONE, 14)).build(),
                fastCollection("glowStoneDust", "Glowstone Dust", XMaterial.GLOWSTONE_DUST, MINING).guiOptions(fastOptions(XMaterial.GLOWSTONE_DUST, 15)).build(),
                fastCollection("goldIngot", "Gold Ingot", XMaterial.GOLD_INGOT, MINING).guiOptions(fastOptions(XMaterial.GOLD_INGOT, 16)).build(),
                fastCollection("gravel", "Gravel", XMaterial.GRAVEL, MINING).guiOptions(fastOptions(XMaterial.GRAVEL, 19)).build(),
                fastCollection("ice", "Ice", XMaterial.ICE, MINING).guiOptions(fastOptions(XMaterial.ICE, 20)).build(),
                fastCollection("ironIngot", "Iron Ingot", XMaterial.IRON_INGOT, MINING).guiOptions(fastOptions(XMaterial.IRON_INGOT, 21)).build(),
                fastCollection("lapizLazuli", "Lapiz Lazuli", XMaterial.LAPIS_LAZULI, MINING).guiOptions(fastOptions(XMaterial.LAPIS_ORE, 22)).build(),
                fastCollection("mycelium", "Mycelium", XMaterial.MYCELIUM, MINING).guiOptions(fastOptions(XMaterial.MYCELIUM, 23)).build(),
                fastCollection("netherQuartz", "Nether Quartz", XMaterial.NETHER_QUARTZ_ORE, MINING).guiOptions(fastOptions(XMaterial.NETHER_QUARTZ_ORE, 24)).build(),
                fastCollection("netherrack", "Netherrack", XMaterial.NETHERRACK, MINING).guiOptions(fastOptions(XMaterial.NETHERRACK, 25)).build(),
                fastCollection("obsidian", "Obsidian", XMaterial.OBSIDIAN, MINING).guiOptions(fastOptions(XMaterial.OBSIDIAN, 28)).build(),
                fastCollection("redSand", "Red Sand", XMaterial.RED_SAND, MINING).guiOptions(fastOptions(XMaterial.RED_SAND, 29)).build(),
                fastCollection("redstone", "Redstone", XMaterial.REDSTONE, MINING).guiOptions(fastOptions(XMaterial.REDSTONE, 30)).build(),
                fastCollection("sand", "Sand", XMaterial.SAND, MINING).guiOptions(fastOptions(XMaterial.SAND, 31)).build()
               );
    }

    private List<Collection> getCombatCollections() {
        return Arrays.asList(
                fastCollection("blazeRod", "blazeRod", XMaterial.BLAZE_ROD, COMBAT).guiOptions(fastOptions(XMaterial.BLAZE_ROD, 10)).build(),
                fastCollection("bone", "Bone", XMaterial.BONE, COMBAT).guiOptions(fastOptions(XMaterial.BONE, 11)).build(),
                fastCollection("enderPearl", "Ender Pearl", XMaterial.ENDER_PEARL, COMBAT).guiOptions(fastOptions(XMaterial.ENDER_PEARL, 12)).build(),
                fastCollection("ghastTear", "Ghast Tear", XMaterial.GHAST_TEAR, COMBAT).guiOptions(fastOptions(XMaterial.GHAST_TEAR, 13)).build(),
                fastCollection("gunpowder", "Gunpowder", XMaterial.GUNPOWDER, COMBAT).guiOptions(fastOptions(XMaterial.GUNPOWDER, 14)).build(),
                fastCollection("magmaCream", "Magma Cream", XMaterial.MAGMA_CREAM, COMBAT).guiOptions(fastOptions(XMaterial.MAGMA_CREAM, 15)).build(),
                fastCollection("rottenFlesh", "Rotten Flesh", XMaterial.ROTTEN_FLESH, COMBAT).guiOptions(fastOptions(XMaterial.ROTTEN_FLESH, 16)).build(),
                fastCollection("slimeBall", "Slime Ball", XMaterial.SLIME_BALL, COMBAT).guiOptions(fastOptions(XMaterial.SLIME_BALL, 19)).build(),
                fastCollection("spiderEye", "Spider Eye", XMaterial.SPIDER_EYE, COMBAT).guiOptions(fastOptions(XMaterial.SPIDER_EYE, 20)).build(),
                fastCollection("string", "String", XMaterial.STRING, COMBAT).guiOptions(fastOptions(XMaterial.STRING, 21)).build()
        );
    }

    private List<Collection> getForagingCollections() {
        return Arrays.asList(
                fastCollection("acaciaWood", "Acacia Wood", XMaterial.ACACIA_LOG, FORAGING).guiOptions(fastOptions(XMaterial.ACACIA_LOG, 10)).build(),
                fastCollection("birchWood", "Birch Wood", XMaterial.BIRCH_LOG, FORAGING).guiOptions(fastOptions(XMaterial.BIRCH_LOG, 11)).build(),
                fastCollection("darkOakWood", "Dark Oak Wood", XMaterial.DARK_OAK_LOG, FORAGING).guiOptions(fastOptions(XMaterial.DARK_OAK_LOG, 12)).build(),
                fastCollection("jungleWood", "Jungle Wood", XMaterial.JUNGLE_LOG, FORAGING).guiOptions(fastOptions(XMaterial.JUNGLE_LOG, 13)).build(),
                fastCollection("oakWood", "Oak Wood", XMaterial.OAK_LOG, FORAGING).guiOptions(fastOptions(XMaterial.OAK_LOG, 14)).build(),
                fastCollection("spruceWood", "Spruce Wood", XMaterial.SPRUCE_LOG, FORAGING).guiOptions(fastOptions(XMaterial.SPRUCE_LOG, 15)).build()
        );
    }

    private List<Collection> getFishingCollections() {
        return Arrays.asList(
                fastCollection("clay", "Clay", XMaterial.CLAY, FISHING).guiOptions(fastOptions(XMaterial.CLAY, 10)).build(),
                fastCollection("clownfish", "Clown Fish", XMaterial.TROPICAL_FISH, FISHING).guiOptions(fastOptions(XMaterial.TROPICAL_FISH, 11)).build(),
                fastCollection("inkSac", "Ink Sac", XMaterial.INK_SAC, FISHING).guiOptions(fastOptions(XMaterial.INK_SAC, 12)).build(),
                fastCollection("lilyPad", "Lily Pad", XMaterial.LILY_PAD, FISHING).guiOptions(fastOptions(XMaterial.LILY_PAD, 13)).build(),
                fastCollection("magmaFish", "End Stone", XMaterial.PLAYER_HEAD, FISHING).guiOptions(fastOptions(XMaterial.PLAYER_HEAD, 14)).build(),
                fastCollection("prismarineCrystals", "Prismarine Crystals", XMaterial.PRISMARINE_CRYSTALS, FISHING).guiOptions(fastOptions(XMaterial.PRISMARINE_CRYSTALS, 15)).build(),
                fastCollection("prismarineShards", "Prismarine Shards", XMaterial.PRISMARINE_SHARD, FISHING).guiOptions(fastOptions(XMaterial.PRISMARINE_SHARD, 16)).build(),
                fastCollection("pufferfish", "PufferFish", XMaterial.PUFFERFISH, FISHING).guiOptions(fastOptions(XMaterial.PUFFERFISH, 19)).build(),
                fastCollection("rawFish", "Raw Fish", XMaterial.COD, FISHING).guiOptions(fastOptions(XMaterial.COD, 20)).build(),
                fastCollection("rawSalmon", "Raw Salmon", XMaterial.SALMON, FISHING).guiOptions(fastOptions(XMaterial.SALMON, 21)).build(),
                fastCollection("sponge", "Sponge", XMaterial.SPONGE, FISHING).guiOptions(fastOptions(XMaterial.SPONGE, 22)).build()
        );
    }

    private GUIOptions fastOptions(XMaterial material, int slot, String... texture) {
        GUIOptions.GUIOptionsBuilder builder = GUIOptions.builder()
                .item(material)
                .slot(slot);
        try {
            String textureStr = texture[0];

            return builder
                    .texture(textureStr)
                    .build();
        } catch (Exception e) {
            return builder
                    .build();
        }

    }

    private Collection.CollectionBuilder fastCollection(String id, String displayName, XMaterial executor, String category) {
        return Collection.builder()
                .id(id)
                .enabled(true)
                .displayName(displayName)
                .maxLevel(9)
                .guiOptions(GUIOptions.builder().build())
                .commandRewards(new CollectionsCommandRewards(FastMap.listBuilder(Integer.class, CommandReward.class)
                        .put(1, Collections.singletonList(new CommandReward(CommandReward.CommandExecutor.CONSOLE, "lp user %player% permission set example.permission.1 true")))
                        .put(2, Collections.singletonList(new CommandReward(CommandReward.CommandExecutor.CONSOLE, "lp user %player% permission set example.permission.2 true")))
                        .build()))
                .xpRequirements(getRequirements())
                .collectionsInfoGUI(getInfo())
                .collectionsInfoMessage(FastMap.listBuilder(Integer.class, String.class).build())
                .category(category)
                .guiCommandsPerLevel(ImmutableMap.<Integer, CommandReward>builder()
                        .put(1, new CommandReward(CommandReward.CommandExecutor.PLAYER, "thecrafting preview reward_recipe")).build())
                .collectionExecutor(CollectionExecutor.ofMaterial(executor));
    }

    private Map<Integer, List<String>> getInfo() {
        return ImmutableMap.<Integer, List<String>>builder()
                .put(1, Collections.singletonList("&a%collection_displayname% %collection_level_roman% Recipes"))
                .put(2, Collections.singletonList("&b%collection_displayname% %collection_level_roman% Recipes"))
                .put(3, Collections.singletonList("&e%collection_displayname% %collection_level_roman% Recipes"))
                .put(4, Collections.singletonList("&d%collection_displayname% %collection_level_roman% Recipes"))
                .put(5, Collections.singletonList("&7%collection_displayname% %collection_level_roman% Recipes"))
                .put(6, Collections.singletonList("&8%collection_displayname% %collection_level_roman% Recipes"))
                .put(7, Collections.singletonList("&2%collection_displayname% %collection_level_roman% Recipes"))
                .put(8, Collections.singletonList("&c%collection_displayname% %collection_level_roman% Recipes"))
                .put(9, Collections.singletonList("&f%collection_displayname% %collection_level_roman% Recipes"))

                .build();
    }

    private Map<Integer, Double> getRequirements() {
        return ImmutableMap.<Integer, Double>builder()
                .put(0, 10D)
                .put(1, 15D)
                .put(2, 20D)
                .put(3, 50D)
                .put(4, 100D)
                .put(5, 200D)
                .put(6, 300D)
                .put(7, 400D)
                .put(8, 600D)
                .put(9, 700D)
                .put(10, 800D)
                .build();
    }
}
