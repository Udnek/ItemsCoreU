package me.udnek.itemscoreu.nms;

import com.mojang.datafixers.util.Either;
import me.udnek.itemscoreu.nms.loot.util.NmsFields;
import me.udnek.itemscoreu.util.Reflex;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_21_R2.CraftLootTable;
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R2.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NmsUtils {

    // REGISTRY
    public static <T> Registry<T> getRegistry(@NotNull ResourceKey<Registry<T>> registry){
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        return server.registryAccess().lookup(registry).orElse(null);
    }
    public static ResourceLocation getResourceLocation(@NotNull Key key){
        return ResourceLocation.parse(key.toString());
    }

    public static <T> Holder<T> toNms(@NotNull ResourceKey<Registry<T>> registry, @NotNull Keyed keyed){
        return getRegistry(registry).get(getResourceLocation(keyed.key())).get();
    }

    // ITEM
    public static net.minecraft.world.item.ItemStack toNmsItemStack(@NotNull ItemStack itemStack){
        return CraftItemStack.asNMSCopy(itemStack);
    }
    public static Item toNmsMaterial(@NotNull Material material){
        return CraftMagicNumbers.getItem(material);
    }
    public static Block toNmsBlock(@NotNull Material material){
        return CraftMagicNumbers.getBlock(material);
    }
    public static ItemStack toBukkitItemStack(@NotNull net.minecraft.world.item.ItemStack itemStack){
        return CraftItemStack.asBukkitCopy(itemStack);
    }
    // ENTITY
    public static Entity toNmsEntity(@NotNull org.bukkit.entity.Entity entity){
        return ((CraftEntity) entity).getHandle();
    }
    public static LivingEntity toNmsEntity(@NotNull org.bukkit.entity.LivingEntity entity){
        return ((CraftLivingEntity) entity).getHandle();
    }
    // WORLD
    public static ServerLevel toNmsWorld(@NotNull World world){
        return ((CraftWorld) world).getHandle();
    }

    // Attribute
    public static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation toNmsOperation(@NotNull AttributeModifier.Operation bukkit){
        return switch (bukkit){
            case ADD_NUMBER -> net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
            case ADD_SCALAR -> net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
            case MULTIPLY_SCALAR_1 -> net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
        };
    }


    ///////////////////////////////////////////////////////////////////////////
    // ENCHANTMENT
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // LOOT
    ///////////////////////////////////////////////////////////////////////////
    public static LootTable toNmsLootTable(org.bukkit.loot.LootTable lootTable){
        return ((CraftLootTable) lootTable).getHandle();
    }
    public static List<LootPool> getPools(LootTable lootTable){
        return (List<LootPool>) Reflex.getFieldValue(lootTable, NmsFields.POOLS);
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
    public static List<LootPoolEntryContainer> getEntries(LootPool lootPool){
        return (List<LootPoolEntryContainer>) Reflex.getFieldValue(lootPool, NmsFields.ENTRIES);
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
            Either<ResourceKey<LootTable>, LootTable> either = (Either<ResourceKey<LootTable>, LootTable>) Reflex.getFieldValue(container, NmsFields.CONTENTS);
            if (either.left() != null && either.left().isPresent()){
                lootTable = getLootTable(either.left().get());
            } else lootTable = either.right().get();

            return getAllSingletonContainers(lootTable);
        }
        else if (container instanceof LootPoolSingletonContainer singletonContainer) {
            return List.of(singletonContainer);
        } else {
            List<LootPoolEntryContainer> childrenContainers = (List<LootPoolEntryContainer>) Reflex.getFieldValue(container, NmsFields.CHILDREN);
            return getAllSingletonContainers(childrenContainers);
        }
    }
    public static List<LootPoolSingletonContainer> getAllSingletonContainers(LootPool lootPool){
        List<LootPoolSingletonContainer> singletonContainers = new ArrayList<>();
        List<LootPoolEntryContainer> containers = (List<LootPoolEntryContainer>) Reflex.getFieldValue(lootPool, NmsFields.ENTRIES);
        singletonContainers.addAll(getAllSingletonContainers(containers));
        return singletonContainers;
    }

    public static List<LootPoolSingletonContainer> getAllSingletonContainers(LootTable lootTable){
        List<LootPool> lootPools = (List<LootPool>) Reflex.getFieldValue(lootTable, NmsFields.POOLS);
        List<LootPoolSingletonContainer> singletonContainers = new ArrayList<>();
        for (LootPool lootPool : lootPools) {
            singletonContainers.addAll(getAllSingletonContainers(lootPool));
        }
        return singletonContainers;
    }
    public static void getPossibleLoot(LootTable lootTable, Consumer<net.minecraft.world.item.ItemStack> consumer){
        getPossibleLoot(getAllSingletonContainers(lootTable), consumer);
    }

    public static void getPossibleLoot(List<LootPoolSingletonContainer> containers, Consumer<net.minecraft.world.item.ItemStack> consumer){
        containers.forEach(container -> getPossibleLoot(container, consumer));
    }
    public static void getPossibleLoot(LootPoolSingletonContainer container, Consumer<net.minecraft.world.item.ItemStack> consumer){
        if (container instanceof LootItem){
            Item item = ((Holder<Item>) Reflex.getFieldValue(container, "item")).value();
            if (item == Items.MAP){
                List<LootItemFunction> functions = (List<LootItemFunction>) Reflex.getFieldValue(container, "functions");
                for (LootItemFunction function : functions) {
                    if (!(function instanceof ExplorationMapFunction)) continue;
                    consumer.accept(new net.minecraft.world.item.ItemStack(Items.FILLED_MAP));
                    return;
                }
            }
        }
        getPossibleLoot(getEntry(container), consumer);
    }
    public static void getPossibleLoot(LootPoolEntry entry, Consumer<net.minecraft.world.item.ItemStack> consumer){
        NmsItemConsumer localConsumer = new NmsItemConsumer();
        entry.createItemStack(consumer, Nms.get().getGenericLootContext());

        for (net.minecraft.world.item.ItemStack itemStack : localConsumer.get()) {
            if (itemStack.getCount() == 0) itemStack.setCount(1);
            consumer.accept(itemStack);
        }
    }

    public static @Nullable LootPool getLootPoolByPredicate(LootTable lootTable, Predicate<net.minecraft.world.item.ItemStack> predicate){
        List<LootPool> pools = (List<LootPool>) Reflex.getFieldValue(lootTable, NmsFields.POOLS);
        for (LootPool pool : pools) {
            AtomicBoolean found = new AtomicBoolean(false);
            getPossibleLoot(getAllSingletonContainers(pool), itemStack -> {
                if (predicate.test(itemStack)) found.set(true);
            });
            if (found.get()) return pool;
        }
        return null;
    }

    public static @Nullable LootPoolSingletonContainer getSingletonContainerByPredicate(LootTable lootTable, Predicate<net.minecraft.world.item.ItemStack> predicate){
        List<LootPoolSingletonContainer> containers = getAllSingletonContainers(lootTable);
        AtomicBoolean found = new AtomicBoolean(false);
        for (LootPoolSingletonContainer container : containers) {
            getPossibleLoot(container, itemStack -> {
                if (predicate.test(itemStack)) found.set(true);
            });
            if (found.get()) return container;
        }
        return null;
    }
    public static @NotNull ResourceKey<LootTable> getResourceKeyLootTable(String id){
        ResourceLocation resourceLocation = ResourceLocation.parse(id);
        return ResourceKey.create(Registries.LOOT_TABLE, resourceLocation);
    }
    public static @NotNull LootTable getLootTable(@NotNull ResourceKey<LootTable> resourceKey){
        ReloadableServerRegistries.Holder registries = ((CraftServer) Bukkit.getServer()).getServer().reloadableRegistries();
        return registries.getLootTable(resourceKey);
    }
}














