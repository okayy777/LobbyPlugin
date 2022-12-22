package de.ascasia.LobbySystem.commands;


import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.NPC.NPCs;
import de.ascasia.LobbySystem.obj.Activity;
import de.ascasia.LobbySystem.obj.NPC;
import net.minecraft.world.entity.Pose;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class npccommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        FileConfiguration config = Main.getPlugin().getConfig();
        if (sender instanceof Player) {
            if (sender.hasPermission("ascasia.LobbyAdmin")) {
                Player p = (Player) sender;
                if (args.length > 5 && args[0].equalsIgnoreCase("spawn") ) {
                    NPC npc = new NPC().createNPC(args[1], p.getLocation());
                    npc.setSkin(args[4]);
                    npc.setActivity(Activity.SwingMainArm);
                    npc.setItemInMainHand(p.getEquipment().getItemInMainHand());
                    npc.setItemInOffHand(p.getEquipment().getItemInOffHand());
                    npc.setHelmet(p.getEquipment().getHelmet());
                    npc.setChestplate(p.getEquipment().getChestplate());
                    npc.setLeggings(p.getEquipment().getLeggings());
                    npc.setBoots(p.getEquipment().getBoots());
                    StringBuilder text = new StringBuilder();
                    for (int x = 5 ; x < args.length ; x++) {
                        text.append( " " +args[x]);
                    }
                    npc.setText(text.toString());
                    npc.spawn();
                }
                else if (args.length > 2 && args[0].equalsIgnoreCase("text")) {
                    StringBuilder text = new StringBuilder();
                    for (int x = 2 ; x < args.length ; x++) {
                        text.append( " " +args[x]);
                    }
                    //NPC.changeText(args[1], text.toString());
                }
                else if ( args.length == 2 && args[0].equalsIgnoreCase("upload")) {
                    //NPCs.uploadNPC(args[1]);
                }
            }
        }

        // /npc spawn dev true RED IhrKleinerTeddy
        // /npc text dev Dev-Server
        return false;
    }


}
