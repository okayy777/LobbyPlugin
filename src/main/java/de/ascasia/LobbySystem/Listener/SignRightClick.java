package de.ascasia.LobbySystem.Listener;

import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.LobbySQLGetter;
import de.ascasia.LobbySystem.utils.SignConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignRightClick implements Listener {
    public LobbySQLGetter Lobby = Main.getPlugin().Ldata;

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onSignRightClick(PlayerInteractEvent e) {
        FileConfiguration SignConf = SignConfig.get();
        FileConfiguration conf = Main.getPlugin().getConfig();
        Player p = e.getPlayer();
        if (SignConf.contains(p.getName())) {
            //p.sendMessage(conf.getString("Prefix.system") +" 1");
            if (p.getItemInHand().getType().equals(Material.NETHER_STAR)) {
                p.sendMessage(conf.getString("Prefix.system") +" 2");
                Block x = e.getClickedBlock();
                Bukkit.broadcastMessage(String.valueOf(x.getType()));
                if (x.getType().equals(Material.LEGACY_SIGN)) {
                    p.sendMessage(conf.getString("Prefix.system") +" 3");
                    Lobby.createSignTable();
                    String Name = SignConf.getString(p.getName() +".NAME");
                    Location loca = x.getLocation();
                    Sign newSign = (Sign) loca.getBlock().getState();
                    newSign.setLine(0 , "§0-__-__-__-__-");
                    newSign.setLine(1 ,  "§9§l" +Name);
                    newSign.setLine(2 , "§c0§8/§c0");
                    newSign.setLine(3 , "§0_--_--_--_--_");
                    newSign.update();
                    int X = x.getX();
                    int Y = x.getY();
                    int Z = x.getZ();
                    String w = x.getWorld().getName();
                    Lobby.addSign(Name , X ,Y , Z , w);
                    p.sendMessage(conf.getString("Prefix.system") + "§5Sign created");
                    SignConf.set(p.getName() , null);
                    SignConfig.save();
                }
            }
        }
    }
}
