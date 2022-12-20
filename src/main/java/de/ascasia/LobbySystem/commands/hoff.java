package de.ascasia.LobbySystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class hoff implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        sender.sendMessage("§4Irgendjemand will dass du hoffst :)");
        return false;
    }
}
