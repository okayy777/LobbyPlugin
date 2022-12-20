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

public class NPCs {

    public static void createNPC(String ID , String ItemInHand , boolean Glowing ,
                                 String Glow_Color , boolean collidable , boolean ShowOnTab ,
                                 Location position , String SkinName) {

        NPC.Global npc = NPCLib.getInstance().generateGlobalNPC(Main.getPlugin() , ID  , position );
        npc.setSkin(SkinName , minecraft -> npc.update());
        npc.setCustomData("SkinName" , SkinName);
        npc.setTextAlignment(new Vector(position.getX() , position.getY()+2 , position.getZ()));
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
        npc.setText(Text);
        npc.updateText();

    }

    public static void syncNPC() {

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
