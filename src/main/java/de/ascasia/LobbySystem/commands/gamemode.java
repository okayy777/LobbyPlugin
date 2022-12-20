package de.ascasia.LobbySystem.commands;

import de.ascasia.LobbySystem.Main;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class gamemode implements CommandExecutor {
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String Prefix = Main.getPlugin().getConfig().getString("Prefix.system");

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("ascasia.LobbyAdmin")) {
                if (args.length == 1) {
                    if (isNumeric(args[0])) {
                        int num = Integer.parseInt(args[0]);
                        if (num == 0) {
                            p.setGameMode(GameMode.SURVIVAL);
                            sender.sendMessage(Prefix + "§eDu bist nun im §4Survival-Mode");
                        } else if (num == 1) {
                            p.setGameMode(GameMode.CREATIVE);
                            sender.sendMessage(Prefix + "§eDu bist nun im §4Creative-Mode");
                        } else if (num == 2) {
                            p.setGameMode(GameMode.ADVENTURE);
                            sender.sendMessage(Prefix + "§eDu bist nun im §4Adventure-Mode");
                        } else if (num == 3) {
                            p.setGameMode(GameMode.SPECTATOR);
                            sender.sendMessage(Prefix + "§eDu bist nun im §4Spectator-Mode");
                        }
                    }
                }
                // ANDERE 0 - 1
                else if (args.length == 2) {
                    if (isNumeric(args[1])) {
                        Player t = Bukkit.getPlayer(args[0]);
                        int num = Integer.parseInt(args[1]);
                        if (num == 0) {
                            t.setGameMode(GameMode.SURVIVAL);
                            sender.sendMessage(Prefix + "§4 " + t.getName() + " §eist nun in §4Survival-Mode");
                        } else if (num == 1) {
                            t.setGameMode(GameMode.CREATIVE);
                            sender.sendMessage(Prefix + "§4" + t.getName() + " §eist nun in §4Creative-Mode");
                        } else if (num == 2) {
                            t.setGameMode(GameMode.ADVENTURE);
                            sender.sendMessage(Prefix + "§4" + t.getName() + " §eist nun in §4Adventure-Mode");
                        } else if (num == 3) {
                            t.setGameMode(GameMode.SPECTATOR);
                            sender.sendMessage(Prefix + "§4" + t.getName() + " §eist nun in §4Spectator-Mode");
                        }

                    } else if (isNumeric(args[0])) {
                        Player u = Bukkit.getPlayer(args[1]);
                        int num = Integer.parseInt(args[0]);
                        if (num == 0) {
                            u.setGameMode(GameMode.SURVIVAL);
                            sender.sendMessage(Prefix + "§4 " + u.getName() + " §eist nun im §4Survival-Mode");
                        } else if (num == 1) {
                            u.setGameMode(GameMode.CREATIVE);
                            sender.sendMessage(Prefix + "§4" + u.getName() + " §eist nun im §4Creative-Mode");
                        } else if (num == 2) {
                            u.setGameMode(GameMode.ADVENTURE);
                            sender.sendMessage(Prefix + "§4" + u.getName() + " §eist nun im §4Adventure-Mode");
                        } else if (num == 3) {
                            u.setGameMode(GameMode.SPECTATOR);
                            sender.sendMessage(Prefix + "§4" + u.getName() + " §eist nun im §4Spectator-Mode");
                        }
                    }
                }
            } else {
                sender.sendMessage(Prefix + "§7Command nicht gefunden? §c/help §7hilft dir weiter");

            }
        }

        return false;
    }
}
