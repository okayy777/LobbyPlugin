package de.ascasia.LobbySystem.commands;

import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.utils.SignConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class admin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        FileConfiguration SignConf = SignConfig.get();
        FileConfiguration config = Main.getPlugin().getConfig();
        String Prefix = config.getString("Prefix.system");
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("ascasia.LobbyAdmin")) {
                if (SignConf.contains("admin." + p.getName())) {
                    if(SignConf.getBoolean("admin." + p.getName())) {
                        SignConf.set("admin." + p.getName() , false);
                        SignConfig.save();
                        p.sendMessage(Prefix +"§7Du hast die Admin funktionen §cdeaktiviert");
                    } else if(!SignConf.getBoolean("admin." + p.getName())) {
                        SignConf.set("admin." + p.getName() , true);
                        SignConfig.save();
                        p.sendMessage(Prefix +"§7Du hast die Admin funktionen §aaktiviert");
                    }
                } else {
                    SignConf.set("admin." + p.getName() , true);
                    SignConfig.save();
                    p.sendMessage(Prefix +"§7Du hast die Admin funktionen §aaktiviert");

                }
            }
        }
        return false;
    }
}
