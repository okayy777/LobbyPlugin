package de.ascasia.LobbySystem.commands;

import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.LobbySQLGetter;
import de.ascasia.LobbySystem.utils.SignConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class teleporter implements CommandExecutor {
    public LobbySQLGetter lobbydata = Main.getPlugin().Ldata;

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        FileConfiguration config = Main.getPlugin().getConfig();
        FileConfiguration SignConf = SignConfig.get();
        String Prefix = config.getString("Prefix.system");
        if (sender instanceof Player) {
            Player p= (Player) sender;
            if (p.hasPermission("ascasia.LobbyAdmin")){
                if(SignConf.getBoolean("admin."+p.getName())) {
                    if (args[0].equals("add") && args.length == 4) {
                        if (isNumeric(args[1])) {
                            if (args[2].equals("teleport")) {
                                if (p.getItemInHand().getType() != null) {
                                    int pos = Integer.parseInt(args[1]);
                                    String Name = args[3];
                                    Material Mat = p.getItemInHand().getType();
                                    String Material = Mat.toString();
                                    Location ploca = p.getLocation();
                                    double x = Double.parseDouble(new DecimalFormat("0.00").format(ploca.getX()));
                                    double y = Double.parseDouble(new DecimalFormat("0.00").format(ploca.getY()));
                                    double z = Double.parseDouble(new DecimalFormat("0.00").format(ploca.getZ()));
                                    String loca = x + "," + y + "," +z + "," + ploca.getYaw() + "," + ploca.getPitch()+ "," + ploca.getWorld().getName();
                                    int maxNum = lobbydata.ItemMaxNum() + 1;
                                    lobbydata.addItem(Name, "empty", "empty", "empty", "empty", Material, "teleport", loca, pos, maxNum);
                                    p.sendMessage(Prefix + "§7Du hast das Item erfolgreich hinzugefügt");
                                } else {
                                    p.sendMessage(Prefix + "§cBitte nehm ein Item deiner Wahl in die Hand");
                                }
                            } else if (args[2].equals("none")) {
                                if (p.getItemInHand().getType() != null) {
                                    int pos = Integer.parseInt(args[1]);
                                    String Name = args[3];
                                    Material Mat = p.getItemInHand().getType();
                                    String Material = Mat.toString();
                                    Location ploca = p.getLocation();
                                    String loca = ploca.getX() + "," + ploca.getY() + "," + ploca.getZ() + "," + ploca.getYaw() + "," + ploca.getPitch() + "," + ploca.getWorld().getName();
                                    int maxNum = lobbydata.ItemMaxNum() + 1;
                                    lobbydata.addItem(Name, "empty", "empty", "empty", "empty", Material, "none", loca, pos, maxNum);
                                    p.sendMessage(Prefix + "§7Du hast das Item erfolgreich hinzugefügt");
                                } else {
                                    p.sendMessage(Prefix + "§cBitte nehm ein Item deiner Wahl in die Hand");
                                }

                            } else {
                                p.sendMessage(Prefix + "§cBitte verwende /compass add <PosInInv> teleport <Name>");
                            }

                        } else {
                            p.sendMessage(Prefix + "§cBitte verwende /compass add <PosInInv> teleport <Name>");
                        }

                    } else if (args.length == 2 && args[0].equals("remove")) {
                        if (lobbydata.Itemexists(args[1])) {
                            lobbydata.remItem(args[1]);
                            p.sendMessage(Prefix + "§7Du hast das Item " + args[1].replace("&", "§") + " §cgelöscht");

                        } else {
                            p.sendMessage(Prefix + "§cBitte gebe den richtigen Namen ein");
                        }
                    }
                } else {
                    p.sendMessage(Prefix + "§cDu musst zuerst den Admin Modus §aaktivieren");
                }
            } else {
                //help Nachricht
            }
        }
        return false;
    }
}
