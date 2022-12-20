package de.ascasia.LobbySystem.sql;

import de.ascasia.LobbySystem.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FriendMySQL {
    FileConfiguration config = Main.getPlugin().getConfig();



    private String host = config.getString("MySQL.host");
    private String port = config.getString("MySQL.port");
    private String Bdatabase = config.getString("MySQL.Bandatabase");
    private String Fdatabase = config.getString("MySQL.Frienddatabase");
    private String username = config.getString("MySQL.username");
    private String password = config.getString("MySQL.password");
    private boolean ssl = config.getBoolean("MySQL.ssl");


    private Connection Banconnection;
    private Connection Friendconnection;

    public boolean FRIENDisConnected() {
        return (Friendconnection != null);
    }

    public void connect()  throws ClassNotFoundException, SQLException {
        if (!FRIENDisConnected()) {
            Friendconnection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" +
                    Fdatabase + "?usessl=" + ssl+ "&autoReconnect=true", username , password );
        }
    }

    public void FRIENDdisconnect() {
        if (FRIENDisConnected()) {
            try {
                Friendconnection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public Connection getFriendConnection() {
        return Friendconnection;
    }


}
