package de.ascasia.LobbySystem.Listener;

import de.ascasia.LobbySystem.Items.Item;
import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.*;
import de.ascasia.LobbySystem.utils.freunde;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class RightClick implements Listener {
    public Item ItemGetter = Main.getPlugin().item;
    public LobbySQLGetter lobbydata = Main.getPlugin().Ldata;
    public ServerSQLGetter SData = Main.getPlugin().ServerData;
    public FriendSQLGetter FriendData = Main.getPlugin().Fdata;
    public PlayerSQLGetter PlayerData = Main.getPlugin().Pdata;
    public PermMySQLGetter PermData = Main.getPlugin().PermData;
    public freunde friend = Main.getPlugin().Freunde;
    private int SlotUsed;
    @EventHandler
    @SuppressWarnings("deprecation")
    public void onItemRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString().replace("-", "");
        ItemStack ItemInHand = p.getItemInHand();
        //Bukkit.broadcastMessage(ItemInHand.getType().toString()); // ITEM NAME
        //Bukkit.broadcastMessage(e.getAction().toString()); // ACTION
        if (e.getAction().toString().contains("CLICK")) {
            if (ItemInHand.getType().toString().equals("FEATHER")) {
                // Bukkit.broadcastMessage(e.getAction().toString());
                if (e.getAction().toString().contains("RIGHT")) {
                    if (lobbydata.Fly(uuid)) {
                        lobbydata.setFly(uuid, false);
                        ItemGetter.giveFlyTool(p, 1);
                    } else {
                        lobbydata.setFly(uuid, true);
                        ItemGetter.giveFlyTool(p, 1);
                    }
                } else if (e.getAction().toString().contains("LEFT")) {
                    double Speed = lobbydata.FlySpeed(uuid);
                    float speed = (float) Speed;
                    //Bukkit.broadcastMessage("§cSpeed: " + speed);
                    if(!p.isSneaking()) {
                         if (Speed == 0.25) {
                            lobbydata.setFlySpeed(uuid, 0.5);
                            ItemGetter.giveFlyTool(p, 1);
                        } else if (Speed == 0.5) {
                            lobbydata.setFlySpeed(uuid, 0.75);
                            ItemGetter.giveFlyTool(p, 1);
                        } else if (Speed == 0.75) {
                            lobbydata.setFlySpeed(uuid, 1);
                            ItemGetter.giveFlyTool(p, 1);
                        } else if (Speed == 1.0) {
                            lobbydata.setFlySpeed(uuid, 0.25);
                            ItemGetter.giveFlyTool(p, 1);
                        }
                    } else {
                        if (Speed == 0.25) {
                            lobbydata.setFlySpeed(uuid, 1);
                            ItemGetter.giveFlyTool(p, 1);
                        }  else if (Speed == 0.5) {
                            lobbydata.setFlySpeed(uuid, 0.25);
                            ItemGetter.giveFlyTool(p, 1);
                        }else if (Speed == 0.75) {
                            lobbydata.setFlySpeed(uuid, 0.5);
                            ItemGetter.giveFlyTool(p, 1);
                        } else if (Speed == 1) {
                            lobbydata.setFlySpeed(uuid, 0.75);
                            ItemGetter.giveFlyTool(p, 1);
                        }
                    }

                }
            } else if (ItemInHand.getType().toString().equals("NAME_TAG")) {
                if (lobbydata.AutoNick(uuid)) {
                    lobbydata.setNick(uuid, false);
                    ItemGetter.giveNickTool(p, 7);
                } else {
                    lobbydata.setNick(uuid, true);
                    ItemGetter.giveNickTool(p, 7);
                }
            } else if (ItemInHand.getType().toString().equals("GLOWSTONE_DUST")) {
                Inventory Server = Bukkit.createInventory(p, 9, ChatColor.GREEN + "Lobbys:");
                String Lobbylist = SData.Lobbys("LOBBY");
                String[] Lobbys = Lobbylist.split(",");
                for (int x = 0; x < Lobbys.length; x++) {
                    int PlayerCount = SData.OnlinePlayer(Lobbys[x]);
                    if (SData.ONLINE(Lobbys[x]).equals("yes")) {
                        ItemStack Item = ItemGetter.ItemsSHORT("§b" + Lobbys[x], "§6" + PlayerCount + " §aSpieler", Material.POTION);
                        Server.setItem(x, Item);
                    } else {
                        ItemStack Item = ItemGetter.ItemsSHORT("§b" + Lobbys[x], "§cOffline", Material.GLASS_BOTTLE);
                        Server.setItem(x, Item);
                    }
                }
                if (p.hasPermission("ascasia.TeamLobby")) {
                    if (SData.ServerCreated("TEAMLOBBY")) {
                        String TeamLobbylist = SData.Lobbys("TEAMLOBBY");
                        String[] TeamLobbys = TeamLobbylist.split(",");
                        int z = 8;
                        for (int x = 0; x < TeamLobbys.length; x++) {
                            int PlayerCount = SData.OnlinePlayer(TeamLobbys[x]);
                            if (SData.ONLINE(TeamLobbys[x]).equals("yes")) {
                                ItemStack Item = ItemGetter.ItemsSHORT("§c" + TeamLobbys[x], "§6" + PlayerCount + " §aSpieler", Material.POTION);
                                PotionMeta potionmeta = (PotionMeta) Item.getItemMeta();
                                potionmeta.setMainEffect(PotionEffectType.INCREASE_DAMAGE);
                                Item.setItemMeta(potionmeta);
                                Server.setItem(z - x, Item);
                            } else {
                                ItemStack Item = ItemGetter.ItemsSHORT("§c" + TeamLobbys[x], "§cOffline", Material.GLASS_BOTTLE);
                                Server.setItem(z - x, Item);
                            }
                        }
                    }
                }
                p.openInventory(Server);

            } else if (ItemInHand.getType().toString().equals("ENDER_EYE")) {
                Inventory Inv = Bukkit.createInventory(p, 9, ChatColor.RED + "Sichtbarkeit:");
                if (lobbydata.Mode(uuid).equals("ALL")) {
                    ItemStack ALL = new ItemStack(Material.LIME_DYE, 1);
                    ItemStack CHOOSEN = new ItemStack(Material.PURPLE_DYE, 1);
                    ItemStack NOONE = new ItemStack(Material.RED_DYE, 1);

                    ItemMeta ALL_meta = ALL.getItemMeta();
                    ALL_meta.setDisplayName("§aAlle Spieler");
                    ALL.setItemMeta(ALL_meta);

                    ItemMeta CHOOSEN_meta = CHOOSEN.getItemMeta();
                    CHOOSEN_meta.setDisplayName("§5Ausgewählte Spieler");
                    CHOOSEN.setItemMeta(CHOOSEN_meta);

                    ItemMeta NOONE_meta = NOONE.getItemMeta();
                    NOONE_meta.setDisplayName("§cKeine Spieler");
                    NOONE.setItemMeta(NOONE_meta);

                    ItemStack Settings = ItemGetter.ItemsSHORT("§cEinstellungen", "§7Hier kannst du selbst wählen wen du sehen willst", Material.REPEATER);

                    Inv.setItem(0, ALL);
                    Inv.setItem(1, CHOOSEN);
                    Inv.setItem(2, NOONE);
                    Inv.setItem(8, Settings);
                    p.openInventory(Inv);
                } else if (lobbydata.Mode(uuid).equals("NOONE")) {
                    ItemStack ALL = new ItemStack(Material.LIME_DYE, 1);
                    ItemStack CHOOSEN = new ItemStack(Material.PURPLE_DYE, 1);
                    ItemStack NOONE = new ItemStack(Material.RED_DYE, 1);

                    ItemMeta ALL_meta = ALL.getItemMeta();
                    ALL_meta.setDisplayName("§aAlle Spieler");
                    ALL.setItemMeta(ALL_meta);

                    ItemMeta CHOOSEN_meta = CHOOSEN.getItemMeta();
                    CHOOSEN_meta.setDisplayName("§5Ausgewählte Spieler");
                    CHOOSEN.setItemMeta(CHOOSEN_meta);

                    ItemMeta NOONE_meta = NOONE.getItemMeta();
                    NOONE_meta.setDisplayName("§cKeine Spieler");
                    NOONE.setItemMeta(NOONE_meta);

                    ItemStack Settings = ItemGetter.ItemsSHORT("§cEinstellungen", "§7Hier kannst du selbst wählen wen du sehen willst", Material.REPEATER);

                    Inv.setItem(0, ALL);
                    Inv.setItem(1, CHOOSEN);
                    Inv.setItem(2, NOONE);
                    Inv.setItem(8, Settings);
                    p.openInventory(Inv);
                } else if (lobbydata.Mode(uuid).equals("CHOOSEN")) {
                    ItemStack ALL = new ItemStack(Material.LIME_DYE, 1);
                    ItemStack CHOOSEN = new ItemStack(Material.PURPLE_DYE, 1);
                    ItemStack NOONE = new ItemStack(Material.RED_DYE, 1);

                    ItemMeta ALL_meta = ALL.getItemMeta();
                    ALL_meta.setDisplayName("§aAlle Spieler");
                    ALL.setItemMeta(ALL_meta);

                    ItemMeta CHOOSEN_meta = CHOOSEN.getItemMeta();
                    CHOOSEN_meta.setDisplayName("§5Ausgewählte Spieler");
                    CHOOSEN.setItemMeta(CHOOSEN_meta);

                    ItemMeta NOONE_meta = NOONE.getItemMeta();
                    NOONE_meta.setDisplayName("§cKeine Spieler");
                    NOONE.setItemMeta(NOONE_meta);

                    ItemStack Settings = ItemGetter.ItemsSHORT("§cEinstellungen", "§7Hier kannst du selbst wählen wen du sehen willst", Material.REPEATER);

                    Inv.setItem(0, ALL);
                    Inv.setItem(1, CHOOSEN);
                    Inv.setItem(2, NOONE);
                    Inv.setItem(8, Settings);
                    p.openInventory(Inv);
                }

            } else if (ItemInHand.getType().toString().equals("COMPASS")) {
                Inventory inv = Bukkit.createInventory(p, 45, ChatColor.AQUA + "Teleporter");
                for (int x = 0; x < 46; x++) {
                    if (lobbydata.ItemNumexists(x)) {
                        String Name = lobbydata.ItemName(x);
                        String lore1 = lobbydata.ItemLore1(Name);
                        String lore2 = lobbydata.ItemLore2(Name);
                        String lore3 = lobbydata.ItemLore3(Name);
                        String lore4 = lobbydata.ItemLore4(Name);
                        String Material = lobbydata.ItemMaterial(Name);
                        Material mat = org.bukkit.Material.getMaterial(Material);
                        ItemStack Item = ItemGetter.Items(Name, lore1, lore2, lore3, lore4, "empty", "empty", mat);
                        int pos = lobbydata.Itempos(Name);
                        inv.setItem(pos, Item);
                    }
                }
                p.openInventory(inv);

            } else if (ItemInHand.getType().toString().equals("SKELETON_SKULL")) {
                friend.getInventory(p  , 1);
            }
        }
        e.setCancelled(true);
    }
}
