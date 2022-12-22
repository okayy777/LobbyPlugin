package de.ascasia.LobbySystem.Listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.ascasia.LobbySystem.Items.Item;
import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.*;
import de.ascasia.LobbySystem.utils.Color;
import de.ascasia.LobbySystem.utils.Sichtbar;
import de.ascasia.LobbySystem.utils.Skull;
import de.ascasia.LobbySystem.utils.nameCHECK1;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;

public class InventoryClick implements Listener {
    public LobbySQLGetter lobbydata = Main.getPlugin().Ldata;
    public PermMySQLGetter PermData = Main.getPlugin().PermData;
    public PlayerSQLGetter pdata = Main.getPlugin().Pdata;
    public FriendSQLGetter FriendData = Main.getPlugin().Fdata;
    public PartySQLGetter PartyData = Main.getPlugin().PAdata;
    public ServerSQLGetter ServerData = Main.getPlugin().ServerData;
    public Item ItemGetter = Main.getPlugin().item;
    public Sichtbar Sicht = Main.getPlugin().Sicht;
    public Color Farbe = Main.getPlugin().Farbe;

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onClick (InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            Player p = (Player) e.getWhoClicked();
            Inventory clicked = e.getClickedInventory();


            String InvTitle = null;

            for (HumanEntity HE : clicked.getViewers()) {
                InvTitle = HE.getOpenInventory().getTitle();
                break;
            }
            String uuid = p.getUniqueId().toString().replace("-", "");

            //Bukkit.broadcastMessage(String.valueOf(e.getSlot()));

            //Bukkit.broadcastMessage(InvTitle);

            // Sichtbarkeits Inventory
            if (InvTitle.contains("Sichtbarkeit")) {
                e.setCancelled(true);
                ItemStack Item = e.getCurrentItem();
                //Auswahl
                if (Item.getItemMeta().getDisplayName().contains("Alle")) {
                    if (!lobbydata.Mode(uuid).equals("ALL")) {
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

                        clicked.setItem(0, ALL);
                        clicked.setItem(1, CHOOSEN);
                        clicked.setItem(2, NOONE);
                        lobbydata.setMode(uuid , "ALL");
                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            p.showPlayer(pl);
                        }
                        e.getWhoClicked().sendMessage("§aDu siehst nun alle Spieler!");
                    }
                } else if (Item.getItemMeta().getDisplayName().contains("Ausgewählte")) {
                    if (!lobbydata.Mode(uuid).equals("CHOOSEN")) {
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

                        clicked.setItem(0, ALL);
                        clicked.setItem(1, CHOOSEN);
                        clicked.setItem(2, NOONE);
                        lobbydata.setMode(uuid , "CHOOSEN");
                        Sicht.setSicht(p);
                        e.getWhoClicked().sendMessage(ChatColor.DARK_PURPLE + "Du siehst nur noch ausgewählte Spieler!");

                    }
                } else if (Item.getItemMeta().getDisplayName().contains("Keine")) {
                    if (!lobbydata.Mode(uuid).equals("NOONE")) {
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

                        clicked.setItem(0, ALL);
                        clicked.setItem(1, CHOOSEN);
                        clicked.setItem(2, NOONE);
                        lobbydata.setMode(uuid , "NOONE");
                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            p.hidePlayer(pl);
                        }
                        e.getWhoClicked().sendMessage("§cDu siehst nun keinen Spieler!");

                    }
                }
                else if (Item.getItemMeta().getDisplayName().contains("Einstellungen")) {
                    p.closeInventory();
                    Inventory inv = Bukkit.createInventory(p , 36 , ChatColor.RED +"Einstellungen");

                    ItemStack Head1 = new ItemStack(Skull.getCustomSkull("http://textures.minecraft.net/texture/df2c9dc314befc8d12f2e8786c9435bfd24d9116fc94c5ec45fd83bd2dc84a4f")); // Spieler
                    ItemStack Head2 = new ItemStack(Skull.getCustomSkull("http://textures.minecraft.net/texture/657f75a33f1cfbffc1374d663cce5b35d2edf9300ff718abf8d6a2a55e2b97e3")); // Premi
                    ItemStack Head3 = new ItemStack(Skull.getCustomSkull("http://textures.minecraft.net/texture/78fc5c47982374d97acdf782bfcb10d7ce8ff7ffea3c12b71448a29f39630fcd")); //VIPS
                    ItemStack Head4 = new ItemStack(Skull.getCustomSkull("http://textures.minecraft.net/texture/dfc31605b54c4fcf4bbc62f331d85440093e7c2d3f2dd152b67f15e6c444f74d")); // CLan
                    ItemStack Head5 = new ItemStack(Skull.getCustomSkull("http://textures.minecraft.net/texture/a24f3c846d552cbdc366d8751dd4bfabde60a3adad535c3620b1a0af5d3f553a")); // Party
                    ItemStack Head6 = new ItemStack(Skull.getCustomSkull("http://textures.minecraft.net/texture/96b194059a0ebcf227f88f2223de1eaf5397c8837e84e4b39e8c7fbe128f32")); // Staff
                    ItemStack Head7 = new ItemStack(Skull.getCustomSkull("http://textures.minecraft.net/texture/621288b412b52945bfac725efc2b107063b99f5b8ee904b490f98090a751ee90")); // Friend

                    //10-16
                    ItemStack Slot10 = ItemGetter.Skull("§eSpieler" , "empty", "empty", "empty", "empty", "empty", "empty" ,Head1);
                    ItemStack Slot11 = ItemGetter.Skull("§ePremium" , "empty", "empty", "empty", "empty", "empty", "empty" ,Head2);
                    ItemStack Slot12 = ItemGetter.Skull("§eFreunde" , "empty", "empty", "empty", "empty", "empty", "empty" ,Head7);
                    ItemStack Slot13 = ItemGetter.Skull("§eFavoriten" , "empty", "empty", "empty", "empty", "empty", "empty" ,Head3);
                    ItemStack Slot14 = ItemGetter.Skull("§eClan" , "empty", "empty", "empty", "empty", "empty", "empty" ,Head4);
                    ItemStack Slot15 = ItemGetter.Skull("§eParty" , "empty", "empty", "empty", "empty", "empty", "empty" ,Head5);
                    ItemStack Slot16 = ItemGetter.Skull("§cTeammitglieder" , "empty", "empty", "empty", "empty", "empty", "empty" ,Head6);
                    inv.setItem(10 , Slot10);
                    inv.setItem(11 , Slot11);
                    inv.setItem(12 , Slot12);
                    inv.setItem(13 , Slot13);
                    inv.setItem(14 , Slot14);
                    inv.setItem(15 , Slot15);
                    inv.setItem(16 , Slot16);
                    //19-25 ✓✕
                    ItemStack silv = new ItemStack(Material.GRAY_DYE, 1);
                    ItemStack green = new ItemStack(Material.LIME_DYE, 1);
                    ItemStack Grey = ItemGetter.Skull("§c✕", "empty", "empty", "empty", "empty", "empty", "empty" ,silv);
                    ItemStack Green = ItemGetter.Skull("§a✓", "empty", "empty", "empty", "empty", "empty", "empty" ,green);
                    if (lobbydata.SichtSpieler(uuid)) {
                        inv.setItem(19 , Green);
                    } else {
                        inv.setItem(19 , Grey);
                    }
                    if (lobbydata.SichtPremium(uuid)) {
                        inv.setItem(20 , Green);
                    } else {
                        inv.setItem(20 , Grey);
                    }
                    if (lobbydata.SichtFreunde(uuid)) {
                        inv.setItem(21 , Green);
                    } else {
                        inv.setItem(21 , Grey);
                    }
                    if (lobbydata.SichtFavoriten(uuid)) {
                        inv.setItem(22 , Green);
                    } else {
                        inv.setItem(22 , Grey);
                    }
                    if (lobbydata.SichtClan(uuid)) {
                        inv.setItem(23 , Green);
                    } else {
                        inv.setItem(23 , Grey);
                    }
                    if (lobbydata.SichtParty(uuid)) {
                        inv.setItem(24 , Green);
                    } else {
                        inv.setItem(24 , Grey);
                    }
                    if (lobbydata.SichtStaff(uuid)) {
                        inv.setItem(25 , Green);
                    } else {
                        inv.setItem(25, Grey);
                    }
                    p.openInventory(inv);





                }
            } else if (InvTitle.contains("Einstellungen")) {
                e.setCancelled(true);
                ItemStack Item = e.getCurrentItem();
                int slot = e.getSlot();
                ItemStack silv = new ItemStack(Material.GRAY_DYE, 1);
                ItemStack green = new ItemStack(Material.LIME_DYE, 1);
                ItemStack Grey = ItemGetter.Skull("§c✕", "empty", "empty", "empty", "empty", "empty", "empty" ,silv);
                ItemStack Green = ItemGetter.Skull("§a✓", "empty", "empty", "empty", "empty", "empty", "empty" ,green);
                if(slot == 19) {
                    if(lobbydata.SichtSpieler(uuid)) {
                        lobbydata.setSPIELER(uuid , false);
                        clicked.setItem(slot , Grey);
                    } else {
                        lobbydata.setSPIELER(uuid , true);
                        clicked.setItem(slot , Green);
                    }
                } else if(slot == 20) {
                    if(lobbydata.SichtPremium(uuid)) {
                        lobbydata.setPREMIUM(uuid , false);
                        clicked.setItem(slot , Grey);
                    } else {
                        lobbydata.setPREMIUM(uuid , true);
                        clicked.setItem(slot , Green);
                    }
                } else if(slot == 21) {
                    if(lobbydata.SichtFreunde(uuid)) {
                        lobbydata.setFREUNDE(uuid , false);
                        clicked.setItem(slot , Grey);
                    } else {
                        lobbydata.setFREUNDE(uuid , true);
                        clicked.setItem(slot , Green);
                    }
                }else if(slot == 22) {
                    if(lobbydata.SichtFavoriten(uuid)) {
                        lobbydata.setFAVORITEN(uuid , false);
                        clicked.setItem(slot , Grey);
                    } else {
                        lobbydata.setFAVORITEN(uuid , true);
                        clicked.setItem(slot , Green);
                    }
                } else if(slot == 23) {
                    if(lobbydata.SichtClan(uuid)) {
                        lobbydata.setCLAN(uuid , false);
                        clicked.setItem(slot , Grey);
                    } else {
                        lobbydata.setCLAN(uuid , true);
                        clicked.setItem(slot , Green);
                    }
                } else if(slot == 24) {
                    if(lobbydata.SichtParty(uuid)) {
                        lobbydata.setPARTY(uuid , false);
                        clicked.setItem(slot , Grey);
                    } else {
                        lobbydata.setPARTY(uuid , true);
                        clicked.setItem(slot , Green);
                    }
                } else if(slot == 25) {
                    if(lobbydata.SichtStaff(uuid)) {
                        lobbydata.setSTAFF(uuid , false);
                        clicked.setItem(slot , Grey);
                    } else {
                        lobbydata.setSTAFF(uuid , true);
                        clicked.setItem(slot , Green);
                    }
                }
            } else if (InvTitle.contains("Teleporter")) {
                e.setCancelled(true);
                int slot = e.getSlot();
                if (lobbydata.ItemSlotexists(slot)) {
                    String Name = lobbydata.ItemNameSlot(slot);
                    if (lobbydata.Itemfunction(Name).equals("teleport")){
                        String Loca = lobbydata.ItemPosition(Name);
                        String[] loc = Loca.split(",");
                        double x = Double.parseDouble(loc[0]);
                        double y = Double.parseDouble(loc[1]);
                        double z = Double.parseDouble(loc[2]);
                        float yaw = Float.parseFloat(loc[3]);
                        float pitch = Float.parseFloat(loc[4]);
                        World w = Bukkit.getWorld(loc[5]);
                        Location teleport = new Location(w ,x , y, z , yaw , pitch);
                        p.teleport(teleport);

                    }
                }

            } else if (InvTitle.contains("Lobbys") || InvTitle.contains("Dev-Server")) {
                e.setCancelled(true);
                ItemStack clickedItem = e.getCurrentItem();
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                String ItemName = clickedItem.getItemMeta().getDisplayName();
                String ItemOhneFarbe = Farbe.ColorRemove(ItemName);
                if (clickedItem.getType() != null) {
                    if (!ItemOhneFarbe.equals(pdata.Server(uuid))) {
                        if (clickedItem.getItemMeta().getDisplayName().contains("Team")) {
                            if (p.hasPermission("ascasia.TeamLobby")) {
                                out.writeUTF(ItemOhneFarbe);
                                p.sendPluginMessage(Main.getPlugin(), "BungeeCord", out.toByteArray());
                            }
                        } else {
                            out.writeUTF(ItemOhneFarbe);
                            p.sendPluginMessage(Main.getPlugin(), "BungeeCord", out.toByteArray());
                        }
                    }
                }
            }
            else if (InvTitle.contains("Freunde")) {
                e.setCancelled(true);
                ItemStack clickedItem = e.getCurrentItem();
                if (clickedItem.getType().toString().equals("SKULL_ITEM")) {
                    String DisplayName = clickedItem.getItemMeta().getDisplayName().replace("  ", " ");
                    String[] PlayerName = DisplayName.split(" ");
                    if (PlayerName.length > 2) {
                        String name = Farbe.ColorRemove(PlayerName[2]);
                        Inventory inv = Bukkit.createInventory(p , 18 , "§3Spieler-Optionen");
                        Runnable runn = new BukkitRunnable() {
                            @Override
                            public void run() {
                                String puuid = null;
                                try {
                                    puuid = nameCHECK1.main(name);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                LocalDateTime fd = FriendData.getDate(uuid , puuid).toLocalDate().atTime(FriendData.getTime(uuid , puuid).toLocalTime());
                                LocalDateTime now = LocalDateTime.now();
                                GregorianCalendar than = new GregorianCalendar(fd.getYear() , fd.getMonthValue() , fd.getDayOfMonth() , fd.getHour() , fd.getMinute(), fd.getSecond());
                                GregorianCalendar today = new GregorianCalendar(now.getYear() , now.getMonthValue() , now.getDayOfMonth() , now.getHour() , now.getMinute(), now.getSecond());
                                long difference = today.getTimeInMillis() - than.getTimeInMillis();
                                int d = (int)(difference / (1000 * 60 * 60 * 24));
                                int h = (int)(difference / (1000 * 60 * 60) % 24);
                                int m = (int)(difference / (1000 * 60) % 60);
                                int s = (int)(difference / 1000 % 60);
                                ItemStack skull = new ItemStack(Skull.getPlayerSkull(name));
                                ItemStack Head = null;
                                if (d == 0) {
                                    if (h == 0) {
                                        if (m ==0) {
                                            Head = ItemGetter.Skull(DisplayName, "§7Befreundet seit:", "§a§l"+s + " §7Sekunden", "empty", "empty", "empty", "empty", skull);
                                        } else {
                                            Head = ItemGetter.Skull(DisplayName, "§7Befreundet seit:", "§a§l"+m + " §7Minuten", "empty", "empty", "empty", "empty", skull);
                                        }
                                    } else {
                                        Head = ItemGetter.Skull(DisplayName, "§7Befreundet seit:", "§a§l"+h + " §7Stunden", "empty", "empty", "empty", "empty", skull);
                                    }
                                } else {
                                    Head = ItemGetter.Skull(DisplayName, "§7Befreundet seit:", "§a§l"+ d + " §7Tagen", "empty", "empty", "empty", "empty", skull);
                                }
                                inv.setItem(4 , Head);
                            }
                        };
                        runn.run();
                        ItemStack space = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                        ItemStack spacer = ItemGetter.SkullSHORT("" , "empty" , space);
                        inv.setItem(0 , spacer);
                        inv.setItem(1 , spacer);
                        inv.setItem(2 , spacer);
                        inv.setItem(3 , spacer);
                        inv.setItem(5 , spacer);
                        inv.setItem(6 , spacer);
                        inv.setItem(7 , spacer);
                        inv.setItem(9 , spacer);
                        inv.setItem(10 , spacer);
                        inv.setItem(16 , spacer);
                        inv.setItem(17 , spacer);
                        ItemStack Glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                        ItemStack Back = ItemGetter.SkullSHORT( "§c§lZurück" , "§7Zurück zur Startseite" , Glass);
                        inv.setItem(8, Back);
                        ItemStack Item1 = ItemGetter.ItemsSHORT( "§e"+ name + " §5nachspringen" , "empty" , Material.ENDER_PEARL);
                        inv.setItem(11 , Item1);
                        ItemStack Item2 = ItemGetter.ItemsSHORT( "§e"+ name + " §9in deinen Clan einladen" , "empty" , Material.DIAMOND_CHESTPLATE);
                        inv.setItem(12 , Item2);
                        ItemStack Item3 = ItemGetter.ItemsSHORT( "§e"+ name + " §6in deine Party einladen" , "empty" , Material.GOLDEN_CARROT);
                        inv.setItem(13 , Item3);
                        ItemStack Head1 = new ItemStack(Skull.getCustomSkull("http://textures.minecraft.net/texture/3b7891745686511d539aad92ba3037b025abaddb9e2ee6e59c79977a717a4f9b")); // VIP
                        ItemStack Item4 = ItemGetter.SkullSHORT( "§e"+ name + " §bzu deinen Favoriten hinzufügen" , "empty" , Head1);
                        inv.setItem(14 , Item4);
                        ItemStack Item5 = ItemGetter.ItemsSHORT( "§e"+ name + " §centfernen" , "empty" , Material.BARRIER);
                        inv.setItem(15 , Item5);
                        p.openInventory(inv);

                    }
                } else if (clickedItem.getItemMeta().getDisplayName().contains("Seite")) {
                    String DisplayName = clickedItem.getItemMeta().getDisplayName();
                    if (DisplayName.contains("Nächste")) {

                    }
                }
            }
            if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                e.setCancelled(true);
            }

        }

    }
}
