package me.udnek.itemscoreu.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customeffect.ConstructableCustomEffect;
import me.udnek.itemscoreu.customeffect.CustomEffect;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import org.bukkit.potion.PotionEffectType;

public class PacketHandler {

    public static void initialize() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                ItemsCoreU.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_EFFECT, PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Holder.Reference<MobEffect> holder = (Holder.Reference<MobEffect>) packet.getModifier().read(1);
                ResourceLocation id = holder.key().location();
                if (id.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) return;
                CustomEffect customEffect = CustomRegistries.EFFECT.get(id.toString());
                if (customEffect instanceof ConstructableCustomEffect disguiseable) {
                    PotionEffectType vanillaDisguise = disguiseable.getVanillaDisguise();
                    if (vanillaDisguise == null) event.setCancelled(true);
                    else packet.getModifier().write(1, NmsUtils.toNms(Registries.MOB_EFFECT, vanillaDisguise));
                }
            }
        });


/*        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                ItemsCoreU.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Server.MAP_CHUNK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                WrappedLevelChunkData.ChunkData chunkData = packet.getLevelChunkData().read(0);
                byte[] buffer = chunkData.getBuffer();


                System.out.println(buffer.length + " " + buffer.length/16 + " " + buffer.length/16/16);
                System.out.println(Arrays.toString(buffer));

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < buffer.length/(16*16); y++) {
                        for (int z = 0; z < 16; z++) {
                            int index = getIndex(x, y, z);
                            byte blockId = buffer[index];
                            if (blockId == 17) {
                                buffer[index] = (byte) 18;
                            }
                        }
                    }
                }
                chunkData.setBuffer(buffer);
            }

            public int getIndex(int x, int y, int z) {
                return y + (z * 16) + (x * 16 * 16);
            }
        });*/

    }
}



















