package de.ascasia.LobbySystem.sql;

import de.ascasia.LobbySystem.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PlayerMySQL {

    FileConfiguration config = Main.getPlugin().getConfig();



    private String host = config.getString("MySQL.host");
    private String port = config.getString("MySQL.port");
    private String Bdatabase = config.getString("MySQL.Bandatabase");
    private String Fdatabase = config.getString("MySQL.Frienddatabase");
    private String Pdatabase = config.getString("MySQL.Playerdatabase");
    private String Partydatabase = config.getString("MySQL.Partydatabase");
    private String Cdatabase = config.getString("MySQL.Clandatabase");
    private String username = config.getString("MySQL.username");
    private String password = config.getString("MySQL.password");
    private boolean ssl = config.getBoolean("MySQL.ssl");

        private Connection Friendconnection;

        public boolean PlayerisConnected() {
            return (Friendconnection != null);
        }

        public void connect()  throws ClassNotFoundException, SQLException {
            if (!PlayerisConnected()) {
                Friendconnection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" +
                        Pdatabase + "?usessl=" + ssl+ "&autoReconnect=true", username , password );
            }
        }

        public void disconnect() {
            if (PlayerisConnected()) {
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
