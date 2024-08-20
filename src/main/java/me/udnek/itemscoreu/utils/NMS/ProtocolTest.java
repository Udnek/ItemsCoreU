package me.udnek.itemscoreu.utils.NMS;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.udnek.itemscoreu.ItemsCoreU;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.bukkit.EntityEffect;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class ProtocolTest{

    public static void modifyPacket(PacketContainer packet){
        Holder.Reference<MobEffect> holder = (Holder.Reference<MobEffect>) packet.getModifier().read(1);
        MobEffect effect = holder.value();
        ResourceKey<MobEffect> key = holder.key();
        ResourceLocation resourceLocation = key.location();
        //player.sendMessage(holder.key().toString());
        //player.sendMessage(holder.key().location().toString());
        //player.sendMessage(holder.key().location().getNamespace());
        if (resourceLocation.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)){
            //player.sendMessage(Component.text("OKEY").color(NamedTextColor.GREEN));
            return;
        }

        //player.sendMessage(Component.text("MODIFING").color(NamedTextColor.RED));

        packet.getModifier().write(1, MobEffects.ABSORPTION);
    }

    public static void kek(){
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                ItemsCoreU.getInstance(),
                ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_EFFECT, PacketType.Play.Server.REMOVE_ENTITY_EFFECT
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                modifyPacket(event.getPacket());
            }
        });
    }
}
