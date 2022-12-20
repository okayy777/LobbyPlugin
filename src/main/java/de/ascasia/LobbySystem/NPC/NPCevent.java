package de.ascasia.LobbySystem.NPC;

import de.ascasia.LobbySystem.Items.Item;
import de.ascasia.LobbySystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NPCevent implements Listener {

    public Item ItemGetter = Main.getPlugin().item;
    /*

    @EventHandler
    public void onNPCInteract(NPC.Events.Interact e) {
        Player p = e.getPlayer();
        NPC npc = e.getNPC();
        String[] Code = npc.getCode().split("_");
        String ID = Code[1];
        boolean leftClick = e.getClickType().isLeftClick();
        boolean rightClick = e.getClickType().isRightClick();

        //System.out.println(ID);

        if (ID.equalsIgnoreCase("dev")) {
            Inventory inv = Bukkit.createInventory(p  , 9 ,"§cDev-Server:" );
            if (Main.getPlugin().ServerData.ServerCreated("TEAMLOBBY")) {
                String DevLobbylist = Main.getPlugin().ServerData.Lobbys("DEVLOBBY");
                String DevMode1list = Main.getPlugin().ServerData.Lobbys("DEVMINING");
                List<String> DevServers = new ArrayList<>();
                if (DevLobbylist != null) {
                    Collections.addAll(DevServers, DevLobbylist.split(","));
                }
                if (DevMode1list != null) {
                    Collections.addAll(DevServers, DevMode1list.split(","));
                }
                String[] DevServer = DevServers.toArray(String[]::new);
                for (int x = 0; x < DevServer.length; x++) {
                    int PlayerCount = Main.getPlugin().ServerData.OnlinePlayer(DevServer[x]);
                    if (Main.getPlugin().ServerData.ONLINE(DevServer[x]).equals("yes")) {
                        ItemStack Item = ItemGetter.ItemsSHORT("§c" + DevServer[x], "§6" + PlayerCount + " §aSpieler", Material.POTION);
                        PotionMeta potionmeta = (PotionMeta) Item.getItemMeta();
                        potionmeta.setMainEffect(PotionEffectType.INCREASE_DAMAGE);
                        Item.setItemMeta(potionmeta);
                        inv.setItem( x, Item);
                    } else {
                        ItemStack Item = ItemGetter.ItemsSHORT("§c" + DevServer[x], "§cOffline", Material.GLASS_BOTTLE);
                        inv.setItem( x, Item);
                    }
                }
            }
            p.openInventory(inv);
        }

        if (rightClick) {
            if (ID.equalsIgnoreCase("test")) {
                Inventory inv = Bukkit.createInventory(p  , 9 ,"§3Testing" );
                p.openInventory(inv);
            }
        }

        if (leftClick) {
            if (ID.equalsIgnoreCase("test")) {
                Inventory inv = Bukkit.createInventory(p  , 9 ,"§3Testing2" );
                p.openInventory(inv);
            }
        }

    }

     */
}
