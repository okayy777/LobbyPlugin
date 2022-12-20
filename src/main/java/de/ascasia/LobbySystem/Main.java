package de.ascasia.LobbySystem;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.ascasia.LobbySystem.Items.Item;
import de.ascasia.LobbySystem.Listener.*;
import de.ascasia.LobbySystem.NPC.NPCevent;
import de.ascasia.LobbySystem.NPC.NPCs;
import de.ascasia.LobbySystem.commands.*;
import de.ascasia.LobbySystem.sql.*;
import de.ascasia.LobbySystem.utils.*;
import dev.sergiferry.playernpc.api.NPCLib;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;

public class Main extends JavaPlugin {

    private static Main plugin;

    public MySQL BSQL;
    public FriendMySQL FSQL;
    public SQLGetter Bdata;
    public FriendSQLGetter Fdata;
    public PlayerSQLGetter Pdata;
    public PlayerMySQL PSQL;
    public PartyMySQL PASQL;
    public PartySQLGetter PAdata;
    public ClanrMySQL CSQL;
    public LobbyMySQL LSQL;
    public LobbySQLGetter Ldata;
    public PermMySQL PermSQL;
    public PermMySQLGetter PermData;
    public ServerMySQL ServerSQL;
    public ServerSQLGetter ServerData;
    public Item item;
    public Sichtbar Sicht;
    public ScoreBoard ScBoard;
    public Color Farbe;
    public freunde Freunde;
    public int uptime;


    private static Date convertd(java.util.Date uDate) {
        Date sDate = new Date(uDate.getTime());
        return sDate;
    }


    public void onEnable() {






        saveDefaultConfig();

        plugin = this;



        this.BSQL = new MySQL();
        this.Bdata = new SQLGetter(this);
        this.FSQL = new FriendMySQL();
        this.Fdata = new FriendSQLGetter(this);
        this.PSQL = new PlayerMySQL();
        this.Pdata = new PlayerSQLGetter(this);
        this.PASQL = new PartyMySQL();
        this.PAdata = new PartySQLGetter(this);
        this.LSQL = new LobbyMySQL();
        this.Ldata = new LobbySQLGetter(this);
        this.PermSQL = new PermMySQL();
        this.PermData = new PermMySQLGetter(this);
        this.ServerSQL = new ServerMySQL();
        this.ServerData = new ServerSQLGetter(this);
        this.item = new Item();
        this.Sicht = new Sichtbar();
        this.ScBoard = new ScoreBoard();
        this.Farbe = new Color();
        this.Freunde = new freunde();



        SignConfig.setup();
        SignConfig.save();


        try {
            LSQL.connect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage("Konnte nicht verbunden werden");
        }

        try {
            NPCLib.getInstance().registerPlugin(this);
            PASQL.connect();
            BSQL.connect();
            FSQL.connect();
            PSQL.connect();
            PermSQL.connect();
            ServerSQL.connect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (LSQL.EntityisConnected()) {
            Ldata.createNPCTable();
            Ldata.createCoinTable();
            Ldata.createLobbyTable();
            Ldata.createSichtbarTable();
            Ldata.createCompassTable();
            for (int x = 0 ; x < 20 ; x++) {
                if (Ldata.NumExists(x)) {
                    String[] data = Ldata.npc_string_data(x);
                    double[] data2 = Ldata.npc_double_data(x);
                    //NPC.getNPC(data[0] , data[2], data[3] ,data[1] , data2[0], data2[1], data2[2], data2[3], data2[4]);
                }
            }
        }
        if (ServerSQL.ServerIsConnected()) {
            if (Main.getPlugin().getConfig().getString("Server.name").contains("00")) {
                int freeNum = 0;

                boolean Team = false;
                String name = "Lobby";
                if (Main.getPlugin().getConfig().getString("Server.gamemode").equalsIgnoreCase("TEAMLOBBY")) {
                    name = "TeamLobby";
                }
                if (Main.getPlugin().getConfig().getString("Server.gamemode").equalsIgnoreCase("DEVLOBBY")) {
                    name = "DevLobby";
                }
                for (int x = 1 ; x< 30 ; x++) {

                    if (!ServerData.exists(name + "-"+ x)) {
                        freeNum = x;
                        break;
                    }
                }
                Main.getPlugin().getConfig().set("Server.name" , name + "-" + freeNum);
                Main.getPlugin().saveConfig();
            }
            ServerData.createTable();
            String SName = Main.getPlugin().getConfig().getString("Server.name");
            String Gamemode = Main.getPlugin().getConfig().getString("Server.gamemode");
            LocalDateTime now = LocalDateTime.now();
            java.util.Date dt = new java.util.Date();
            Date DateNow = convertd(dt);
            Time TimeNow = new Time(dt.getTime());
            String IP = Bukkit.getServer().getIp();
            int online = Bukkit.getOnlinePlayers().size();
            if(ServerData.exists(SName)) {
                int uptime = ServerData.uptime(SName);
                ServerData.setServer(SName, IP , 0 , DateNow , TimeNow , Gamemode , online);
            } else {
                ServerData.setServer(SName, IP , 0 , DateNow , TimeNow, Gamemode, online);
            }

        }
        World w = Bukkit.getWorld("world");
        Server s = Bukkit.getServer();
        w.setDifficulty(Difficulty.PEACEFUL);
        w.setPVP(false);
        Main.getPlugin().getConfig().set("board.mode" , "online");
        Main.getPlugin().getConfig().set("board.time" , 0);
        Main.getPlugin().saveConfig();


        getServer().getMessenger().registerOutgoingPluginChannel(Main.getPlugin(), "BungeeCord");

        //Listener
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new onJoin(), this);
        pluginManager.registerEvents(new onQuit(), this);
        pluginManager.registerEvents(new ClickNPC(), this);
        pluginManager.registerEvents(new PlayerMove(), this);
        pluginManager.registerEvents(new onMessage(), this);
        pluginManager.registerEvents(new SignRightClick(), this);
        pluginManager.registerEvents(new RightClick(), this);
        pluginManager.registerEvents(new InventoryClick(), this);
        pluginManager.registerEvents(new onMove(), this);
        pluginManager.registerEvents(new onDamage(), this);
        pluginManager.registerEvents(new WeatherChange() , this);
        pluginManager.registerEvents(new NPCevent(), this);

        //Commands
        getCommand("npc").setExecutor(new npccommand());
        getCommand("signcreate").setExecutor(new SignCreate());
        getCommand("teleporter").setExecutor(new teleporter());
        getCommand("admin").setExecutor(new admin());
        getCommand("recht").setExecutor(new rechtecheck());
        getCommand("top").setExecutor(new top());
        getCommand("lobby").setExecutor(new Lobby());
        getCommand("hoff").setExecutor(new hoff());
        getCommand("gamemode").setExecutor(new gamemode());


        BukkitRunnable AutoRun = new BukkitRunnable() {
            @Override
            public void run() {
                String SName = Main.getPlugin().getConfig().getString("Server.name");
                int count = Bukkit.getOnlinePlayers().size();
                ServerData.setOnlinePlayer(SName , count);
                int uptime = ServerData.uptime(SName) + 1;
                ServerData.setUptime(SName , uptime);
                Bukkit.getWorld("world").setTime(6000);
                if (Bukkit.getOnlinePlayers().size() != 0) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Scoreboard board = p.getScoreboard();
                        ScBoard.update(p);
                    }
                }
            }
        };
        AutoRun.runTaskTimer(Main.getPlugin() , 0 , 20);

        BukkitRunnable ColorLoop = new BukkitRunnable() {
            @Override
            public void run() {
                Main.getPlugin().ScBoard.changeDisplayName();
            }
        };
        ColorLoop.runTaskTimer(Main.getPlugin() , 40 , 5);


    }

    public void onDisable() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                String[] server = Main.getPlugin().ServerData.OnlineServer(Main.getPlugin().getConfig().getString("Server.gamemode"));
                String picked = null;
                picked = server[0];

                out.writeUTF(picked);
                player.sendPluginMessage(Main.getPlugin(), "BungeeCord", out.toByteArray());
            }
        }

    }

    public static Main getPlugin() {
        return plugin;
    }


}
