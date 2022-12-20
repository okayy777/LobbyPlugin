package de.ascasia.LobbySystem.Listener;

import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class onQuit implements Listener {
    public HashMap<String, Scoreboard> boards = new HashMap<String, Scoreboard>();;
    public PermMySQLGetter PermData = Main.getPlugin().PermData;
    public PlayerSQLGetter pdata = Main.getPlugin().Pdata;
    public LobbySQLGetter ldata = Main.getPlugin().Ldata;
    public FriendSQLGetter FriendData = Main.getPlugin().Fdata;
    public PartySQLGetter PartyData = Main.getPlugin().PAdata;
    public ServerSQLGetter ServerData = Main.getPlugin().ServerData;
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("");
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString().replace("-" , "");
        Location Loca = p.getLocation();
        double x = Loca.getX();
        double y = Loca.getY();
        double z = Loca.getZ();
        double yaw = Loca.getYaw();
        double pitch = Loca.getPitch();
        ldata.setPos(uuid , x ,y , z , yaw , pitch);
        //reader.uninject(e.getPlayer());
    }
}
