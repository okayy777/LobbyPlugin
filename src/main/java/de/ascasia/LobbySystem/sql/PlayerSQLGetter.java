package de.ascasia.LobbySystem.sql;


import de.ascasia.LobbySystem.Main;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerSQLGetter {

    private Main plugin;

    public PlayerSQLGetter(Main plugin) {
        this.plugin = plugin;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = plugin.PSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_info (NAME VARCHAR(100), UUID VARCHAR(100), PLAYTIME INT, ONLINE BOOLEAN ," +
                    " SERVER VARCHAR(100) , FONLINE INT , CLONLINE INT , CLNAME VARCHAR(100) , CLTAG VARCHAR(100) , TOGGLEONLINE BOOLEAN , TOGGLEJUMP BOOLEAN , TOGGLEREQUEST BOOLEAN , TOGGLEPARTY BOOLEAN , TOGGLEMSG BOOLEAN, FIRSTJOINED DATE ,   PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void firstJoin(String uuid , String name , Date FJOIN ) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("INSERT INTO player_info (NAME,UUID,FIRSTJOINED,TOGGLEONLINE,TOGGLEJUMP,TOGGLEREQUEST,TOGGLEPARTY,TOGGLEMSG,ONLINE,PLAYTIME) values (?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1 , name);
            ps.setString(2, uuid);
            ps.setDate(3 , FJOIN);
            ps.setBoolean(4 , false);
            ps.setBoolean(5 , false);
            ps.setBoolean(6 , false);
            ps.setBoolean(7 , false);
            ps.setBoolean(8 , false);
            ps.setBoolean(9 , true);
            ps.setInt(10 , 0);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public String getRang(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE UUID=?");
            ps.setString( 1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String rang = rs.getString("RANG");
                return rang;

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean exists(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE UUID=?");
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

    public boolean nameexists(String name) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE NAME=?");
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

    public void setOnline(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("UPDATE player_info SET ONLINE = ? WHERE UUID=?");
            ps.setString(2 , uuid);
            ps.setBoolean(1 ,true);
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public void setOffline(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("UPDATE  player_info SET ONLINE = ?, SERVER = ? WHERE UUID=?");
            ps.setString(2 , "");
            ps.setString(3 , uuid);
            ps.setBoolean(1 ,false);
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void setAllOffline() {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("UPDATE player_info SET ONLINE = ?, SERVER = ?");
            ps.setBoolean(1 ,false);
            ps.setString(2 , "");
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public void setServer(String uuid, String server) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("UPDATE player_info SET SERVER = ? WHERE UUID=?");
            ps.setString(2 , uuid);
            ps.setString(1 ,server);
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public boolean toggleeonline(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT TOGGLEONLINE FROM player_info WHERE UUID = ?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            boolean toggled = false;
            if (rs.next()) {
                toggled = rs.getBoolean("TOGGLEONLINE");
                return toggled;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean togglejump(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE UUID=?");
            ps.setString( 1 , uuid);
            ResultSet rs = ps.executeQuery();
            boolean toggled = false;
            if (rs.next()) {
                toggled = rs.getBoolean("TOGGLEJUMP");
                return toggled;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean togglerequest(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT TOGGLEREQUEST FROM player_info WHERE UUID=?");
            ps.setString( 1 , uuid);
            ResultSet rs = ps.executeQuery();
            boolean toggled = false;
            if (rs.next()) {
                toggled = rs.getBoolean("TOGGLEREQUEST");
                return toggled;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public boolean toggleparty(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            ps.setString( 1 , uuid);
            ResultSet rs = ps.executeQuery();
            boolean toggled = false;
            if (rs.next()) {
                toggled = rs.getBoolean("TOGGLEPARTY");
                return toggled;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean togglemsg(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            ps.setString( 1 , uuid);
            ResultSet rs = ps.executeQuery();
            boolean toggled = false;
            if (rs.next()) {
                toggled = rs.getBoolean("TOGGLEMSG");
                return toggled;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public String Server (String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            ps.setString( 1 , uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if(rs.getBoolean("ONLINE")) {
                    String server = rs.getString("SERVER");
                    return server;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public boolean isOnline(String uuid) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE UUID= ?");
            ps.setString( 1 , uuid);
            ResultSet rs = ps.executeQuery();
            boolean online = false;
            while (rs.next()) {
                online = rs.getBoolean("ONLINE");
                return online;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public String getName(String uuid) {
        try {
        PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
        ps.setString( 1 , uuid);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String Name = rs.getString("NAME");
            return Name;

        }
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
        return null;
    }

    public void setOnlineTime(String uuid, int x) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("UPDATE player_info SET PLAYTIME = ? WHERE UUID=?");
            ps.setString(2 , uuid);
            ps.setInt(1 , x);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getOnlineTime(String uuid){
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE UUID = ?");
            ps.setString( 1 , uuid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int x = rs.getInt("PLAYTIME");
                return x;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String OnlinePlayer() {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("SELECT * FROM player_info WHERE ONLINE=true");
            ResultSet rs = ps.executeQuery();
            StringBuilder Online = null;
            for(int i = 0; rs.next(); i++) {
                if(Online == null) {
                    Online = new StringBuilder(rs.getString("UUID")).append(",");
                }  else {
                    Online.append(rs.getString("UUID")).append(",");
                }

            }
            return Online == null ? null : Online.toString();



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public void NameChange(String uuid, String Name) {
        try {
            PreparedStatement ps = plugin.PSQL.getConnection().prepareStatement("UPDATE player_info SET NAME = ? WHERE UUID=?");
            ps.setString(1, Name);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}