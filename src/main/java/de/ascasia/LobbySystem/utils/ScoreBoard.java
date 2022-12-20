package de.ascasia.LobbySystem.utils;

import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.util.HashMap;

public class ScoreBoard {
    public PermMySQLGetter PermData = Main.getPlugin().PermData;
    public PlayerSQLGetter pdata = Main.getPlugin().Pdata;
    public LobbySQLGetter ldata = Main.getPlugin().Ldata;
    public FriendSQLGetter FriendData = Main.getPlugin().Fdata;
    public PartySQLGetter PartyData = Main.getPlugin().PAdata;
    public ServerSQLGetter ServerData = Main.getPlugin().ServerData;

    public HashMap<Player, Scoreboard> boards = new HashMap<Player, Scoreboard>();

    public int state;


    @SuppressWarnings("deprecation")
    public void setScoreBoard(Player p) {

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        String uuid = p.getUniqueId().toString().replace("-" , "");
        String Rang = pdata.getRang(uuid);
        String Prefix = PermData.getPrefix(Rang);
        DecimalFormat df = new DecimalFormat("#.##");
        double OnTime = pdata.getOnlineTime(uuid) ;
        double Ontime = OnTime / 3600;
        String ontime = df.format(Ontime);

        String F = FriendData.Friends(uuid);
        int online = 0;
        int count = 0;
        if (F != null) {
            String[] Friends = F.split(",");
            for (int x = 0; x < Friends.length; ) {
                if (pdata.isOnline(Friends[x])) {
                    online++;
                    x++;
                } else {
                    x++;
                }
            }
            count = Friends.length;
        }

        for (Team teams : board.getTeams()) {
            if (teams.getName().length() < 4) {
                teams.unregister();
            }
        }


        for (Objective objs :  board.getObjectives()) {
            objs.unregister();
        }


        Objective o = board.getObjective(DisplaySlot.SIDEBAR);
        o = board.registerNewObjective( uuid+ "_Side", "dummy");
        o.setDisplayName("   §b§lOKAY §f§lNETWORK   ");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        Team friends = board.registerNewTeam("f");
        friends.setPrefix("");
        friends.setSuffix("");
        friends.addEntry("§8/§f");
        Team clan = board.registerNewTeam("c1");
        clan.setPrefix("");
        clan.setSuffix("");
        clan.addEntry("§f§8/§f");
        Team Coins = board.registerNewTeam("c2");
        Coins.setPrefix("");
        Coins.setSuffix("");
        Coins.addEntry(" §6Coins");
        Team Time = board.registerNewTeam("t");
        Time.setPrefix("");
        Time.setSuffix("");
        Time.addEntry("§8§7");
        Team Zeit = board.registerNewTeam("z");
        Zeit.setPrefix("");
        Zeit.setSuffix("");
        Zeit.addEntry(" §aStunden");
        Team Rang1 = board.registerNewTeam("r");
        Rang1.setPrefix(Prefix);
        Rang1.setSuffix("");
        Rang1.addEntry("§8§7§6");
        Score spacer1 = o.getScore("§r§9§r");
        spacer1.setScore(20);
        Score Rank = o.getScore("§f§lRang:");
        Rank.setScore(19);
        String pref = Prefix.replace("§7●", "");
        Score rang1 = o.getScore("§8§7§6");
        rang1.setScore(18);
        Score spacer2 = o.getScore("§r§8§r");
        spacer2.setScore(17);
        Score time = o.getScore("§8§7");
        time.setScore(16);
        Time.setPrefix("§f§lOnlinetime:");
        Score zeit = o.getScore(" §aStunden");
        zeit.setScore(15);
        Zeit.setPrefix("§7" +  ontime);
        Score spacer9 = o.getScore("§r§7§r");
        spacer9.setScore(14);
        Score coins = o.getScore("§f§lCoins:");
        coins.setScore(13);
        Score coinsAmount = o.getScore(" §6Coins");
        coinsAmount.setScore(12);
        Coins.setPrefix("§e" + ldata.Coins(uuid));
        Score spacer3 = o.getScore("§r§6§r");
        spacer3.setScore(11);
        Score Friend = o.getScore("§b§lFreunde: ");
        Friend.setScore(10);
        Score fcount = o.getScore("§8/§f");
        fcount.setScore(9);
        friends.setPrefix("§f" + online);
        friends.setSuffix("§f" + count);
        Score spacer4 = o.getScore("§r§5§r");
        spacer4.setScore(6);
        Score Clan = o.getScore("§9§lClan:");
        Clan.setScore(5);
        Score Ccount = o.getScore("§f§8/§f");
        Ccount.setScore(4);
        clan.setPrefix("§f" + "SOON");
        clan.setSuffix("§f" + "SOON");
        Score spacer5 = o.getScore("§r§4§r");
        spacer5.setScore(3);



        p.setScoreboard(board);
        if (boards.containsValue(p)) {
            boards.replace(p, board);
        } else {
            boards.put(p, board);
        }

    }


    public void update(Player p) {
        FileConfiguration config = Main.getPlugin().getConfig();

        String uuid = p.getUniqueId().toString().replace("-" , "");
        String Rang = pdata.getRang(uuid);
        String Prefix = PermData.getPrefix(Rang);
        Scoreboard board = boards.get(p);
        if (board != null) {

            if (config.getString("board.mode").equals("online")) {
                Team mode = board.getTeam("t");
                mode.setPrefix("§f§lOnlinetime:");
                double OnTime = pdata.getOnlineTime(uuid);
                double Ontime = OnTime / 3600;
                DecimalFormat df = new DecimalFormat("#.##");
                String ontime = df.format(Ontime);
                Team Zeit = board.getTeam("z");
                Zeit.setPrefix("§7" + ontime);
                int time = config.getInt("board.time");
                if (time == 30) {
                    config.set("board.time", 0);
                    config.set("board.mode", "play");
                    Main.getPlugin().saveConfig();
                } else {
                    config.set("board.time", time + 1);
                    Main.getPlugin().saveConfig();
                }
            } else if (config.getString("board.mode").equals("play")) {
                Team mode = board.getTeam("t");
                mode.setPrefix("§f§lPlaytime:");
                //double OnTime = pdata.getPlayTime(uuid) ;
                //double Ontime = OnTime / 3600;
                //DecimalFormat df = new DecimalFormat("#.##");
                //String ontime = df.format(Ontime);
                Team Zeit = board.getTeam("z");
                Zeit.setPrefix("§7" + 0);
                int time = config.getInt("board.time");
                if (time == 30) {
                    config.set("board.time", 0);
                    config.set("board.mode", "online");
                    Main.getPlugin().saveConfig();
                } else {
                    config.set("board.time", time + 1);
                    Main.getPlugin().saveConfig();
                }
            }
            String F = FriendData.Friends(uuid);
            int online = 0;
            if (F != null) {
                String[] Friends = F.split(",");
                for (int x = 0; x < Friends.length; ) {
                    if (pdata.isOnline(Friends[x])) {
                        online++;
                        x++;
                    } else {
                        x++;
                    }
                }
                if (Friends.length != 0) {
                    int count = Friends.length;
                    Team friends = board.getTeam("f");
                    if (online == 0) {
                        friends.setPrefix("§f" + 0);
                    } else {
                        friends.setPrefix("§f" + online);
                    }
                    friends.setSuffix("§f" + count);
                } else {
                    Team friends = board.getTeam("f");
                    friends.setPrefix("§f" + 0);
                    friends.setSuffix("§f" + 0);

                }
            }
            Team rang = board.getTeam("r");
            rang.setPrefix(Prefix);


            Team Coins = board.getTeam("c2");
            Coins.setPrefix("§e" + ldata.Coins(uuid));
            p.setScoreboard(board);
            if (boards.containsValue(p)) {
                boards.replace(p, board);
            } else {
                boards.put(p, board);
            }
        } else {
            setScoreBoard(p);
        }
    }



    public void changeDisplayName() {
        if (state <10) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                String uuid = p.getUniqueId().toString().replace("-", "");
                Scoreboard board = boards.get(p);
                Objective o = board.getObjective(uuid + "_Side");
                o.setDisplayName(ColorString());
                p.setScoreboard(board);
                if (boards.containsValue(p)) {
                    boards.replace(p, board);
                } else {
                    boards.put(p, board);
                }
            }
        }
        state++;

        if (state == 70) {
            state = 0;
        }


    }


    private String ColorString() {
        String s = "   §b§lOKAY §f§lNETWORK   ";
        if (state == 0) {
            s = "   §b§lOKAY §f§lNETWORK   ";
        } else if (state == 1) {
            s = "   §4§lO§b§lKAY §f§lNETWORK   ";
        } else if (state == 2) {
            s = "   §4§lOK§b§lAY §f§lNETWORK   ";
        } else if (state == 3) {
            s = "   §4§lOKA§b§lY §f§lNETWORK   ";
        } else if (state == 4) {
            s = "   §4§lOKAY§b§l §f§lNETWORK   ";
        } else if (state == 5) {
            s = "   §4§lOKAY §f§lNETWORK   ";
        } else if (state == 6) {
            s = "   §b§lO§4§lKAY §f§lNETWORK   ";
        } else if (state == 7) {
            s = "   §b§lOK§4§lAY §f§lNETWORK   ";
        } else if (state == 8) {
            s = "   §b§lOKA§4§lY §f§lNETWORK   ";
        } else if (state == 9) {
            s = "   §b§lOKAY§4§l §f§lNETWORK   ";
        }

        return s;

    }

}
