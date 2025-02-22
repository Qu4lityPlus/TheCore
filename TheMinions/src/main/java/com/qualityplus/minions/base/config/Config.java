package com.qualityplus.minions.base.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.config.ConfigDatabase;
import com.qualityplus.assistant.api.gui.LoreWrapper;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Comment;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.CustomKey;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.minions.base.minions.entity.message.RandomMessage;
import com.qualityplus.minions.base.minions.entity.status.MinionStatus;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Configuration()
@Header("================================")
@Header("       Config      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Config extends OkaeriConfig {
    public String prefix = "[TheMinions] ";

    @CustomKey("configDatabase")
    @Comment("Database Configuration")
    @Comment("Allowed Database Types")
    @Comment("- H2")
    @Comment("- FLAT")
    @Comment("- MYSQL")
    @Comment("- REDIS")
    public ConfigDatabase configDatabase = new ConfigDatabase();

    @CustomKey("loreWrapper")
    @Comment("- wrapLength = After how many characters in a lore you want")
    @Comment("               to separate it.")
    @Comment("- wrapStart = After line is separated what character do you")
    @Comment("              want to start the new line with.")
    public LoreWrapper loreWrapper = new LoreWrapper(50, "&7");

    public Item minionEggItem = ItemBuilder.of(XMaterial.STONE, 1, "&9%minion_egg_egg_displayname%", Arrays.asList("&7%minion_description%",
                    "",
                    "&7Time Between Actions: &a%minion_time_between_actions%s", "&7Max Storage: &a%minion_max_storage%", "&7Resources Generated: &b%minion_resources_generated%", "", "&9&lRARE"))
            .build();

    public Item skinItem = ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "&a%minion_skin_display_name%", Collections.singletonList("&7%minion_skin_lore%")).build();

    @CustomKey("maxMinionsPerPlayer")
    @Comment("maxMinionsPerPlayer = This is the max amount of minions that a player can place")
    @Comment("Take in count this is the max amount per all players, then you need to set permissions")
    @Comment("for each player, e.g: 'theminions.place_amount.1' if you want player have 1 minion")
    @Comment("'theminions.place_amount.2' if you want he to place just two and so")
    public int maxMinionsPerPlayer = 15;

    public Map<MinionStatus, List<RandomMessage>> messages = FastMap.listBuilder(MinionStatus.class, RandomMessage.class)
            .put(MinionStatus.IDEAL_LAYOUT, List.of(new RandomMessage(Collections.singletonList("          "), 100)))
            .put(MinionStatus.INVALID_LAYOUT, List.of(new RandomMessage(Arrays.asList("&c/!\\", "&cInvalid Layout!"), 100)))
            .put(MinionStatus.STORAGE_FULL, List.of(new RandomMessage(Arrays.asList("&c/!\\", "&cStorage limit reached!"), 100)))
            .build();
}
