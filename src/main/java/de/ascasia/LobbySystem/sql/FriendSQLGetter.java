package de.ascasia.LobbySystem.sql;

import de.ascasia.LobbySystem.Main;

import java.sql.*;

public class FriendSQLGetter {

    private Main plugin;

    public FriendSQLGetter(Main plugin) {
        this.plugin = plugin;
    }

    public void createTable(String uuid) {
        PreparedStatement ps;
        try {
            ps = plugin.FSQL.getFriendConnection().prepareStatement("CREATE TABLE IF NOT EXISTS spieler_"+ uuid +" (NAME VARCHAR(100), UUID VARCHAR(100), VIP BOOLEAN , ACCEPTED BOOLEAN , OUTREQUEST BOOLEAN, INREQUEST BOOLEAN , DATE DATE, TIME TIME , PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public boolean exists(String uuid) {
        try {
            DatabaseMetaData ps = plugin.FSQL.getFriendConnection().getMetaData();
            ResultSet results = ps.getTables(null, null, "spieler_"+uuid , new String[] {"TABLE"});
            while (results.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void FriendAdd(String name, String uuid , String tname , String tuuid) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("INSERT INTO spieler_"+uuid  +"  (NAME, UUID,OUTREQUEST) values (?,?,?)");
            ps.setString(1 , tname );
            ps.setString(2 , tuuid );
            ps.setBoolean(3 , true);
            PreparedStatement ps2 = plugin.FSQL.getFriendConnection().prepareStatement("INSERT INTO spieler_"+tuuid  +" (NAME, UUID,INREQUEST) values (?,?,?)");
            ps2.setString(1,name);
            ps2.setString(2 , uuid);
            ps2.setBoolean(3 , true);
            ps.executeUpdate();
            ps2.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public int FriendCount(String uuid) {
         try {
             PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT COUNT(*) FROM spieler_"+ uuid+"  WHERE ACCEPTED=?");
             ps.setBoolean(1, true);
             ResultSet rs = ps.executeQuery();
             while (rs.next()) {
                 return rs.getInt("COUNT(*)");
             }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public String Friends(String uuid) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT * FROM spieler_"+ uuid +" WHERE ACCEPTED=true");
            ResultSet rs = ps.executeQuery();
            StringBuilder Friends = null;
            for(int i = 0; rs.next(); i++) {
                if(Friends == null) {
                    Friends = new StringBuilder(rs.getString("UUID")).append(",");
                }  else {
                    Friends.append(rs.getString("UUID")).append(",");
                }

            }
            return Friends == null ? null : Friends.toString();



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public String VIPS(String uuid) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT * FROM spieler_"+ uuid +" WHERE ACCEPTED=true AND VIP=true");
            ResultSet rs = ps.executeQuery();
            StringBuilder Friends = null;
            for(int i = 0; rs.next(); i++) {
                if(Friends == null) {
                    Friends = new StringBuilder(rs.getString("UUID")).append(",");
                }  else {
                    Friends.append(rs.getString("UUID")).append(",");
                }

            }
            return Friends == null ? null : Friends.toString();



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean PlayerInFL(String uuid , String tuuid ) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT * FROM spieler_"+uuid  +" WHERE UUID=?");
            ps.setString(1 , tuuid);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                boolean res = results.getBoolean("ACCEPTED");
                if (res) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
    public boolean PlayerInFLisFav(String uuid , String tuuid ) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT * FROM spieler_"+uuid  +" WHERE UUID=?");
            ps.setString(1 , tuuid);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                boolean res = results.getBoolean("ACCEPTED");
                boolean res2 = results.getBoolean("VIP");
                if (res && res2) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean requested(String uuid , String tuuid) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT * FROM spieler_"+uuid  +" WHERE UUID=?");
            ps.setString(1 , tuuid);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                boolean res = results.getBoolean("INREQUEST");
                if (res) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    public boolean IsOutRequest(String uuid , String tuuid) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT * FROM spieler_"+uuid  +" WHERE UUID=?");
            ps.setString(1 , tuuid);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                boolean res = results.getBoolean("INREQUEST");
                if (res) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    public void setFriends(String uuid, String tuuid, Date Datum , Time Zeit) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("UPDATE spieler_" +uuid + " SET ACCEPTED = ?, OUTREQUEST = ? , INREQUEST = ? ,DATE = ? , TIME =? WHERE UUID=?");
            ps.setBoolean(1, true);
            ps.setBoolean(2 , false);
            ps.setBoolean(3 , false);
            ps.setDate(4, Datum);
            ps.setTime(5, Zeit);
            ps.setString( 6, tuuid);
            PreparedStatement ps2 = plugin.FSQL.getFriendConnection().prepareStatement("UPDATE spieler_" +tuuid + " SET ACCEPTED = ?, OUTREQUEST = ? , INREQUEST = ? ,DATE = ? , TIME =? WHERE UUID=?");
            ps2.setBoolean(1, true);
            ps2.setBoolean(2 , false);
            ps2.setBoolean(3 , false);
            ps2.setDate(4, Datum);
            ps2.setTime(5, Zeit);
            ps2.setString( 6, uuid);
            ps.executeUpdate();
            ps2.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void remFriends(String uuid, String tuuid) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("DELETE FROM spieler_" + uuid + " WHERE UUID=?");
            ps.setString( 1, tuuid);
            PreparedStatement ps2 = plugin.FSQL.getFriendConnection().prepareStatement("DELETE FROM spieler_" + tuuid + " WHERE UUID=?");

            ps2.setString( 1, uuid);
            ps.executeUpdate();
            ps2.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void NameChange(String uuid, String Name) {
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("UPDATE * SET NAME = ? WHERE UUID=?");
            ps.setString(1, Name);
            ps.setString(2 , uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Date getDate(String uuid, String tuuid){
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT * FROM spieler_"+uuid  +" WHERE UUID=?");
            ps.setString(1 , tuuid);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                Date da = results.getDate("DATE");
                return da;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    public Time getTime(String uuid, String tuuid){
        try {
            PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT * FROM spieler_"+uuid  +" WHERE UUID=?");
            ps.setString(1 , tuuid);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                Time da = results.getTime("TIME");
                return da;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}