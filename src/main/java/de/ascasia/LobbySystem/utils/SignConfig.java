package de.ascasia.LobbySystem.utils;

import de.ascasia.LobbySystem.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SignConfig {

    private static File file;
    private static FileConfiguration costumFile;

    // Finds or generates the config file
    public static void setup() {
        file = new File(Main.getPlugin().getDataFolder(), "signconfig.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // ERROR
            }

        }

        costumFile = YamlConfiguration.loadConfiguration((file));

    }

    public static FileConfiguration get() {
        return costumFile;
    }

    public static void save() {
        try {
            costumFile.save(file);
        } catch (IOException e) {
            System.out.println("Save failed");
        }
    }
    public static void reload() {
        costumFile = YamlConfiguration.loadConfiguration(file);
    }
}
