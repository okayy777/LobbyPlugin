package de.ascasia.LobbySystem.Listener;


import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class NPCRightClick{
    @EventHandler
    public void onNPCInteraction(PlayerInteractAtEntityEvent e) {
        LivingEntity entity = (LivingEntity) e.getRightClicked();
        if (!entity.hasAI()) {
            if (entity.getCustomName().contains("Dev")) {

            }
        }

    }
}