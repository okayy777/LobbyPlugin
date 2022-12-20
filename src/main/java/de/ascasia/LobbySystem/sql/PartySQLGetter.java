package de.ascasia.LobbySystem.sql;

import de.ascasia.LobbySystem.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PartySQLGetter {


        private Main plugin;

        public PartySQLGetter(Main plugin) {
            this.plugin = plugin;
        }

        public void createTable() {
            PreparedStatement ps;
            try {
                ps = plugin.PASQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS party_list (UUID1 VARCHAR(100), UUID2 VARCHAR(100), UUID3 VARCHAR(100) , UUID4 VARCHAR(100) , UUID5 VARCHAR(100) , UUID6 VARCHAR(100) , UUID7 VARCHAR(100), UUID8 VARCHAR(100) , UUID9 VARCHAR(100)" +
                        " , UUID10 VARCHAR(100) , UUID11 VARCHAR(100) ,UUID12 VARCHAR(100) ,UUID13 VARCHAR(100) ,UUID14 VARCHAR(100) , UUID15 VARCHAR(100) , UUID16 VARCHAR(100) , UUID17 VARCHAR(100) , UUID18 VARCHAR(100) , UUID19 VARCHAR(100) ," +
                        "UUID20 VARCHAR(100) ,LEADER VARCHAR(100) ,MODERATOR VARCHAR(400) , OUTREQUEST VARCHAR(1500) ,PRIMARY  KEY (UUID1))");
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        public void clearTable() {
            try {
                PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("TRUNCATE party_list");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public boolean isParty(String uuid) {
            for (int x = 1; x < 21; x++) {
                try {
                    PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("SELECT * FROM party_list WHERE UUID" + x + "= ?");
                    ps.setString(1 ,uuid);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        public void CreateParty(String uuid, String puuid) {
            try {
                PreparedStatement ps =plugin.PASQL.getConnection().prepareStatement("INSERT INTO party_list (UUID1,LEADER,OUTREQUEST) values (?,?,?)");
                ps.setString(1, uuid);
                ps.setString(2, uuid);
                ps.setString(3, puuid);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public void acceptParty(String uuid , String puuid) {
            for (int x = 1; x < 21; x++) {
                if (!UUIDisSet(uuid , x )) {
                    try {
                        PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("UPDATE party_list SET UUID" + x + " = ? WHERE OUTREQUEST = ?");
                        ps.setString(1, puuid);
                        ps.setString(2, puuid);
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public boolean UUIDisSet (String uuid , int pos){
            try {
                PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("SELECT * FROM party_list WHERE UUID"+ pos +"= ?");
                ps.setString(1 , uuid);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
    }

    public void LeaveParty(String uuid) {
        for (int x = 1; x < 21; x++) {
            if (UUIDisSet(uuid , x)) {
                try {
                    PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("UPDATE party_list SET UUID = ? WHERE UUID" + x + "= ?");
                    ps.setString(1 , null);
                    ps.setString(2, uuid);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setModerator(String uuid) {
        for (int x = 1; x < 21; x++) {
            try {
                PreparedStatement ps2 = plugin.PASQL.getConnection().prepareStatement("SELECT * FROM party_list WHERE UUID = ? ");
                ps2.setString(1, uuid);
                ResultSet rs = ps2.executeQuery();
                while (rs.next()) {
                    if (rs.getString("MODERATOR") == null) {
                        PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("UPDATE party_list SET MODERATOR = ? WHERE UUID = ? ");
                        ps.setString(1, uuid);
                        ps.setString(2, uuid);
                        ps.executeUpdate();
                    } else {
                        PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("UPDATE party_list SET MODERATOR = ? WHERE UUID = ? ");
                        ps.setString(1, rs.getString("MODERATOR") + "," + uuid);
                        ps.setString(2, uuid);
                        ps.executeUpdate();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String Moderators(String uuid) {
        for (int x = 1; x < 21; x++) {
            try {
                PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("SELECT * FROM party_list WHERE UUID" + x + "=?");
                ps.setString(1  , uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String Mods = rs.getString("MODERATOR");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void unsetModerator (String uuid) {
        for (int x = 1; x < 21; x++) {
            try {
                PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("UPDATE party_list SET MODERATOR = ? WHERE UUID"+x+" = ? ");
                String Mods = Moderators(uuid) ;
                String Mnew = Mods.replace("," + uuid , "");
                ps.setString(1 , Mnew);
                ps.setString(2, uuid);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public String Leader(String uuid) {
        for (int x = 1; x < 21; x++) {
            try {
                PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("SELECT * FROM party_list WHERE UUID" + x + "=?");
                ps.setString(1  , uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String Mods = rs.getString("LEADER");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void setLeader(String uuid) {
        for (int x = 1; x < 21; x++) {
            try {
                PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("UPDATE party_list SET LEADER = ? WHERE UUID" + x + "=?");
                ps.setString(1 , uuid);
                ps.setString(2 , uuid);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRequsted(String uuid) {
        try {
            PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("SELECT * FROM party_list WHERE REQUESTED = ? ");
            ps.setString(1 ,uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String partylist(String uuid) {
        StringBuilder Party = null;
        for (int x = 1; x < 21; x++) {
            try {
                PreparedStatement ps = plugin.FSQL.getFriendConnection().prepareStatement("SELECT * FROM party_list WHERE UUID" + x +" = ?");
                ps.setString(1 , uuid);
                ResultSet rs = ps.executeQuery();
                for (int y = 1; y < 21; y++) {
                    if (Party == null) {
                        Party = new StringBuilder(rs.getString("UUID" +y)).append(",");
                    } else {
                        Party.append(rs.getString("UUID" + y)).append(",");
                    }
                }
                return Party == null ? null : Party.toString();



            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return null;
    }

    public void setRequestd(String uuid , String puuid ) {
        for (int x = 1; x < 21; x++) {
            try {
                PreparedStatement ps2 = plugin.PASQL.getConnection().prepareStatement("SELECT * FROM party_list WHERE UUID = ? ");
                ps2.setString(1, uuid);
                ResultSet rs = ps2.executeQuery();
                while (rs.next()) {
                    if (rs.getString("OUTREQUEST") == null) {
                        PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("UPDATE party_list SET OUTREQUEST = ? WHERE UUID = ? ");
                        ps.setString(1, puuid);
                        ps.setString(2, uuid);
                        ps.executeUpdate();
                    } else {
                        PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("UPDATE party_list SET OUTREQUEST = ? WHERE UUID = ? ");
                        ps.setString(1, rs.getString("OUTREQUEST") + "," + puuid);
                        ps.setString(2, uuid);
                        ps.executeUpdate();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void deny(String uuid , String puuid ) {
        for (int x = 1; x < 21; x++) {
            try {
                PreparedStatement ps2 = plugin.PASQL.getConnection().prepareStatement("SELECT * FROM party_list WHERE UUID = ? ");
                ps2.setString(1, uuid);
                ResultSet rs = ps2.executeQuery();
                while (rs.next()) {
                        PreparedStatement ps = plugin.PASQL.getConnection().prepareStatement("UPDATE party_list SET OUTREQUEST = ? WHERE UUID = ? ");
                        ps.setString(1, rs.getString("OUTREQUEST").replace(puuid , ""));
                        ps.setString(2, uuid);
                        ps.executeUpdate();
                    }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}


