package de.ascasia.LobbySystem.utils;

import de.ascasia.LobbySystem.Main;
import de.ascasia.LobbySystem.sql.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Sichtbar {
    public LobbySQLGetter lobbydata = Main.getPlugin().Ldata;
    public PermMySQLGetter PermData = Main.getPlugin().PermData;
    public PlayerSQLGetter pdata = Main.getPlugin().Pdata;
    public FriendSQLGetter FriendData = Main.getPlugin().Fdata;
    public PartySQLGetter PartyData = Main.getPlugin().PAdata;
    public ServerSQLGetter ServerData = Main.getPlugin().ServerData;

    public void setSicht(Player p) {
        String uuid = p.getUniqueId().toString().replace("-" ,"");
        boolean friend = lobbydata.SichtFreunde(uuid);
        boolean clan = lobbydata.SichtClan(uuid);
        boolean Party = lobbydata.SichtParty(uuid);
        boolean staff = lobbydata.SichtStaff(uuid);
        boolean Premium = lobbydata.SichtPremium(uuid);
        boolean spieler = lobbydata.SichtSpieler(uuid);
        boolean favs = lobbydata.SichtFavoriten(uuid);
        for (Player pl : Bukkit.getOnlinePlayers()) {
            String pluuid = pl.getUniqueId().toString().replace("-" ,"");
            if (pl.hasPermission("ascasia.Team")) {
                if (staff) {
                    p.showPlayer(pl);
                } else {
                    p.hidePlayer(pl);
                }
            } else if (pdata.getRang(uuid).equals("spieler")) {
                if (spieler) {
                    p.showPlayer(pl);
                } else {
                    p.hidePlayer(pl);
                }
            }  else if (pdata.getRang(uuid).equals("premium")) {
                if (Premium) {
                    p.showPlayer(pl);
                } else {
                    p.hidePlayer(pl);
                }
            } else if (FriendData.PlayerInFL(uuid , pluuid)) {
                if (friend) {
                    p.showPlayer(pl);
                } else {
                    p.hidePlayer(pl);
                }
            } else if (FriendData.PlayerInFLisFav(uuid , pluuid)) {
                if (favs) {
                    p.showPlayer(pl);
                } else {
                    p.hidePlayer(pl);
                }
            }// CLAN UND PARTY FEHLT
        }
    }
}
