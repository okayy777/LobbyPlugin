package de.ascasia.LobbySystem.Listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location pLocation = p.getLocation();
        float yaw = p.getLocation().getYaw();
        /*
        if (NPC.getNPC() != null) {
            for (EntityPlayer ep : NPC.getNPC()) {
                NPC.Rotate(ep , p);
            }
        }

         */

    }
}
