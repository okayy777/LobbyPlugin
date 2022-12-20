package de.ascasia.LobbySystem.Listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class onMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

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
}
