package de.ascasia.LobbySystem.Listener;

import de.ascasia.LobbySystem.obj.NPC;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class onMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        NPCLookAt(e.getPlayer());

        Location From = e.getFrom();
        Location To = e.getTo();
        World w = To.getWorld();

        boolean NoBlocks = false;

        Block block = w.getBlockAt(To.getBlockX() , 49 , To.getBlockZ());
        if (block !=null ) {
            if (block.getType().toString().equals("AIR")) {
                NoBlocks = true;
            }
        }

        if (NoBlocks) {
            Player p = e.getPlayer();
            double x = e.getTo().getX();
            double z = e.getTo().getZ();
            double newx = x;
            double newz = z;
            if (newx < 0) {
                newx = newx *-1;
            }
            if (newz < 0) {
                newz = newz *-1;
            }
            if ( newx > newz) {
                if (x < 0 ) {
                    p.setVelocity(new Vector(1 ,0, p.getVelocity().getZ()));
                }
                if (x > 0) {
                    p.setVelocity(new Vector(-1 ,0, p.getVelocity().getZ()));
                }
            } else if (newz > newx) {
                if (z < 0) {
                    p.setVelocity(new Vector(p.getVelocity().getX(), 0, 1));
                }
                if (z > 0) {
                    p.setVelocity(new Vector(p.getVelocity().getX(), 0, -1));
                }
            } else {
                if (x < 0 ) {
                    p.setVelocity(new Vector(1 ,0, p.getVelocity().getZ()));
                }
                if (x > 0) {
                    p.setVelocity(new Vector(-1 ,0, p.getVelocity().getZ()));
                }
                if (z < 0) {
                    p.setVelocity(new Vector(p.getVelocity().getX(), 0, 1));
                }
                if (z > 0) {
                    p.setVelocity(new Vector(p.getVelocity().getX(), 0, -1));
                }
            }
        }
    }

    public void NPCLookAt(Player player) {
        NPC.NPC_List.forEach((name, npc) -> {
            //The location of the NPC
            Location loc = npc.getNpc().getBukkitEntity().getLocation();
            //Calculate a new direction by subtracting the location of the player vector from the location vector of the npc
            loc.setDirection(player.getLocation().subtract(loc).toVector());

            //yaw and pitch used to calculate head movement
            float yaw = loc.getYaw();
            float pitch = loc.getPitch();

            //get the connection so we can send packets in NMS
            ServerGamePacketListenerImpl ps = ((CraftPlayer) player).getHandle().connection;

            //used for horizontal head movement
            ps.send(new ClientboundRotateHeadPacket(npc.getNpc(), (byte) ((yaw%360)*256/360)));
            //used for body movement and vertical head movement
            ps.send(new ClientboundMoveEntityPacket.Rot(npc.getNpc().getBukkitEntity().getEntityId(), (byte) ((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360),false));
        });
    }
}
