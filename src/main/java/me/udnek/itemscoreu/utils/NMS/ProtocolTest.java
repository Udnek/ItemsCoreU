package me.udnek.itemscoreu.utils.NMS;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.udnek.itemscoreu.ItemsCoreU;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class ProtocolTest{

    public static void modifyPacket(PacketContainer packet){
        Holder.Reference<MobEffect> holder = (Holder.Reference<MobEffect>) packet.getModifier().read(1);
        ResourceKey<MobEffect> key = holder.key();
        ResourceLocation resourceLocation = key.location();
        if (resourceLocation.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)){
            return;
        }
        packet.getModifier().write(1, MobEffects.HEAL);
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
