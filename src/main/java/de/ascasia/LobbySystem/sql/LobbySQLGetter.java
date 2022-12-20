package de.ascasia.LobbySystem.sql;

import de.ascasia.LobbySystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LobbySQLGetter {

    private Main plugin;

    public LobbySQLGetter(Main plugin) {
        this.plugin = plugin;
    }


    public void createNPCTable(){
        try {
        PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS npc_list (NAME VARCHAR(20) , X DOUBLE" +
                " , Y DOUBLE , Z DOUBLE , PITCH DOUBLE , YAW DOUBLE, WORLD VARCHAR(100) , " +
                "GLOW BOOLEAN , GLOW_COLOR VARCHAR(20) , SHOWONTAB BOOLEAN , COLLIDABLE BOOLEAN ,TEXT VARCHAR(1000) , SKIN VARCHAR(1000), ITEMINHAND VARCHAR(30), PRIMARY KEY (NAME))");
        ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public boolean NameExists(String Name) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM npc_list WHERE NAME=?");
            ps.setString(1 , Name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public List<String> NPC_Names() {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT NAME FROM npc_list");
            ResultSet rs = ps.executeQuery();
            List<String> Names = new ArrayList<>();
            while(rs.next()) {
                Names.add(rs.getString("NAME"));
            }
            return Names;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    public NPC.Global getNPC(String Name) {
        try {

            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM npc_list WHERE NAME=?");
            ps.setString(1 , Name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                double X = rs.getDouble("X");
                double Y = rs.getDouble("Y");
                double Z = rs.getDouble("Z");
                double PITCH = rs.getDouble("PITCH");
                double YAW = rs.getDouble("YAW");
                String World = rs.getString("WORLD");
                Location position = new Location(Bukkit.getWorld(World) , X, Y ,Z, (float) YAW,(float) PITCH);
                String Text = rs.getString("TEXT");
                String SkinName = rs.getString("SKIN");
                String ItemInHand = rs.getString("ITEMINHAND");
                boolean Glow = rs.getBoolean("GLOW");
                String Glow_Color = "empty";
                if (Glow) {
                    Glow_Color = rs.getString("GLOW_COLOR");
                }
                boolean collidable = rs.getBoolean("COLLIDABLE");
                boolean ShowOnTab = rs.getBoolean("SHOWONTAB");

                NPC.Global npc = NPCLib.getInstance().generateGlobalNPC(Main.getPlugin() , Name  , position );
                npc.setSkin(SkinName , minecraft -> npc.update());
                npc.setTextAlignment(new Vector(position.getX() , position.getY()+2 , position.getZ()));
                npc.setTextOpacity(NPC.Hologram.Opacity.FULL);
                ChatColor col = ChatColor.valueOf(Glow_Color);
                npc.setGlowing(Glow , col);
                npc.setPose(NPC.Pose.STANDING);
                npc.setCollidable(collidable);
                npc.setInteractCooldown(40);
                npc.setShowOnTabList(ShowOnTab);
                npc.setGazeTrackingType(NPC.GazeTrackingType.NONE);
                if (!ItemInHand.equalsIgnoreCase("empty")) {
                    ItemStack item = new ItemStack(Material.getMaterial(ItemInHand));
                    npc.setItem(NPC.Slot.MAINHAND, item);
                }
                npc.update();
                npc.setVisibility(NPC.Global.Visibility.SELECTED_PLAYERS);
                return npc;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

     */


    public void setNPC( double x , double y , double z , double pitch , double yaw , String World,
                        String Name , String Text, String Skin , boolean Glow , String GlowColor , boolean collidable , boolean ShowOnTab) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("INSERT INTO npc_list (X,Y,Z,PITCH,YAW,WORLD,NAME,SKIN,TEXT,GLOW,GLOW_COLOR,SHOWONTAB,COLLIDABLE) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            if (!NameExists(Name)) {
                ps.setDouble(1, x);
                ps.setDouble(2, y);
                ps.setDouble(3, z);
                ps.setDouble(4, pitch);
                ps.setDouble(5, yaw);
                ps.setString(6 , World);
                ps.setString(7 , Name);
                ps.setString(8 , Skin);
                ps.setString(9, Text);
                ps.setBoolean(10 , Glow);
                ps.setString(11, GlowColor);
                ps.setBoolean(12 , ShowOnTab);
                ps.setBoolean(13 , collidable);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    // CoinSystem

    public void createCoinTable() {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS coin_system (UUID VARCHAR(100)" +
                    " ,COINS INT, PURCHASED VARCHAR(400) , PRIMARY KEY (UUID))");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addPlayerCoins(String uuid , int Coins ) {
        if (!Coinexists(uuid)) {
            try {
                PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("INSERT INTO coin_system (UUID,COINS,PURCHASED) values (?,?,?)");
                ps.setString(1, uuid);
                ps.setInt(2, Coins);
                ps.setString(3, "null");
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean Coinexists(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM coin_system WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public int Coins (String uuid) {
        if(Coinexists(uuid)) {
            try {
                PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM coin_system WHERE UUID=?");
                ps.setString(1 , uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    return rs.getInt("COINS");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return 0;
    }

    public String TopCoins(int num) {
        int num2 = num+1;
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM coin_system ORDER BY - COINS LIMIT " +  num + " , "+ num2);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("UUID");
                int Coins = rs.getInt("COINS");
                return uuid + "," +Coins;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // SignSystem
    public void createSignTable() {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS sign_info (NAME VARCHAR(100) , X INT , Y INT, Z INT , WORLD VARCHAR(20), PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addSign(String Name , int x , int y , int z , String world ) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("INSERT INTO sign_info (NAME,X,Y,Z,WORLD) values (?,?,?,?,?)");
            ps.setString(1 , Name);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);
            ps.setString(5 , world);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createLobbyTable() {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS lobby_player (UUID VARCHAR(100) , X DOUBLE , Y DOUBLE, Z DOUBLE, YAW DOUBLE, PITCH DOUBLE," +
                    " NICK BOOLEAN , FLYSPEED DOUBLE , FLY BOOLEAN, PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean exists(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void addPlayer(String uuid , int x , int y , int z , int yaw , int pitch) {
        if (!exists(uuid)) {
            try {
                PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("INSERT INTO lobby_player (UUID,X,Y,Z,YAW,PITCH,NICK,FLYSPEED,FLY) values (?,?,?,?,?,?,?,?,?)");
                ps.setString(1, uuid);
                ps.setInt(2, x);
                ps.setInt(3, y);
                ps.setInt(4, z);
                ps.setInt(5, yaw);
                ps.setInt(6, pitch);
                ps.setBoolean(7, false);
                ps.setDouble(8, 0.25);
                ps.setBoolean(9, false);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean Fly(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("FLY");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Double FlySpeed(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getDouble("FLYSPEED");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    public boolean AutoNick(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("NICK");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void setFly(String uuid, boolean state) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player SET FLY=? WHERE UUID=?");
            ps.setBoolean(1 , state);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setFlySpeed(String uuid, double state) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player SET FLYSPEED=? WHERE UUID=?");
            ps.setDouble(1 , state);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setNick(String uuid, boolean state) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player SET NICK=? WHERE UUID=?");
            ps.setBoolean(1 , state);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setPos(String uuid, double x , double y , double z , double yaw , double pitch) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player SET X=?,Y=?,Z=?,YAW=?,PITCH=? WHERE UUID=?");
            ps.setDouble(1 , x);
            ps.setDouble(2 , y);
            ps.setDouble(3, z);
            ps.setDouble(4 , yaw);
            ps.setDouble(5 , pitch);
            ps.setString(6, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double[] getPos(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                double x = rs.getDouble("X");
                double y = rs.getDouble("Y");
                double z = rs.getDouble("Z");
                double yaw = rs.getDouble("YAW");
                double pitch = rs.getDouble("PITCH");
                return new double[]  {x ,y , z , yaw ,pitch};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void createSichtbarTable() {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS lobby_player_settings (UUID VARCHAR(100) ,MODE VARCHAR(100), FREUNDE BOOLEAN , CLAN BOOLEAN ," +
                    "PARTY BOOLEAN , STAFF BOOLEAN, PREMIUM BOOLEAN , SPIELER BOOLEAN, PREMIPLUS BOOLEAN , FAVORITEN BOOLEAN  ,PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addPlayerSicht(String uuid ) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("INSERT INTO lobby_player_settings (UUID,MODE,FREUNDE,CLAN,PARTY,STAFF,SPIELER,PREMIUM,PREMIPLUS,FAVORITEN) values (?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1 , uuid);
            ps.setString(2 , "ALL");
            ps.setBoolean(3 , true);
            ps.setBoolean(4 , true);
            ps.setBoolean(5 , true);
            ps.setBoolean(6 , true);
            ps.setBoolean(7 , true);
            ps.setBoolean(8 , true);
            ps.setBoolean(9 , true);
            ps.setBoolean(10 , true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setMode(String uuid , String Mode) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player_settings SET MODE=? WHERE UUID=?");
            ps.setString(1 , Mode);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setFREUNDE(String uuid , boolean FREUNDE) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player_settings SET FREUNDE=? WHERE UUID=?");
            ps.setBoolean(1 , FREUNDE);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setCLAN(String uuid , boolean CLAN) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player_settings SET CLAN=? WHERE UUID=?");
            ps.setBoolean(1 , CLAN);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setPARTY(String uuid , boolean PARTY) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player_settings SET PARTY=? WHERE UUID=?");
            ps.setBoolean(1 , PARTY);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setPREMIUM(String uuid , boolean PREMIUM) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player_settings SET PREMIUM=? WHERE UUID=?");
            ps.setBoolean(1 , PREMIUM);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setSPIELER(String uuid , boolean SPIELER) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player_settings SET SPIELER=? WHERE UUID=?");
            ps.setBoolean(1 , SPIELER);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setSTAFF(String uuid , boolean STAFF) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player_settings SET STAFF=? WHERE UUID=?");
            ps.setBoolean(1 , STAFF);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setFAVORITEN(String uuid , boolean FAVORITEN) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("UPDATE lobby_player_settings SET FAVORITEN=? WHERE UUID=?");
            ps.setBoolean(1 , FAVORITEN);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String Mode(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player_settings WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("MODE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean SichtFreunde(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player_settings WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("FREUNDE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean SichtClan(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player_settings WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("CLAN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean SichtParty(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player_settings WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("PARTY");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean SichtStaff(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player_settings WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("Staff");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean SichtPremium(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player_settings WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("Premium");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean SichtSpieler(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player_settings WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("SPIELER");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean SichtFavoriten(String uuid) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_player_settings WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("FAVORITEN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void createCompassTable(){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS lobby_compass (NAME VARCHAR(100), " +
                    "LORE1 VARCHAR(100),LORE2 VARCHAR(100),LORE3 VARCHAR(100),LORE4 VARCHAR(100),MATERIAL VARCHAR(100), FUNCTION VARCHAR(100), POSITION VARCHAR(100), POS INT , NUMBER INT, PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addItem(String Name , String lore1, String lore2, String lore3, String lore4, String Material , String function , String Position, int pos, int Number){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("INSERT INTO lobby_compass (NAME,LORE1,LORE2,LORE3,LORE4,MATERIAL,FUNCTION,POSITION,POS,NUMBER) values (?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1 , Name);
            ps.setString(2 ,lore1);
            ps.setString(3 ,lore2);
            ps.setString(4 ,lore3);
            ps.setString(5 ,lore4);
            ps.setString(6, Material);
            ps.setString(7 , function);
            ps.setString(8, Position);
            ps.setInt(9 , pos);
            ps.setInt(10 , Number);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public boolean Itemexists(String name) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean ItemNumexists(int num) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NUMBER=?");
            ps.setInt(1 , num);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean ItemSlotexists(int num) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE POS=?");
            ps.setInt(1 , num);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void remItem(String name) {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("DELETE FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String ItemName(int Num){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NUMBER=?");
            ps.setInt(1 , Num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String ItemNameSlot(int Num){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE POS=?");
            ps.setInt(1 , Num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String ItemLore1(String name){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("LORE1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String ItemLore2(String name){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("LORE2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String ItemLore3(String name){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("LORE3");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String ItemLore4(String name){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("LORE4");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String ItemMaterial(String name){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("MATERIAL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String Itemfunction(String name){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("FUNCTION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String ItemPosition(String name){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("POSITION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int Itempos(String name){
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT * FROM lobby_compass WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("POS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int ItemMaxNum() {
        try {
            PreparedStatement ps = plugin.LSQL.getConnection().prepareStatement("SELECT MAX(NUMBER) FROM lobby_compass");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("MAX(NUMBER)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



}
