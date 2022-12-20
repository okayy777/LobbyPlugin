package de.ascasia.LobbySystem.obj;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Npc;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Material;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class NPC  {
    public static HashMap<String , ServerPlayer > NPC_List = new HashMap<>();


    public static void createNPC(Location position , String Name,  String ItemInHand , boolean Glowing ,
                                 String Glow_Color , boolean collidable , boolean ShowOnTab ,
                                  String SkinName) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel nmsWorld = ((CraftWorld)position.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID() , Name);
        ServerPlayer nmsNPC = new ServerPlayer(nmsServer , nmsWorld , gameProfile, null);
        nmsNPC.setPos(position.getX() , position.getY() , position.getZ());
        nmsNPC.setCustomNameVisible(false);
        nmsNPC.setInvulnerable(true);
        nmsNPC.setOnGround(true);
        nmsNPC.collides = collidable;
        nmsNPC.setPose(Pose.STANDING);

        NPC_List.put(Name , nmsNPC);
        sendSetNPCSkinPacket(nmsNPC , SkinName);


    }

    public static void addNPC(Player p) {
        for (ServerPlayer nmsNPC : NPC_List.values()) {
        }
    }

    public static void removeNPCPacket(ServerPlayer npc){
        for (Player player : Bukkit.getOnlinePlayers()) {
            Connection connection = ((CraftPlayer) player).getHandle().connection.getConnection();
            connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
        }
    }

    public static void addNPCPacket(ServerPlayer npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            ServerPlayer sp = craftPlayer.getHandle();
            ServerGamePacketListenerImpl ps = sp.connection;
            ps.send(new ClientboundAddPlayerPacket(npc));
        }
    }


    public static void sendSetNPCSkinPacket(ServerPlayer npc, String username) { // The username is the name for the player that has the skin.
        removeNPCPacket(npc);

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", username)).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                ArrayList<String> lines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reader.lines().forEach(lines::add);

                String reply = String.join(" ", lines);
                int indexOfValue = reply.indexOf("\"value\": \"");
                int indexOfSignature = reply.indexOf("\"signature\": \"");
                String skin = reply.substring(indexOfValue + 10, reply.indexOf("\"", indexOfValue + 10));
                String signature = reply.substring(indexOfSignature + 14, reply.indexOf("\"", indexOfSignature + 14));
                npc.getGameProfile().getProperties().put("textures", new Property("textures", skin, signature));
            } else {
                Bukkit.getConsoleSender().sendMessage("Connection could not be opened when fetching player skin (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        addNPCPacket(npc);
    }
}
