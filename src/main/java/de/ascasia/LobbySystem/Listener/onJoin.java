package de.ascasia.LobbySystem.Listener;

import de.ascasia.LobbySystem.Items.Item;
import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.*;
import de.ascasia.LobbySystem.utils.ScoreBoard;
import de.ascasia.LobbySystem.utils.Sichtbar;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class onJoin implements Listener {
    public HashMap<String, Scoreboard> boards = new HashMap<String, Scoreboard>();
    private Item ItemGetter = Main.getPlugin().item;
    public PermMySQLGetter PermData = Main.getPlugin().PermData;
    public PlayerSQLGetter pdata = Main.getPlugin().Pdata;
    public LobbySQLGetter ldata = Main.getPlugin().Ldata;
    public FriendSQLGetter FriendData = Main.getPlugin().Fdata;
    public PartySQLGetter PartyData = Main.getPlugin().PAdata;
    public ServerSQLGetter ServerData = Main.getPlugin().ServerData;
    public Sichtbar Sicht = Main.getPlugin().Sicht;
    public ScoreBoard setBoard = Main.getPlugin().ScBoard;

    @EventHandler
    @SuppressWarnings("deprecation")
    public void  onJoin(PlayerJoinEvent e) {
        //Player infos
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString().replace("-" , "");
        String Name = p.getName();
        String Rang = pdata.getRang(uuid);


        String Prefix = PermData.getPrefix(Rang);
        //create data
        if (!ldata.exists(uuid)) {
            World w = p.getWorld();
            int x = w.getSpawnLocation().getBlockX();
            int y = w.getSpawnLocation().getBlockY();
            int z = w.getSpawnLocation().getBlockZ();
            int yaw = (int) w.getSpawnLocation().getYaw();
            int pitch = (int) w.getSpawnLocation().getPitch();
            p.teleport(p.getWorld().getSpawnLocation());
            ldata.addPlayerCoins(uuid , 0);
            ldata.addPlayer(uuid, x, y, z, yaw, pitch);
            ldata.addPlayerSicht(uuid);
        }

        //Items

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                p.setGameMode(GameMode.ADVENTURE);
                ItemGetter.giveCompass(p , 0);
                ItemGetter.givePlayerToggle(p, 2);
                ItemGetter.giveStoryInfo(p, 4);
                ItemGetter.giveLobbySwicht(p,6);
                ItemGetter.giveProfil(p, 8);
                if (p.hasPermission("ascasia.Nick")) {
                    ItemGetter.giveNickTool(p , 7);
                }
                if (p.hasPermission("ascasia.Fly")) {
                    ItemGetter.giveFlyTool(p , 1);
                    if (ldata.Fly(uuid)) {
                        p.setFlying(true);
                        p.setAllowFlight(true);
                    }
                }
            }
        };

        runnable.runTaskLater(Main.getPlugin(), 20 );
        p.getInventory().clear();

        //Sicht
        if (ldata.Mode(uuid).equals("NOONE")) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(pl);
            }
        } else if (ldata.Mode(uuid).equals("CHOOSEN")) {
            Sicht.setSicht(p);
        } else if (ldata.Mode(uuid).equals("ALL")) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                p.showPlayer(pl);
            }
        }
        BukkitRunnable runnable2 = new BukkitRunnable() {
            @Override
            public void run() {
                setBoard.setScoreBoard(p);
                double[] pos = ldata.getPos(uuid);
                Location Loca = new Location( Bukkit.getWorld("world"), pos[0] , pos[1] ,pos[2] , (float)pos[3] , (float) pos[4]);
                p.teleport(Loca);
            }
        };
        runnable2.runTaskLater(Main.getPlugin(), 4);

        e.setJoinMessage("");

        //NPCs
        /*
        if (NPC.getNPC() == null) {
            return;
        }
        if (NPC.getNPC().isEmpty()) {
            return;
        }
        NPC.addJoinPacket(e.getPlayer());

        PacketReader reader = new PacketReader();
        reader.inject(e.getPlayer());

         */

        //SQL



    }


}
