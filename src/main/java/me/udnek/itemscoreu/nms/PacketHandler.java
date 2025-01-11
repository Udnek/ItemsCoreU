package me.udnek.itemscoreu.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BundleContents;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customeffect.ConstructableCustomEffect;
import me.udnek.itemscoreu.customeffect.CustomEffect;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    }
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
                    stacks.set(i, fix(itemStack));
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
                ItemStack itemStack = packet.getItemModifier().read(0);
                packet.getItemModifier().write(0, fix(itemStack));
            }
        });


        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                ItemsCoreU.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_METADATA)
        {
            @Override
            public void onPacketSending(PacketEvent event) {
*//*                PacketContainer packet = event.getPacket();
                System.out.println(packet.getItemModifier().size());
                packet.getItemModifier().read(4);*//*
            }
        });
    }








    public static boolean isCustomId(@NotNull ItemStack itemStack){
        net.minecraft.world.item.ItemStack nmsStack = NmsUtils.toNmsItemStack(itemStack);
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(nmsStack.getItem());
        return !id.getNamespace().equals("minecraft");
    }

    public static @NotNull List<ItemStack> fix(@NotNull List<ItemStack> brokens){
        List<ItemStack> fixeds = new ArrayList<>();
        for (ItemStack broken : brokens) {
            fixeds.add(fix(broken));
        }
        return fixeds;
    }
    
    public static @NotNull ItemStack fix(@NotNull ItemStack broken){
        if (isCustomId(broken)){
            broken = broken.withType(Material.MACE);
        }
        BundleContents bundleContents = broken.getData(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null || bundleContents.contents().isEmpty()) return broken;
        BundleContents newBundle = BundleContents.bundleContents(fix(bundleContents.contents()));
        broken.setData(DataComponentTypes.BUNDLE_CONTENTS, newBundle);
        return broken;
    }*/
}
