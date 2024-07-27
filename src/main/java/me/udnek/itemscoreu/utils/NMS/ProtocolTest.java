package me.udnek.itemscoreu.utils.NMS;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import me.udnek.itemscoreu.ItemsCoreU;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ProtocolTest{
    public static void kek(){
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                ItemsCoreU.getInstance(),
                ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_EFFECT
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                player.sendMessage(event.getPacket().getIntegers().getField(1).toString());
            }
        });
    }
}
