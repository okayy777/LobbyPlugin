package de.ascasia.LobbySystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class rechtecheck implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = Bukkit.getPlayer(args[0]);
        if (p.hasPermission("ascasia.Fly")) {
            sender.sendMessage("ja");
        } else {
            sender.sendMessage("nein");
        }
        return false;
    }
}
