package de.ascasia.LobbySystem.sql;

import de.ascasia.LobbySystem.Main;
import org.bukkit.command.CommandSender;

import java.sql.*;

public class SQLGetter {

    private Main plugin;
    public SQLGetter(Main plugin) {
        this.plugin = plugin;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = plugin.BSQL.getBanConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ban_info" + "(NAME VARCHAR(100), UUID VARCHAR(100), BANNED BOOLEAN, PERMANENT BOOLEAN ,BANDATE DATE, BANTIME TIME, REASON VARCHAR(300), DAUER INT , BANCOUNT INT, KICKCOUNT INT, MUTECOUNT INT ,PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void addPlayer(String name , String uuid) {
        PreparedStatement ps = null;
        try {
            if (!exists(uuid)) {
                PreparedStatement ps2 = plugin.BSQL.getBanConnection().prepareStatement("INSERT INTO ban_info (NAME,UUID,BANNED) VALUES (?,?,?)");
                ps2.setString(1 , name);
                ps2.setString(2 , uuid);
                ps2.setBoolean(3 , false);
                ps2.executeUpdate();
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean exists(String uuid) {
        try {
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("SELECT * FROM ban_info WHERE UUID=?");
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
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("SELECT * FROM ban_info WHERE NAME=?");
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

    public void addBan(String name , String uuid , Date BanDate , Time BanTime, boolean banned , boolean permanent, String reason , int Dauer) {
            try {
                if (exists(uuid)) {
                    PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("UPDATE ban_info SET BANDATE = ?, BANTIME = ? , BANNED = ? , PERMANENT = ? , REASON = ?, DAUER = ? , BANCOUNT = ? WHERE UUID = ?");
                    PreparedStatement ps2 = plugin.BSQL.getBanConnection().prepareStatement("SELECT * FROM ban_info WHERE UUID=?");
                    ps.setDate(1 ,  BanDate);
                    ps.setTime(2 ,  BanTime);
                    ps.setBoolean(3 , banned);
                    ps.setBoolean(4 , permanent);
                    ps.setString(5 , reason);
                    ps.setString(8 , uuid);
                    ps.setInt(6 , Dauer);
                    ps2.setString(1 ,uuid);
                    ResultSet rs = ps2.executeQuery();
                    int x = rs.getInt("BANCOUNT");
                    ps.setInt(7, x + 1);
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps2 = plugin.BSQL.getBanConnection().prepareStatement("INSERT INTO ban_info (NAME,UUID,BANNED,PERMANENT,BANDATE,BANTIME,REASON,DAUER,BANCOUNT) VALUES (?,?,?,?,?,?,?,?,?)");
                    ps2.setString(1, name);
                    ps2.setString(2, uuid);
                    ps2.setBoolean(3, banned);
                    ps2.setDate(5 , BanDate);
                    ps2.setTime(6 , BanTime);
                    ps2.setBoolean(4 , permanent);
                    ps2.setString(7 , reason);
                    ps2.setInt(8 , Dauer);
                    ps2.setInt(9 , 1);
                    ps2.executeUpdate();

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


    }
    public void AutoRemBan(String uuid) {
        if (exists(uuid)) {
            try {
                PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("UPDATE ban_info SET BANDATE = ?, BANTIME = ? , BANNED = ? , PERMANENT = ? , REASON = ?, DAUER = ? WHERE UUID = ?");
                ps.setDate(1 ,  null);
                ps.setTime(2 ,  null);
                ps.setBoolean(3 , false);
                ps.setBoolean(4 , false);
                ps.setString(5 , null);
                ps.setString(7 , uuid);
                ps.setInt(6 , 0);
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
    public void RemBan(String name , CommandSender p ) {
        if (nameexists(name)) {
            try {
                PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("UPDATE ban_info SET BANDATE = ?, BANTIME = ? , BANNED = ? , PERMANENT = ? , REASON = ?, DAUER = ? WHERE NAME = ?");
                ps.setDate(1 ,  null);
                ps.setTime(2 ,  null);
                ps.setBoolean(3 , false);
                ps.setBoolean(4 , false);
                ps.setString(5 , null);
                ps.setString(7 , name);
                ps.setInt(6 , 0);
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else {
        }

    }

    public boolean isBanend(String uuid) { // TOGGLEJUMP TOGGLEREQUEST TOGGLEPARTY TOGGLEMSG
        try {
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("SELECT * FROM ban_info WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            boolean banned = false;
            if (rs.next()) {
                banned = rs.getBoolean("BANNED");
                return banned;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public boolean isPermanent(String uuid) {
        try {
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("SELECT PERMANENT FROM ban_info WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            boolean banned = false;
            if (rs.next()) {
                banned = rs.getBoolean("PERMANENT");
                return banned;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public String getUUID (String name) {
        try {
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("SELECT * FROM ban_info WHERE NAME=?");
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            String banned = null;
            if (rs.next()) {
                banned = rs.getString("UUID");
                return banned;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public Time Time(String uuid) {
        try {
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("SELECT BANTIME FROM ban_info WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            Time banned = null;
            if (rs.next()) {
                banned = rs.getTime("BANTIME");
                return banned;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public Date mDate(String uuid) {
        try {
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("SELECT BANDATE FROM ban_info WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            Date banned = null;
            if (rs.next()) {
                banned = rs.getDate("BANDATE");
                return banned;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public int Dauer(String uuid) {
        try {
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("SELECT DAUER FROM ban_info WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            int banned = 0;
            if (rs.next()) {
                banned = rs.getInt("DAUER");
                return banned;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public String Reason(String uuid) {
        try {
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("SELECT * FROM ban_info WHERE UUID=?");
            ps.setString(1 , uuid);
            ResultSet rs = ps.executeQuery();
            String banned = null;
            if (rs.next()) {
                banned = rs.getString("REASON");
                return banned;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void addKick(String uuid) {
        try {
            PreparedStatement ps = plugin.BSQL.getBanConnection().prepareStatement("UPDATE ban_info KICKCOUNT = ? WHERE UUID = ?");
            PreparedStatement ps2 = plugin.BSQL.getBanConnection().prepareStatement("SELECT KICKCOUNT FROM ban_info WHERE UUID= ?");
            ps2.setString(1 , uuid);
            ResultSet rs = ps2.executeQuery();
            int x = rs.getInt("KICKCOUNT");
            ps.setInt(1 , x + 1);
            ps.setString(2, uuid);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getKick(String name) {
        try {
            PreparedStatement ps2 = plugin.BSQL.getBanConnection().prepareStatement("SELECT KICKCOUNT FROM ban_info WHERE NAME = ?");
            ps2.setString(1 , name);
            ResultSet rs = ps2.executeQuery();
            int x = rs.getInt("KICKCOUNT");
            return x;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
}
