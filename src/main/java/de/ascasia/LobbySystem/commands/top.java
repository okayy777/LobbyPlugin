package de.ascasia.LobbySystem.commands;

import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.LobbySQLGetter;
import de.ascasia.LobbySystem.utils.nameCHECK2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class top  implements CommandExecutor {
    public LobbySQLGetter lobbydata = Main.getPlugin().Ldata;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§eTop 5 der §6Reichsten §eSpieler:");
            for (int x = 0 ; x <= 1 ; x++) {
                String Top = lobbydata.TopCoins(x);
                String[] split = Top.split(",");
                String Name = null;
                try {
                    Name = nameCHECK2.main(split[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sender.sendMessage("§8- §e§l" + Name + " §8(§e" + split[1] + " §6Coins§8)");
            }
        }
        return false;
    }
}
