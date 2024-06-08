package me.udnek.itemscoreu.utils.NMS;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedAttribute;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProtocolTest extends PacketAdapter{

    public ProtocolTest(@NotNull PacketAdapter.AdapterParameteters params) {
        super(params);
    }

    public ProtocolTest(Plugin plugin, PacketType... types) {
        super(plugin, types);
    }

    public ProtocolTest(Plugin plugin, Iterable<? extends PacketType> types) {
        super(plugin, types);
    }

    public ProtocolTest(Plugin plugin, ListenerPriority listenerPriority, Iterable<? extends PacketType> types) {
        super(plugin, listenerPriority, types);
    }

    public ProtocolTest(Plugin plugin, ListenerPriority listenerPriority, Iterable<? extends PacketType> types, ListenerOptions... options) {
        super(plugin, listenerPriority, types, options);
    }

    public ProtocolTest(Plugin plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        //LogUtils.log(String.valueOf(packetEvent.getPacketType()));
        if(packetEvent.getPacketType() != PacketType.Play.Server.UPDATE_ATTRIBUTES) return;
        //LogUtils.log("ATTR PACKET");
        PacketContainer packet = packetEvent.getPacket();
/*        StructureModifier<List<WrappedAttribute>> modifier = packet.getAttributeCollectionModifier();
        for (List<WrappedAttribute> value : modifier.getValues()) {
            for (WrappedAttribute wrappedAttribute : value) {
                LogUtils.log(wrappedAttribute.getAttributeKey());
            }
        }*/
        //LogUtils.log(String.valueOf(modifier));
    }
}
