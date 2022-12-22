package de.ascasia.LobbySystem.obj;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Hologram {
    public static ArmorStand createHologram(Location location, String Text) {
        Text = Text.replace("&" , "§");
        ArmorStand hologram = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setCustomNameVisible(true);
        hologram.setCustomName(Text);
        hologram.setGravity(false);
        hologram.setCanPickupItems(false);
        hologram.setInvulnerable(true);
        return hologram;
    }

    public static void deleteHologram(ArmorStand Hologram) {
        Hologram.remove();
    }
}
