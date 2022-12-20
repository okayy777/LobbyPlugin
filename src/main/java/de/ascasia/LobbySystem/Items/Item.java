package de.ascasia.LobbySystem.Items;

import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.LobbySQLGetter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class Item {

    public LobbySQLGetter lobbydata = Main.getPlugin().Ldata;

    public void giveCompass(Player p, int pos) {
        String Name = p.getName();
        String ItemName = "§bTeleporter";
        Material Mat = Material.COMPASS;
        String lore = "§7Teleport zu den Verschiedenen §bSpielmodis";
        ItemStack Item = Items(ItemName , lore,"empty","empty","empty","empty","empty",Mat);
        p.getInventory().setItem(pos , Item);
    }

    public void giveLobbySwicht(Player p, int pos) {
        String Name = p.getName();
        String ItemName = "§aLobby Auswahl";
        Material Mat = Material.GLOWSTONE_DUST;
        ItemStack Item = Items(ItemName , "empty","empty","empty","empty","empty","empty",Mat);
        p.getInventory().setItem(pos , Item);
    }
    public void givePlayerToggle(Player p , int pos) {
        String Name = p.getName();
        String ItemName = "§cSichtbarkeit";
        Material Mat = Material.ENDER_EYE;
        ItemStack Item = Items(ItemName , "empty","empty","empty","empty","empty","empty",Mat);
        p.getInventory().setItem(pos , Item);
    }
    public void giveStoryInfo(Player p , int pos){
        String Name = p.getName();
        String ItemName = "§6Story";
        Material Mat = Material.GOLD_NUGGET;
        ItemStack Item = Items(ItemName , "empty","empty","empty","empty","empty","empty",Mat);
        p.getInventory().setItem(pos , Item);
    }
    public void giveProfil(Player p , int pos) {
        String Name = p.getName();
        String ItemName = "§3Mein Profil";
        ItemStack item = new ItemStack(Material.SKELETON_SKULL, 1, (short) 3);
        ItemMeta newItem_meta = item.getItemMeta();
        newItem_meta.setDisplayName(ItemName);
        ArrayList<String> newItem_lore = new ArrayList<>();
        newItem_meta.setLore(newItem_lore);
        item.setItemMeta(newItem_meta);
        SkullMeta sm = (SkullMeta) item.getItemMeta();
        sm.setOwner(p.getName());
        item.setItemMeta(sm);
        p.getInventory().setItem(pos , item);
    }
    public void giveNickTool(Player p, int pos) {
        String Name = p.getName();
        String uuid = p.getUniqueId().toString().replace("-", "");
        String ItemName = null;
        if (lobbydata.AutoNick(uuid)) {
            ItemName = "§eNick-Tool §8» §aON";
        } else if (!lobbydata.AutoNick(uuid)) {
            ItemName = "§eNick-Tool §8» §cOFF";
        }
        Material Mat = Material.NAME_TAG;
        ItemStack Item = Items(ItemName , "empty","empty","empty","empty","empty","empty",Mat);
        p.getInventory().setItem(pos , Item);
    }
    public void giveFlyTool(Player p, int pos) {
        String Name = p.getName();
        String uuid = p.getUniqueId().toString().replace("-", "");
        String ItemName = null;
        if (lobbydata.Fly(uuid)) {
            double FlySpeed = lobbydata.FlySpeed(uuid);
            if (FlySpeed != 1) {
                FlySpeed = FlySpeed + 0.75;
            } else {
                FlySpeed = FlySpeed + 1;
            }
            ItemName = "§fFliegemodus §ex" + FlySpeed+ " §8» §aON";
            p.setAllowFlight(true);
            p.setFlying(true);
            double FlySpeeed = lobbydata.FlySpeed(uuid);
            p.setFlySpeed((float) FlySpeeed);
        } else if (!lobbydata.Fly(uuid)) {
            double FlySpeed = lobbydata.FlySpeed(uuid);
            if (FlySpeed != 1) {
                FlySpeed = FlySpeed + 0.75;
            } else {
                FlySpeed = FlySpeed + 1;
            }
            ItemName = "§fFliegemodus §ex" + FlySpeed + " §8» §cOFF";
            p.setAllowFlight(false);
            p.setFlying(false);
            double FlySpeeed = lobbydata.FlySpeed(uuid);
            p.setFlySpeed((float) FlySpeeed);
        }
        Material Mat = Material.FEATHER;
        ItemStack Item = Items(ItemName , "empty","empty","empty","empty","empty","empty",Mat);
        p.getInventory().setItem(pos , Item);
    }

    public ItemStack Items(String Name , String lore , String lore2, String lore3, String lore4, String lore5, String lore6,  Material Item) {
        if (Name.contains("&")) {
            Name = Name.replace("&" , "§");
        }
        ItemStack newItem = new ItemStack(Item);
        ItemMeta newItem_meta = newItem.getItemMeta();
        newItem_meta.setDisplayName(Name);
        ArrayList<String> newItem_lore = new ArrayList<>();
        if (!lore.equals("empty")) {
            newItem_lore.add(lore);
        }
        if (!lore2.equals("empty")) {
            newItem_lore.add(lore2);
        }
        if (!lore3.equals("empty")) {
            newItem_lore.add(lore3);
        }
        if (!lore4.equals("empty")) {
            newItem_lore.add(lore4);
        }
        if (!lore5.equals("empty")) {
            newItem_lore.add(lore5);
        }
        if (!lore6.equals("empty")) {
            newItem_lore.add(lore6);
        }
        newItem_meta.setLore(newItem_lore);
        newItem.setItemMeta(newItem_meta);
        return  newItem;
    }
    public ItemStack ItemsSHORT(String Name , String lore,  Material Item) {
        ItemStack newItem = new ItemStack(Item);
        ItemMeta newItem_meta = newItem.getItemMeta();
        newItem_meta.setDisplayName(Name);
        ArrayList<String> newItem_lore = new ArrayList<>();
        if (!lore.equals("empty")) {
            newItem_lore.add(lore);
        }
        newItem_meta.setLore(newItem_lore);
        newItem.setItemMeta(newItem_meta);
        return  newItem;
    }
    public ItemStack Skull(String Name , String lore , String lore2, String lore3, String lore4, String lore5, String lore6,  ItemStack Item) {
        ItemMeta newItem_meta = Item.getItemMeta();
        newItem_meta.setDisplayName(Name);
        ArrayList<String> newItem_lore = new ArrayList<>();
        if (!lore.equals("empty")) {
            newItem_lore.add(lore);
        }
        if (!lore2.equals("empty")) {
            newItem_lore.add(lore2);
        }
        if (!lore3.equals("empty")) {
            newItem_lore.add(lore3);
        }
        if (!lore4.equals("empty")) {
            newItem_lore.add(lore4);
        }
        if (!lore5.equals("empty")) {
            newItem_lore.add(lore5);
        }
        if (!lore6.equals("empty")) {
            newItem_lore.add(lore6);
        }
        newItem_meta.setLore(newItem_lore);
        Item.setItemMeta(newItem_meta);
        return  Item;
    }
    public ItemStack SkullSHORT(String Name , String lore ,  ItemStack Item) {
        ItemMeta newItem_meta = Item.getItemMeta();
        newItem_meta.setDisplayName(Name);
        ArrayList<String> newItem_lore = new ArrayList<>();
        if (!lore.equals("empty")) {
            newItem_lore.add(lore);
        }
        newItem_meta.setLore(newItem_lore);
        Item.setItemMeta(newItem_meta);
        return  Item;
    }
}
