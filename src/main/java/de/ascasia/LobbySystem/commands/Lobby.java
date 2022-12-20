package de.ascasia.LobbySystem.commands;

import de.ascasia.LobbySystem.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Lobby implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        FileConfiguration config = Main.getPlugin().getConfig();
        String Prefix = config.getString("Prefix.system");
        sender.sendMessage(Prefix + "§eDiese Lobby wurde von §6361Grad §egebaut");

        return false;
    }
}
