package de.ascasia.LobbySystem.NPC;



import de.ascasia.LobbySystem.Main;
import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

public class NPCs {

    public static HashMap<String , NPC.Global> NPC_List = new HashMap<>();

    public static void createNPC(String ID , String ItemInHand , boolean Glowing ,
                                 String Glow_Color , boolean collidable , boolean ShowOnTab ,
                                 Location position , String SkinName) {

        NPC.Global npc = NPCLib.getInstance().generateGlobalNPC(Main.getPlugin() , ID  , position );
        npc.setSkin(SkinName , minecraft -> npc.update());
        npc.setCustomData("SkinName" , SkinName);
        //npc.setTextAlignment();
        npc.setTextOpacity(NPC.Hologram.Opacity.FULL);
        ChatColor col = ChatColor.valueOf(Glow_Color);
        npc.setGlowing(Glowing , col);
        npc.setPose(NPC.Pose.STANDING);
        npc.setCollidable(collidable);
        npc.setInteractCooldown(40);
        npc.setShowOnTabList(ShowOnTab);
        npc.setGazeTrackingType(NPC.GazeTrackingType.NONE);
        if (!ItemInHand.equalsIgnoreCase("empty")) {
            ItemStack item = new ItemStack(Material.getMaterial(ItemInHand));
            npc.setItem(NPC.Slot.MAINHAND, item);
        }
        npc.setAutoCreate(true);
        npc.setAutoShow(true);
    }

    public static void changeText(String ID , String Text) {
        NPC.Global npc = NPCLib.getInstance().getGlobalNPC(Main.getPlugin() , ID);
        ChatColor col = ChatColor.WHITE;
        if (npc.isGlowing()) {
            col = npc.getGlowingColor().getChatColor();
        }
        npc.setText(col + Text);
        npc.updateText();
        npc.update();

    }

    public static void syncNPCs() {
        List<String> NPC_Names = Main.getPlugin().Ldata.NPC_Names();
        if (NPC_Names != null) {
            if (NPC_Names.isEmpty()) {
                return;
            }

            if (NPC_List.isEmpty()) {
                for (String Name : NPC_Names) {
                    NPC.Global npc = Main.getPlugin().Ldata.getNPC(Name);
                    npc.setVisibility(NPC.Global.Visibility.EVERYONE);
                    npc.setAutoShow(true);
                    npc.setAutoCreate(true);
                    npc.update();
                    NPC_List.put(npc.getSimpleCode() , npc);
                }
            } else {
                for (String Name : NPC_Names) {
                    if (NPC_List.containsKey(Name)) {
                        NPC.Global npc = Main.getPlugin().Ldata.getNPC(Name);
                        npc.setVisibility(NPC.Global.Visibility.EVERYONE);
                        npc.setAutoShow(true);
                        npc.setAutoCreate(true);
                        npc.update();
                        NPC_List.replace(npc.getSimpleCode(), npc);
                    } else {
                        NPC.Global npc = Main.getPlugin().Ldata.getNPC(Name);
                        npc.setVisibility(NPC.Global.Visibility.EVERYONE);
                        npc.setAutoShow(true);
                        npc.setAutoCreate(true);
                        npc.update();
                        NPC_List.put(npc.getSimpleCode(), npc);
                    }
                }
                for (String Name : NPC_List.keySet()) {
                    if (!NPC_Names.contains(Name)) {
                        NPC_List.get(Name).destroy();
                        NPC_List.get(Name).update();
                        NPC_List.remove(Name);
                    }
                }
            }
        }
    }

    public static void removeNPC(String ID) {
        NPC.Global npc = NPCLib.getInstance().getGlobalNPC(Main.getPlugin() , ID);
        npc.destroy();
        npc.update();
    }


    public static void uploadNPC(String ID) {
        NPC.Global npc = NPCLib.getInstance().getGlobalNPC(Main.getPlugin() , ID);
        Location loca = npc.getLocation();
        Main.getPlugin().Ldata.setNPC(loca.getX() , loca.getY() , loca.getZ() ,
                loca.getPitch(), loca.getYaw() ,loca.getWorld().getName(),
                npc.getSimpleCode() , npc.getText().toString() , npc.getCustomData("SkinName") ,
                npc.isGlowing() , npc.getGlowingColor().name() , npc.isCollidable() , npc.isShowOnTabList() );

    }







}
