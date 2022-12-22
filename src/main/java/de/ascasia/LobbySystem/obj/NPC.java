package de.ascasia.LobbySystem.obj;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import de.ascasia.LobbySystem.Items.Item;
import de.ascasia.LobbySystem.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.Visibility;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static de.ascasia.LobbySystem.obj.Hologram.createHologram;

public class NPC {
    public static HashMap<String , NPC > NPC_List = new HashMap<>();

    HashMap<String , String> CostumArgs = new HashMap<>();
    GameProfile profile;

    Location position;

    String ID;

    Pose pose;

    String Text;

    boolean jumping;
    String SkinName;

    // Items
    ItemStack ItemInMainHand;
    ItemStack ItemInOffHand;
    ItemStack Helmet;
    ItemStack Chestplate;
    ItemStack Leggings;
    ItemStack Boots;

    Activity activity;


    List<Pair<EquipmentSlot , ItemStack>> Equip = new ArrayList<>();


    // other
    boolean collidable;
    boolean invulnerable;

    boolean LookAtNearestPlayer;


    // GLow settings
    boolean Glowing;
    ChatColor GlowColor;

    ArmorStand Hologram;


    MinecraftServer server;
    ServerLevel level;

    PlayerTeam playerTeam;


    ServerPlayer npc;




    public NPC createNPC(String ID , Location position) {
        this.position = position;
        this.profile = new GameProfile(UUID.randomUUID() , ID);
        this.server = ((CraftServer) Bukkit.getServer()).getServer();
        this.level = ((CraftWorld )position.getWorld()).getHandle();
        this.ID = ID;
        return this;
    }

    public void spawn() {
        this.npc = new ServerPlayer(server , level , profile , null);
        npc.setPos(position.getX() , position.getY() , position.getZ());
        npc.setInvulnerable(invulnerable);
        npc.setJumping(jumping);
        npc.setOnGround(true);
        if (Glowing) {
            MobEffectInstance instance = new MobEffectInstance(MobEffects.GLOWING , 99999);
            npc.c(instance.getEffect() , EntityPotionEffectEvent.Cause.COMMAND);
        }
        npc.collides = collidable;




        this.playerTeam = new PlayerTeam(new Scoreboard(), profile.getId().toString().substring(0 , 15));
        this.playerTeam.getPlayers().add(ID);
        this.playerTeam.setNameTagVisibility(Team.Visibility.NEVER);
        if (Glowing) {
            if (GlowColor != null) {
                this.playerTeam.setColor(ChatFormatting.getByName(GlowColor.name()));
            } else {
                this.playerTeam.setColor(ChatFormatting.getByName("YELLOW"));
            }
        }

        if(pose != null) {
            npc.setPose(pose);
        }

        sendNMS();
        NPC_List.put(ID , this);
    }


    private void sendNMS() {
        Bukkit.getOnlinePlayers().forEach(player ->{
            ServerGamePacketListenerImpl ps = ((CraftPlayer) player).getHandle().connection;
            ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
            ps.send(new ClientboundAddPlayerPacket(npc));

            ps.send(ClientboundSetPlayerTeamPacket.createRemovePacket(playerTeam));
            ps.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam , true));
            loadEquip();
            if (!Equip.isEmpty()) {
                ps.send(new ClientboundSetEquipmentPacket(npc.getId(), Equip));
            }

            // Second Skin Layer
            SynchedEntityData data = npc.getEntityData(); // DataWatcher
            byte bitmask = (byte) (0x01 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40);
            data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), bitmask);
            if (Glowing) {
                byte bitmask1 = (byte) (0x40);
                data.set(new EntityDataAccessor<>(0, EntityDataSerializers.BYTE), bitmask1);
            }
            ps.send(new ClientboundSetEntityDataPacket(npc.getId() , data, true));

            if (activity != null) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        ps.send(new ClientboundAnimatePacket(npc, activity.getID()));
                    }
                }.runTaskTimerAsynchronously(Main.getPlugin(), 0, 5);
            }
            new BukkitRunnable() {

                @Override
                public void run() {
                    ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc));
                }
            }.runTaskLaterAsynchronously(Main.getPlugin() , 10);

        });
    }
    public void upload() {

    }
    public void setSkin(String SkinName) { // The username is the name for the player that has the skin.

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", SkinName)).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                ArrayList<String> lines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reader.lines().forEach(lines::add);

                String reply = String.join(" ", lines);
                int indexOfValue = reply.indexOf("\"value\": \"");
                int indexOfSignature = reply.indexOf("\"signature\": \"");
                String skin = reply.substring(indexOfValue + 10, reply.indexOf("\"", indexOfValue + 10));
                String signature = reply.substring(indexOfSignature + 14, reply.indexOf("\"", indexOfSignature + 14));
                profile.getProperties().put("textures", new Property("textures", skin, signature));
                this.SkinName = SkinName;
            } else {
                Bukkit.getConsoleSender().sendMessage("Connection could not be opened when fetching player skin (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void deleteHologram() {
        de.ascasia.LobbySystem.obj.Hologram.deleteHologram(Hologram);
    }


    private void loadEquip() {
        if (ItemInMainHand != null) {
            Equip.add(Pair.of(EquipmentSlot.MAINHAND, ItemInMainHand));
        }
        if (ItemInOffHand != null) {
            Equip.add(Pair.of(EquipmentSlot.OFFHAND, ItemInOffHand));
        }
        if (Helmet != null) {
            Equip.add(Pair.of(EquipmentSlot.HEAD, Helmet));
        }
        if (Chestplate != null) {
            Equip.add(Pair.of(EquipmentSlot.CHEST, Chestplate));
        }
        if (Leggings != null) {
            Equip.add(Pair.of(EquipmentSlot.LEGS, Leggings));
        }
        if (Boots != null) {
            Equip.add(Pair.of(EquipmentSlot.FEET, Boots));
        }
    }
    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
        Location loco = new Location(position.getWorld() , position.getX() , position.getY() , position.getZ());
        loco.setY(position.getY() - 0.25);
        this.Hologram = createHologram(loco , text);
    }

    public ServerPlayer getNpc() {
        return npc;
    }

    public void setNpc(ServerPlayer npc) {
        this.npc = npc;
    }

    public HashMap<String, String> getCostumArgs() {
        return CostumArgs;
    }

    public void setCostumArgs(HashMap<String, String> costumArgs) {
        CostumArgs = costumArgs;
    }
    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }
    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public boolean isLookAtNearestPlayer() {
        return LookAtNearestPlayer;
    }

    public void setLookAtNearestPlayer(boolean lookAtNearestPlayer) {
        LookAtNearestPlayer = lookAtNearestPlayer;
    }

    public boolean isGlowing() {
        return Glowing;
    }

    public void setGlowing(boolean glowing) {
        Glowing = glowing;
    }

    public ChatColor getGlowColor() {
        return GlowColor;
    }

    public void setGlowColor(ChatColor glowColor) {
        GlowColor = glowColor;
    }

    public void setPose(Pose pose) {
        this.pose = pose;
    }

    public Pose getPose() {
        return pose;
    }

    public Location getPosition() {
        return position;
    }

    public void setPosition(Location position) {
        this.position = position;
    }

    public org.bukkit.inventory.ItemStack getItemInMainHand() {
        return CraftItemStack.asBukkitCopy(ItemInMainHand);
    }

    public void setItemInMainHand(org.bukkit.inventory.ItemStack Item) {
        ItemInMainHand = CraftItemStack.asNMSCopy(Item);
    }

    public org.bukkit.inventory.ItemStack getItemInOffHand() {
        return CraftItemStack.asBukkitCopy(ItemInOffHand);
    }

    public void setItemInOffHand(org.bukkit.inventory.ItemStack Item) {
        ItemInOffHand = CraftItemStack.asNMSCopy(Item);
    }

    public org.bukkit.inventory.ItemStack getHelmet() {
        return CraftItemStack.asBukkitCopy(Helmet);
    }

    public void setHelmet(org.bukkit.inventory.ItemStack Item) {
        Helmet = CraftItemStack.asNMSCopy(Item);
    }
    public org.bukkit.inventory.ItemStack getChestplate() {
        return CraftItemStack.asBukkitCopy(Chestplate);
    }

    public void setChestplate(org.bukkit.inventory.ItemStack Item) {
        Chestplate = CraftItemStack.asNMSCopy(Item);
    }

    public org.bukkit.inventory.ItemStack getLeggings() {
        return CraftItemStack.asBukkitCopy(Leggings);
    }

    public void setLeggings(org.bukkit.inventory.ItemStack Item) {
        Leggings = CraftItemStack.asNMSCopy(Item);
    }

    public org.bukkit.inventory.ItemStack getBoots() {
        return CraftItemStack.asBukkitCopy(Boots);
    }

    public void setBoots(org.bukkit.inventory.ItemStack Item) {
        Boots = CraftItemStack.asNMSCopy(Item);
    }



}
