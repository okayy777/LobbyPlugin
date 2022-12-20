package de.ascasia.LobbySystem.commands;

import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.utils.SignConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SignCreate implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        FileConfiguration config = Main.getPlugin().getConfig();
        FileConfiguration SignConf = SignConfig.get();
        String Prefix = config.getString("Prefix.system");
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("ascasia.LobbyAdmin")) {
                if (args.length == 1) { // Name, IP , PORT
                    String Name = args[0];
                    SignConf.set(p.getName() + ".NAME" , Name);
                    SignConfig.save();
                    p.sendMessage(Prefix + "§5Select a Sign!");
                } else {
                    p.sendMessage(Prefix + "§7Bitte verwende §c/signcreate <Name>");
                }

            }
            else {

            }
        }
        return false;
    }
}
