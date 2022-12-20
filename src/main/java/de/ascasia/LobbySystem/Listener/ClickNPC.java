package de.ascasia.LobbySystem.Listener;

import de.ascasia.LobbySystem.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class ClickNPC implements Listener {
    private int i;
    @EventHandler
    public void onClickNPC() {
        /*

        Player p = e.getPlayer();
        if (e.getNPC().getBukkitEntity().getProfile().getName().equals("§4Güntha")) {
            i = 0;
            BukkitRunnable run = new BukkitRunnable() {
                @Override
                public void run() {
                    while (i < 3) {
                        if (i == 0){
                            p.sendMessage("§4Güntha"+"§8: " + "§cIch bin zwar Faul da ich hier den ganzen Tag nur rumstehe");
                            i++;
                            break;
                        } else if (i == 1){
                            p.sendMessage("§4Güntha"+"§8: " +"§cAber §6DXXDLY §cist noch fauler ");
                            p.sendMessage("§4Güntha"+"§8: " +"§cda er den §6GANZEN §cTag VALORANT spielt");
                            i++;
                            break;
                        } else if( i== 2) {
                            p.sendMessage("§4Güntha"+"§8: " +"§cSolltest du §6Dxxdly §csein §4--> §6HAB DICH LIEB!");
                            i++;
                            break;
                        }
                    }
                }
            };
            run.runTaskTimer(Main.getPlugin() , 2 , 20);
        }


         */
    }


}
