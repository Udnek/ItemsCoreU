package me.udnek.itemscoreu.nms;

import com.mojang.datafixers.util.Either;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R1.CraftLootTable;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NmsUtils {

    // ITEM
    public static net.minecraft.world.item.ItemStack toNmsItemStack(ItemStack itemStack){
        return CraftItemStack.asNMSCopy(itemStack);
    }
    public static Item toNmsMaterial(Material material){
        return CraftMagicNumbers.getItem(material);
    }
    public static ItemStack toBukkitItemStack(net.minecraft.world.item.ItemStack itemStack){
        return CraftItemStack.asBukkitCopy(itemStack);
    }
    // ENTITY
    public static Entity toNmsEntity(org.bukkit.entity.Entity entity){
        return ((CraftEntity) entity).getHandle();
    }
    public static LivingEntity toNmsEntity(org.bukkit.entity.LivingEntity entity){
        return ((CraftLivingEntity) entity).getHandle();
    }
    // WORLD
    public static ServerLevel toNmsWorld(World world){
        return ((CraftWorld) world).getHandle();
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOOT
    ///////////////////////////////////////////////////////////////////////////
    public static LootTable toNmsLootTable(org.bukkit.loot.LootTable lootTable){
        return ((CraftLootTable) lootTable).getHandle();
    }
    public static List<LootPool> getPools(LootTable lootTable){
        return (List<LootPool>) Reflex.getFieldValue(lootTable, "pools");
    }
    public static List<LootPoolEntry> getAllEntries(List<LootPoolSingletonContainer> containers){
        List<LootPoolEntry> entries = new ArrayList<>();
        for (LootPoolSingletonContainer container : containers) {
            entries.add(getEntry(container));
        }
        return entries;
    }
    public static LootPoolEntry getEntry(LootPoolSingletonContainer container){
        return ((LootPoolEntry) Reflex.getFieldValue(container, "entry"));
    }
    public static List<LootPoolEntryContainer> getContainers(LootPool lootPool){
        return (List<LootPoolEntryContainer>) Reflex.getFieldValue(lootPool, "entries");
    }
    public static List<LootPoolSingletonContainer> getAllSingletonContainers(List<LootPoolEntryContainer> containers){
        List<LootPoolSingletonContainer> result = new ArrayList<>();
        for (LootPoolEntryContainer container : containers) {
            result.addAll(getAllSingletonContainers(container));
        }
        return result;
    }
    public static List<LootPoolSingletonContainer> getAllSingletonContainers(LootPoolEntryContainer container){
        if (container instanceof NestedLootTable){
            LootTable lootTable;
            Either<ResourceKey<LootTable>, LootTable> either = (Either<ResourceKey<LootTable>, LootTable>) Reflex.getFieldValue(container, "contents");
            if (either.left() != null && either.left().isPresent()){
                lootTable = getLootTable(either.left().get());
            } else lootTable = either.right().get();

            return getAllSingletonContainers(lootTable);
        }
        else if (container instanceof LootPoolSingletonContainer singletonContainer) {
            return List.of(singletonContainer);
        } else {
            List<LootPoolEntryContainer> childrenContainers = (List<LootPoolEntryContainer>) Reflex.getFieldValue(container, "children");
            return getAllSingletonContainers(childrenContainers);
        }
    }
    public static List<LootPoolSingletonContainer> getAllSingletonContainers(LootTable lootTable){
        List<LootPool> lootPools = (List<LootPool>) Reflex.getFieldValue(lootTable, "pools");
        List<LootPoolSingletonContainer> singletonContainers = new ArrayList<>();
        for (LootPool lootPool : lootPools) {
            List<LootPoolEntryContainer> containers = (List<LootPoolEntryContainer>) Reflex.getFieldValue(lootPool, "entries");
            singletonContainers.addAll(getAllSingletonContainers(containers));
        }
        return singletonContainers;
    }
    public static void getPossibleLoot(LootPoolSingletonContainer container, Consumer<net.minecraft.world.item.ItemStack> consumer){
        getPossibleLoot(getEntry(container), consumer);
    }
    public static void getPossibleLoot(LootPoolEntry entry, Consumer<net.minecraft.world.item.ItemStack> consumer){
        ItemConsumer localConsumer = new ItemConsumer();
        try {entry.createItemStack(localConsumer, Nms.GENERIC_LOOT_CONTEXT);
        } catch (Exception ignored) {}

        for (net.minecraft.world.item.ItemStack itemStack : localConsumer.get()) {
            if (itemStack.getCount() == 0) itemStack.setCount(1);
            consumer.accept(itemStack);
        }
    }
    public static @Nullable LootPoolSingletonContainer getSingletonContainerByItems(LootTable lootTable, Predicate<net.minecraft.world.item.ItemStack> predicate){
        List<LootPoolSingletonContainer> containers = getAllSingletonContainers(lootTable);
        AtomicBoolean found = new AtomicBoolean(false);
        for (LootPoolSingletonContainer container : containers) {
            getPossibleLoot(container, new Consumer<net.minecraft.world.item.ItemStack>() {
                @Override
                public void accept(net.minecraft.world.item.ItemStack itemStack) {
                    if (predicate.test(itemStack)){
                        found.set(true);
                    }
                }
            });
            if (found.get()){
                return container;
            }
        }
        return null;
    }
    public static @NotNull ResourceKey<net.minecraft.world.level.storage.loot.LootTable> getResourceKeyLootTable(String id){
        ResourceLocation resourceLocation = ResourceLocation.parse(id);
        return ResourceKey.create(Registries.LOOT_TABLE, resourceLocation);
    }
    public static @NotNull LootTable getLootTable(@NotNull ResourceKey<net.minecraft.world.level.storage.loot.LootTable> resourceKey){
        ReloadableServerRegistries.Holder registries = ((CraftServer) Bukkit.getServer()).getServer().reloadableRegistries();
        return registries.getLootTable(resourceKey);
    }
}














