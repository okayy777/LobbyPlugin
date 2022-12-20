package de.ascasia.LobbySystem.sql;


import de.ascasia.LobbySystem.Main;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ServerSQLGetter {

    private Main plugin;

    public ServerSQLGetter(Main plugin) {
        this.plugin = plugin;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = plugin.ServerSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS server_info " + "(NAME VARCHAR(100), ONLINE VARCHAR(10),GAMEMODE VARCHAR(100), IP VARCHAR(100),ONLINEPLAYER INT , UPTIME INT, RDATE DATE, RTIME TIME, PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public boolean exists(String name) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("SELECT * FROM server_info WHERE NAME=?");
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
    public boolean ServerCreated(String gamemode) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("SELECT * FROM server_info WHERE GAMEMODE=?");
            ps.setString(1 , gamemode);
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

    public String[] OnlineServer (String GAMEMODE) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("SELECT * FROM server_info WHERE GAMEMODE=? AND ONLINE=?");
            ps.setString(1 , GAMEMODE);
            ps.setString(2,"yes");
            ResultSet rs = ps.executeQuery();
            String Names = "";
            while (rs.next()) {
                Names = Names +  rs.getString("NAME")+ ",";

            }
            String[] Name = Names.split(",");
            return Name;




        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setServer(String Name , String IP , int uptime , Date date , Time time , String gamemode , int online) {
        if (!exists(Name)) {
            try {
                PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("INSERT INTO server_info (NAME,ONLINE,IP,UPTIME,RDATE,RTIME,GAMEMODE,ONLINEPLAYER) VALUES (?,?,?,?,?,?,?,?)");
                ps.setString(1 , Name);
                ps.setString(2, "yes");
                ps.setString(3, IP);
                ps.setInt(4, uptime);
                ps.setDate(5 ,date);
                ps.setTime(6, time);
                ps.setString(7, gamemode);
                ps.setInt(8, online);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("UPDATE server_info SET ONLINE=?,IP=?,UPTIME=?,RDATE=?,RTIME=? WHERE NAME=?");
                ps.setString(6 , Name);
                ps.setString(1, "yes");
                ps.setString(2, IP);
                ps.setInt(3, uptime);
                ps.setDate(4 ,date);
                ps.setTime(5, time);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public int uptime(String Name) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("SELECT * FROM server_info WHERE NAME=?");
            ps.setString(1 , Name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int uptime = rs.getInt("UPTIME");
                return uptime;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public LocalDateTime lastRestart(String Name) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("SELECT * FROM server_info WHERE NAME=?");
            ps.setString(1 , Name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate date = rs.getDate("RDATE").toLocalDate();
                LocalTime time = rs.getTime("RTIME").toLocalTime();
                LocalDateTime DateTime = date.atTime(time);
                return DateTime;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String IP (String Name) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("SELECT * FROM server_info WHERE NAME=?");
            ps.setString(1 , Name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String IP = rs.getString("IP");
                return IP;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String ONLINE(String Name) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("SELECT * FROM server_info WHERE NAME=?");
            ps.setString(1 , Name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String ONLINE = rs.getString("ONLINE");
                return ONLINE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setOffline(String Name) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("UPDATE server_info SET ONLINE=? WHERE NAME=?");
            ps.setString(1, "no");
            ps.setString(2, Name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setUptime(String Name, int uptime) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("UPDATE server_info SET UPTIME=? WHERE NAME=?");
            ps.setInt(1, uptime);
            ps.setString(2, Name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setOnlinePlayer(String Name, int ONLINE) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("UPDATE server_info SET ONLINEPLAYER=? WHERE NAME=?");
            ps.setInt(1, ONLINE);
            ps.setString(2, Name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int OnlinePlayer(String Name) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("SELECT * FROM server_info WHERE NAME=?");
            ps.setString(1, Name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                return rs.getInt("ONLINEPLAYER");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String Lobbys(String gamemode) {
        try {
            PreparedStatement ps = plugin.ServerSQL.getConnection().prepareStatement("SELECT * FROM server_info WHERE GAMEMODE =?");
            ps.setString(1, gamemode);
            ResultSet rs = ps.executeQuery();
            StringBuilder Lobbys = null;
            for(int i = 0; rs.next(); i++) {
                if(Lobbys == null) {
                    Lobbys = new StringBuilder(rs.getString("NAME")).append(",");
                }  else {
                    Lobbys.append(rs.getString("NAME")).append(",");
                }

            }
            return Lobbys == null ? null : Lobbys.toString();



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}