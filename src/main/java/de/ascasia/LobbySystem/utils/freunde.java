package de.ascasia.LobbySystem.utils;

import de.ascasia.LobbySystem.Items.Item;
import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class freunde {
    public Item ItemGetter = Main.getPlugin().item;
    public LobbySQLGetter lobbydata = Main.getPlugin().Ldata;
    public ServerSQLGetter SData = Main.getPlugin().ServerData;
    public FriendSQLGetter FriendData = Main.getPlugin().Fdata;
    public PlayerSQLGetter PlayerData = Main.getPlugin().Pdata;
    public PermMySQLGetter PermData = Main.getPlugin().PermData;
    private int SlotUsed;

    @SuppressWarnings("deprecation")
    public void getInventory(Player p , int Seite) {
        String uuid = p.getUniqueId().toString().replace("-", "");
        int start = 35;
        if (Seite != 1) {
            start = 35 * Seite;
        } else {
            start = 35;
        }
        Inventory inv = Bukkit.createInventory(p, 54, ChatColor.DARK_AQUA + "Freunde-Menü:");
        String F = FriendData.Friends(uuid);
        int seiten = 1;
        if (F != null) {
            String[] Flist = F.split(",");
            seiten = (Flist.length / 36) + 1;
        }
        ItemStack Item = ItemGetter.ItemsSHORT("§eNächste Seite", "§71/" + seiten, Material.OAK_BUTTON);
        inv.setItem(53, Item);
        ItemStack Item2 = ItemGetter.ItemsSHORT("§eVorherige Seite", "§71/" + seiten, Material.OAK_BUTTON);
        inv.setItem(45, Item2);
        ItemStack Slot53 = ItemGetter.Items("§cEinstellungen", "§7Hier kannst du Einstellungen", "§7für dein Profil vornehmen", "empty", "empty", "empty", "empty", Material.COMPARATOR);
        ItemStack Slot52 = ItemGetter.ItemsSHORT("§eFreundschaftsanfragen", "§7Hier siehst du deine §eFreundschaftsanfragen", Material.WRITABLE_BOOK);
        ItemStack Slot51 = ItemGetter.ItemsSHORT("§9Clan-Menü", "§7Hier kannst du Claneinstellungen vornehmen", Material.DIAMOND_CHESTPLATE);
        ItemStack ChestPlate = new ItemStack(Material.GOLDEN_CHESTPLATE);
        ItemStack Slot50 = ItemGetter.SkullSHORT("§9Freunde-Menü", "§7Hier siehst du alle deine Freunde", ChestPlate);
        inv.setItem(52, Slot53);
        inv.setItem(51, Slot52);
        inv.setItem(50, Slot51);
        //inv.setItem(49 , Slot50);
        ItemStack Glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemStack Spacer = ItemGetter.Skull("§c", "empty", "empty", "empty", "empty", "empty", "empty", Glass);
        inv.setItem(44, Spacer);
        inv.setItem(43, Spacer);
        inv.setItem(42, Spacer);
        inv.setItem(41, Spacer);
        inv.setItem(40, Spacer);
        inv.setItem(39, Spacer);
        inv.setItem(38, Spacer);
        inv.setItem(37, Spacer);
        inv.setItem(36, Spacer);


        // VIPS (online) -> Freunde (online) -> Vips (offline) -> Freunde offline
        SlotUsed = 0;
        String Vips = FriendData.VIPS(uuid);
        String Friends = FriendData.Friends(uuid);
        String[] vip = null;
        if (Vips != null) {
            vip = Vips.split(",");
        }

        if (Vips != null) {
            for (int x = 0; x < vip.length; x++) {
                if (Friends.contains(vip[x])) {
                    F = F.replace(vip[x] + ",", "");
                }
            }
        }

        String[] friend = null;
        if (Friends != null) {
            friend = Friends.split(",");
        }

        //VIPS (online)
        if (Vips != null) {
            for (int v1 = 0; v1 < vip.length; v1++) {
                if (SlotUsed < 35) {
                    int num = 0;
                    if (Seite > 1) {
                        if (vip.length > ((Seite - 1) * 35)) {
                            num = (Seite - 1) * 35;
                        } else {
                            break;
                        }
                    }
                    int pos = num + v1;
                    if (PlayerData.isOnline(vip[pos])) {
                        String name = PlayerData.getName(vip[pos]);
                        String Rang = PlayerData.getRang(vip[pos]);
                        String Prefix = PermData.getPrefix(Rang);
                        String Clan = "";
                        String PlayerName = Prefix + " §7" + name + "§e✩ " + Clan;
                        String Server = PlayerData.Server(vip[pos]);
                        ItemStack skull = new ItemStack(Skull.getPlayerSkull(name));
                        ItemStack Head = ItemGetter.Skull(PlayerName, "§aOnline §7auf §e" + Server, "empty", "empty", "empty", "empty", "empty", skull);
                        inv.setItem(SlotUsed, Head);
                        SlotUsed++;
                    }
                } else {
                    break;
                }
            }
        }
        //FRIENDS (online)
        if (friend != null) {
            for (int f1 = 0; f1 < friend.length; f1++) {
                if (SlotUsed < 35) {
                    int num = 0;
                    if (Seite > 1) {
                        if (friend.length > ((Seite - 1) * 35)) {
                            num = (Seite - 1) * 35;
                        } else {
                            break;
                        }
                    }
                    int pos = num + f1;
                    if (PlayerData.isOnline(friend[pos])) {
                        String name = PlayerData.getName(friend[pos]);
                        String Rang = PlayerData.getRang(friend[pos]);
                        String Prefix = PermData.getPrefix(Rang);
                        String Clan = "";
                        String PlayerName = Prefix + " §7" + name + Clan;
                        String Server = PlayerData.Server(friend[pos]);
                        ItemStack skull = new ItemStack(Skull.getPlayerSkull(name));
                        ItemStack Head = ItemGetter.Skull(PlayerName, "§aOnline §7auf §e" + Server, "empty", "empty", "empty", "empty", "empty", skull);
                        inv.setItem(SlotUsed, Head);
                        SlotUsed++;
                    }
                } else {
                    break;
                }
            }

        }
        //VIPS (offline)
        if (Vips != null) {
            for (int v1 = 0; v1 < vip.length; v1++) {
                if (SlotUsed < 35) {
                    int num = 0;
                    if (Seite > 1) {
                        if (vip.length > ((Seite - 1) * 35)) {
                            num = (Seite - 1) * 35;
                        } else {
                            break;
                        }
                    }
                    int pos = num + v1;
                    if (!PlayerData.isOnline(vip[pos])) {
                        String name = PlayerData.getName(vip[pos]);
                        String Rang = PlayerData.getRang(vip[pos]);
                        String Prefix = PermData.getPrefix(Rang);
                        String Clan = "";
                        String PlayerName = Prefix + " §7" + name + "§e✩ " + Clan;
                        String Server = PlayerData.Server(vip[pos]);
                        ItemStack skull = new ItemStack(Material.SKELETON_SKULL, 1, (byte) 1);
                        ItemStack Head = ItemGetter.Skull(PlayerName, "§cOffline", "empty", "empty", "empty", "empty", "empty", skull);
                        inv.setItem(SlotUsed, Head);
                        SlotUsed++;
                    }
                } else {
                    break;
                }
            }
        }
        //FRIENDS (online)
        if (friend != null) {
            for (int f1 = 0; f1 < friend.length; f1++) {
                if (SlotUsed < 35) {
                    int num = 0;
                    if (Seite > 1) {
                        if (friend.length > ((Seite - 1) * 35)) {
                            num = (Seite - 1) * 35;
                        } else {
                            break;
                        }
                    }
                    int pos = num + f1;
                    if (!PlayerData.isOnline(friend[pos])) {
                        String name = PlayerData.getName(friend[pos]);
                        String Rang = PlayerData.getRang(friend[pos]);
                        String Prefix = PermData.getPrefix(Rang);
                        String Clan = "";
                        String PlayerName = Prefix + " §7" + name + Clan;
                        String Server = PlayerData.Server(friend[pos]);
                        ItemStack skull = new ItemStack(Material.SKELETON_SKULL, 1, (byte) 1);
                        ItemStack Head = ItemGetter.Skull(PlayerName, "§cOffline", "empty", "empty", "empty", "empty", "empty", skull);
                        inv.setItem(SlotUsed, Head);
                        SlotUsed++;
                    }
                } else {
                    break;
                }
            }

        }
        p.openInventory(inv);
    }
}
