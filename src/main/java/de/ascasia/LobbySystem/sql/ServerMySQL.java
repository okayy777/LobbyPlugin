package de.ascasia.LobbySystem.sql;


import de.ascasia.LobbySystem.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerMySQL {

    FileConfiguration config = Main.getPlugin().getConfig();



    private String host = config.getString("MySQL.host");
    private String port = config.getString("MySQL.port");
    private String ServerDatabase = config.getString("MySQL.Serverdatabase");
    private String StatsDatabase = config.getString("MySQL.StatsDatabase");
    private String username = config.getString("MySQL.username");
    private String password = config.getString("MySQL.password");
    private boolean ssl = config.getBoolean("MySQL.ssl");

        private Connection Friendconnection;

        public boolean ServerIsConnected() {
            return (Friendconnection != null);
        }

        public void connect()  throws ClassNotFoundException, SQLException {
            if (!ServerIsConnected()) {
                Friendconnection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" +
                        ServerDatabase + "?usessl=" + ssl + "&autoReconnect=true", username , password );
            }
        }

        public void disconnect() {
            if (ServerIsConnected()) {
                try {
                    Friendconnection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        public Connection getConnection() {
            return Friendconnection;
        }



}
