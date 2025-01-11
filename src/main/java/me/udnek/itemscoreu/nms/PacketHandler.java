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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PacketHandler {
    
    public static void initialize(){
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                ItemsCoreU.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_EFFECT, PacketType.Play.Server.REMOVE_ENTITY_EFFECT)
        {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Holder.Reference<MobEffect> holder = (Holder.Reference<MobEffect>) packet.getModifier().read(1);
                ResourceLocation id = holder.key().location();
                if (id.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) return;
                CustomEffect customEffect = CustomRegistries.EFFECT.get(id.toString());
                if (customEffect instanceof ConstructableCustomEffect disguiseable){
                    PotionEffectType vanillaDisguise = disguiseable.getVanillaDisguise();
                    if (vanillaDisguise == null) event.setCancelled(true);
                    else packet.getModifier().write(1, NmsUtils.toNms(Registries.MOB_EFFECT, vanillaDisguise));
                }
            }
        });

/*        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                ItemsCoreU.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Server.WINDOW_ITEMS)
        {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                List<ItemStack> stacks = packet.getItemListModifier().read(0);
                List<Integer> modifyIndexes = new ArrayList<>();
                for (int i = 0; i < stacks.size(); i++) {
                    ItemStack itemStack = stacks.get(i);
                    net.minecraft.world.item.ItemStack nmsStack = NmsUtils.toNmsItemStack(itemStack);
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(nmsStack.getItem());
                    if (!id.getNamespace().equals("minecraft")) modifyIndexes.add(i);
                }
                for (Integer i : modifyIndexes) {
                    stacks.set(i, new ItemStack(Material.MACE));
                }
            }
        });

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                ItemsCoreU.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Server.SET_SLOT)
        {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                packet.getItemArrayModifier()
            }
        });*/
    }
}
