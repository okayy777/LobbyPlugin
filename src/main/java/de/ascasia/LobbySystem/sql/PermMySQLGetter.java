package de.ascasia.LobbySystem.sql;

import de.ascasia.LobbySystem.Main;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermMySQLGetter {
    private Main plugin;

    public PermMySQLGetter(Main plugin) {
        this.plugin = plugin;
    }

    public void createGroup(String Name) {
        PreparedStatement ps;
        try {
            ps = plugin.PermSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS perms_"+ Name +" (NUMBER INT , PERMISSION VARCHAR(100), STATE BOOLEAN,PRIMARY KEY (NUMBER))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Groups Name/Prefix/Suffix Gewichtung // /promote Spieler (Rang Spieler) -> Spieler (Rang Test-Moderator)
        /*
        Rang Liste:
        Admin
        Sr Builder - Builder - Test Builder
        Sr Content - Content - Test Content
        Sr Moderator - Moderator - Test Moderator
        Sr Developer - Developer - Test Developer
        Giga-Premium - Premium - Spieler

        Weight:
        Spieler = 1
        Premium = 2
        Giga-Premium = 3
        Test-Rang = 4
        Rang = 5
        Sr = 6
        Admin = 7
         */
    }
    public boolean PermExists(String name) {
        try {
            DatabaseMetaData ps = plugin.PermSQL.getConnection().getMetaData();
            ResultSet results = ps.getTables(null, null, "perms_"+name , new String[] {"TABLE"});
            while (results.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean exists(String name) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("SELECT * FROM perms_info WHERE NAME=?");
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
    public boolean NumExists(String Rang, int num) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("SELECT * FROM perms_"+Rang +" WHERE NUMBER=?");
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

    public void createInfo() {
        PreparedStatement ps;
        try {
            ps = plugin.PermSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS perms_info (NAME VARCHAR(100) , PREFIX VARCHAR(100), SUFFIX VARCHAR(100), GRUPPE VARCHAR(100) , POSITION INT , STATE BOOLEAN,WEIGHT INT , PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void addGroup(String Name, String Prefix ,String Suffix ,String GROUP , int pos , int weight) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("INSERT INTO perms_info (NAME,PREFIX,SUFFIX,GRUPPE,POSITION,STATE,WEIGHT) values(?,?,?,?,?,?,?)");
            ps.setString(1, Name);
            ps.setString(2, Prefix);
            ps.setString(3, Suffix);
            ps.setString(4, GROUP);
            ps.setInt(5, pos);
            ps.setBoolean(6, true);
            ps.setInt(7, weight);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setPrefix(String Name, String Prefix) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("UPDATE perms_info PREFIX=? WHERE NAME=?");
            ps.setString(1, Prefix);
            ps.setString(2, Name);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setSuffix(String Name, String Suffix) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("UPDATE perms_info SUFFIX=? WHERE NAME=?");
            ps.setString(1, Suffix);
            ps.setString(2, Name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setState(String Name, boolean State) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("UPDATE perms_info SUFFIX=? WHERE NAME=?");
            ps.setBoolean(1, State);
            ps.setString(2, Name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPrefix(String Name) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("SELECT * from perms_info WHERE NAME=?");
            ps.setString(1, Name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String prefix = rs.getString("PREFIX");
                return prefix;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSuffix(String Name) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("SELECT * from perms_info WHERE NAME=?");
            ps.setString(1, Name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String suffix = rs.getString("SUFFIX");
                return suffix;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPosition(String Name) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("SELECT * from perms_info WHERE NAME=?");
            ps.setString(1, Name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int pos = rs.getInt("POSITION");
                return pos;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String Rang(int Pos) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("SELECT * from perms_info WHERE WEIGHT=?");
            ps.setInt(1 , Pos);
            ResultSet rs = ps.executeQuery();
            StringBuilder Rang = null;
            for(int i = 0; rs.next(); i++) {
                if(Rang == null) {
                    Rang = new StringBuilder(rs.getString("NAME")).append(",");
                }  else {
                    Rang.append(rs.getString("NAME")).append(",");
                }

            }
            return Rang == null ? null : Rang.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getGroup(String Name) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("SELECT * from perms_info WHERE NAME=?");
            ps.setString(1, Name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String group = rs.getString("GRUPPE");
                return group;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] permission(String name, int num) {
        String x = name.replace("-" , "_");
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("SELECT * from perms_"+name + " WHERE NUMBER = ?" );
            ps.setInt(1 , num);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String perm = rs.getString("PERMISSION");
                String allowed = String.valueOf(rs.getBoolean("STATE"));
                return new String[] {perm , allowed};
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPermission(String name ,String Permission , int num, boolean state) {
        try {
            PreparedStatement ps = plugin.PermSQL.getConnection().prepareStatement("INSERT INTO perms_"+name + "(NUMBER,PERMISSION,STATE) values (?,?,?)");
            ps.setInt(1, num);
            ps.setString(2, Permission);
            ps.setBoolean(3, state);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setPermissions() {
        String allPerms = "ascasia.WartungsJoin\n" + "ascasia.Whitelisted\n" +
                "ascasia.Gamemode\n" + "ascasia.Mute\n" + "ascasia.TemporaryMute\n" + "ascasia.Kick\n" +
                "ascasia.TemporaryBan\n" + "ascasia.Ban\n" + "ascasia.PermaBan\n" + "ascasia.Gungame\n" +
                "ascasia.Heal\n" + "ascasia.PromoteRang\n" + "ascasia.DemoteRang\n" +
                "ascasia.addRang\n" + "ascasia.removeRang\n" + "ascasia.Nick\n" + "ascasia.JoinFullRounds\n" + "ascasia.TeamServer\n" +
                "ascasia.TeamLobby\n" + "ascasia.TeamChat\n" + "ascasia.ReportSystem\n" + "ascasia.ReplaySystem\n" +
                "ascasia.StartRound\n" + "ascasia.JoinMe\n" + "ascasia.Fly\n" + "ascasia.Build";
        String[] perm = allPerms.split("\n");
        String Grouplist = "admin/sr-developer/developer/test-developer/sr-content/" +
                "content/test-content/sr-builder/builder/test-builder/sr-moderator/moderator/test-moderator/giga-premium/premium/spieler";
        String list = Grouplist.replace("-" , "_");

        String[] group = list.split("/");
        for (int y = 0; y < group.length;) {
            if (!PermExists(group[y])) {
                createGroup(group[y]);
                for (int x = 0; x < perm.length; ) {
                    setPermission(group[y], perm[x], x + 1, false);
                    x++;
                }
            }
            y++;
        }
    }
    public void setGroups() {
        String Grouplist = "admin/sr-developer/developer/test-developer/sr-content/" +
                "content/test-content/sr-builder/builder/test-builder/sr-moderator/moderator/test-moderator/giga-premium/premium/spieler";

        String[] group = Grouplist.split("/");
        createInfo();

        for (int x = 0; x < group.length ; ) {
            if (group[x].contains("sr")) {
                if(group[x].contains("developer")) {
                    addGroup(group[x] , "", "" , "developer" , 3 , 6);
                } else if(group[x].contains("content")) {
                    addGroup(group[x] , "", "" , "content" , 3 , 6);
                } else if(group[x].contains("builder")) {
                    addGroup(group[x] , "", "" , "builder" , 3 , 6);
                } else if(group[x].contains("moderator")) {
                    addGroup(group[x] , "", "" , "moderator" , 3 , 6);
                }
            }
            else if (group[x].contains("test")) {
                if(group[x].contains("developer")) {
                    addGroup(group[x] , "", "" , "developer" , 1 , 4);
                } else if(group[x].contains("content")) {
                    addGroup(group[x] , "", "" , "content" , 1 , 4);
                } else if(group[x].contains("builder")) {
                    addGroup(group[x] , "", "" , "builder" , 1 , 4);
                } else if(group[x].contains("moderator")) {
                    addGroup(group[x] , "", "" , "moderator" , 1 , 4);
                }
            } else {
                if(group[x].contains("developer")) {
                    addGroup(group[x] , "", "" , "developer" , 2 , 5);
                } else if(group[x].contains("content")) {
                    addGroup(group[x] , "", "" , "content" , 2 , 5);
                } else if(group[x].contains("builder")) {
                    addGroup(group[x] , "", "" , "builder" , 2 , 5);
                } else if(group[x].contains("moderator")) {
                    addGroup(group[x] , "", "" , "moderator" , 2 , 5);
                } else if(group[x].contains("admin")) {
                    addGroup(group[x] , "", "" , "admin" , 0 , 7);
                } else if(group[x].contains("giga-premium")) {
                    addGroup(group[x] , "", "" , "moderator" , 3 , 3);
                } else if(group[x].contains("premium")) {
                    addGroup(group[x] , "", "" , "moderator" , 2 , 2);
                } else if(group[x].contains("spieler")) {
                    addGroup(group[x] , "", "" , "moderator" , 1 , 1);
                }

            }
            x++;
        }
    }
}


