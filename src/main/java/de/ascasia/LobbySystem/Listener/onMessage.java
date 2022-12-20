package de.ascasia.LobbySystem.Listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onMessage implements Listener {
    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
    }
}
