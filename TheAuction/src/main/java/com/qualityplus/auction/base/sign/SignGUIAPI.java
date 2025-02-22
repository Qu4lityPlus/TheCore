package com.qualityplus.auction.base.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.qualityplus.assistant.api.event.SignCompletedEvent;
import com.qualityplus.assistant.api.sign.handler.SignCompleteHandler;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SignGUIAPI {
    private final SignCompleteHandler action;
    private PacketAdapter packetListener;
    private final List<String> lines;
    private LeaveListener listener;
    private final Plugin plugin;
    private final UUID uuid;
    private Sign sign;

    @Builder
    public SignGUIAPI(SignCompleteHandler action, List<String> withLines, UUID uuid, Plugin plugin) {
        this.lines = withLines;
        this.plugin = plugin;
        this.action = action;
        this.uuid = uuid;
    }

    public void open() {
        Player player = Bukkit.getPlayer(uuid);

        if(player == null) return;

        this.listener = new LeaveListener();

        int x_start = player.getLocation().getBlockX();

        int y_start = player.getLocation().getBlockY();

        int z_start = player.getLocation().getBlockZ();

        final Material material = XMaterial.OAK_WALL_SIGN.parseMaterial();
        while (!player.getWorld().getBlockAt(x_start, y_start, z_start).getType().equals(Material.AIR) &&
                !player.getWorld().getBlockAt(x_start, y_start, z_start).getType().equals(material)) {
            y_start--;
            if (y_start == 1)
                return;
        }
        player.getWorld().getBlockAt(x_start, y_start, z_start).setType(material);

        this.sign = (Sign)player.getWorld().getBlockAt(x_start, y_start, z_start).getState();
        this.sign.setEditable(true);
        int i = 0;
        for (String line : lines) {
            this.sign.getSide(Side.BACK).setLine(i, line);
            this.sign.getSide(Side.FRONT).setLine(i, line);

            i++;
        }

        this.sign.update(true, true);

        final WrappedChatComponent[] lines = new WrappedChatComponent[4];

        for (int y = 0; y < 4; y++)
        {
            try {
                lines[y] = WrappedChatComponent.fromText(this.lines.get(y));
            } catch (Exception e) {
                lines[y] = WrappedChatComponent.fromText("");
            }
        }

        final PacketContainer openSign = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        final BlockPosition position = new BlockPosition(x_start, y_start, z_start);

        openSign.getBlockPositionModifier().write(0, position);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSign);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 3L);

        Bukkit.getPluginManager().registerEvents(this.listener, plugin);
        registerSignUpdateListener();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private class LeaveListener implements Listener {
        @EventHandler
        public void onLeave(PlayerQuitEvent e) {
            if (e.getPlayer().getUniqueId().equals(SignGUIAPI.this.uuid)) {
                ProtocolLibrary.getProtocolManager().removePacketListener(SignGUIAPI.this.packetListener);
                HandlerList.unregisterAll(this);
                SignGUIAPI.this.sign.getBlock().setType(Material.AIR);
            }
        }
    }

    private void registerSignUpdateListener() {
        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        this.packetListener = new PacketAdapter(plugin, PacketType.Play.Client.UPDATE_SIGN) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPlayer().getUniqueId().equals(SignGUIAPI.this.uuid)) {
                    List<String> lines = Stream.of(0,1,2,3).map(line -> getLine(event, line)).collect(Collectors.toList());

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        manager.removePacketListener(this);

                        HandlerList.unregisterAll(SignGUIAPI.this.listener);

                        SignGUIAPI.this.sign.getBlock().setType(Material.AIR);

                        SignGUIAPI.this.action.onSignClose(new SignCompletedEvent(event.getPlayer(), lines));
                    });
                }
            }
        };
        manager.addPacketListener(this.packetListener);
    }

    private String getLine(PacketEvent event, int line){
        return Bukkit.getVersion().contains("1.8") ?
                ((WrappedChatComponent[])event.getPacket().getChatComponentArrays().read(0))[line].getJson().replaceAll("\"", "") :
                ((String[])event.getPacket().getStringArrays().read(0))[line];
    }
}
