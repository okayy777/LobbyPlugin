package de.ascasia.LobbySystem.commands;


import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.NPC.NPCs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class npccommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        FileConfiguration config = Main.getPlugin().getConfig();
        if (sender instanceof Player) {
            if (sender.hasPermission("ascasia.LobbyAdmin")) {
                Player p = (Player) sender;
                if (args.length == 8 && args[0].equalsIgnoreCase("spawn") ) {
                    NPCs.createNPC(args[1] , args[2] ,Boolean.parseBoolean(args[3]), args[4],Boolean.parseBoolean(args[5]),Boolean.parseBoolean(args[6]), p.getLocation() , args[7]);
                }
            }
        }
        return false;
    }


}
