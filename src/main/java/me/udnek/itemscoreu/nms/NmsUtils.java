package me.udnek.itemscoreu.nms;

import com.mojang.datafixers.util.Either;
import me.udnek.itemscoreu.nms.loot.util.NmsFields;
import me.udnek.itemscoreu.util.Reflex;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
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
import org.bukkit.craftbukkit.v1_21_R2.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_21_R2.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NmsUtils {

    // CHAT
    public static @NotNull Component toNmsComponent(@Nullable net.kyori.adventure.text.Component component){
        if (component == null) component = net.kyori.adventure.text.Component.empty();
        return CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(component));
    }

    // REGISTRY
    public static <T> Registry<T> getRegistry(@NotNull ResourceKey<Registry<T>> registry){
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        return server.registryAccess().lookup(registry).orElse(null);
    }
    public static ResourceLocation toNmsResourceLocation(@NotNull Key key){
        return ResourceLocation.parse(key.toString());
    }

    public static <T> Holder<T> toNms(@NotNull ResourceKey<Registry<T>> registry, @NotNull Keyed keyed){
        return toNms(getRegistry(registry), keyed);
    }
    public static <T> Holder<T> toNms(@NotNull Registry<T> registry, @NotNull Keyed keyed){
        return registry.get(toNmsResourceLocation(keyed.key())).get();
    }

    public static <T> void editRegistry(@NotNull Registry<T> registry, @NotNull Consumer<Registry<T>> consumer){
        Reflex.setFieldValue(registry, "frozen", false);
        Class<?> tagSetClass;
        try {tagSetClass = Class.forName("net.minecraft.core.MappedRegistry$TagSet");
        } catch (ClassNotFoundException e) {throw new RuntimeException(e);}
        Method unboundMethod = Reflex.getMethod(tagSetClass, "unbound");
        Object tags = Reflex.invokeMethod(null, unboundMethod);
        Reflex.setFieldValue(registry, "allTags", tags);
        consumer.accept(registry);
        registry.freeze();
    }

    public static <T> Holder<T> registerInRegistry(@NotNull Registry<T> registry, @NotNull T object, @NotNull Key key){
        final Holder<T>[] holder = new Holder[1];
        editRegistry(registry, new Consumer<>() {
            @Override
            public void accept(Registry<T> registry) {
                holder[0] = Registry.registerForHolder(registry, toNmsResourceLocation(key), object);
            }
        });
        return holder[0];
    }

    public static <T> @NotNull HolderSet<T> createHolderSet(@NotNull Registry<T> registry, @Nullable Iterable<? extends Keyed> keys){
        if (keys == null) return HolderSet.direct();
        List<Holder<T>> list = new ArrayList<>();
        for (Keyed key : keys) {
            list.add(toNms(registry, key));
        }
        return HolderSet.direct(list);
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

    public static @NotNull Holder<Enchantment> registerEnchantment(@NotNull Enchantment enchantment, @NotNull Key key){
        return registerInRegistry(getRegistry(Registries.ENCHANTMENT), enchantment, key);
    }

    ///////////////////////////////////////////////////////////////////////////
    // EFFECT
    ///////////////////////////////////////////////////////////////////////////

    public static @NotNull Holder<MobEffect> registerEffect(@NotNull MobEffect effect, @NotNull Key key){
        return registerInRegistry(BuiltInRegistries.MOB_EFFECT, effect, key);
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOOT
    ///////////////////////////////////////////////////////////////////////////
    public static LootTable toNmsLootTable(org.bukkit.loot.LootTable lootTable){
        return ((CraftLootTable) lootTable).getHandle();
    }
    public static List<LootPool> getPools(LootTable lootTable){
        return (List<LootPool>) Reflex.getFieldValue(lootTable, NmsFields.POOLS);
    }
    public static @NotNull LootPoolEntry getEntry(@NotNull LootPoolSingletonContainer container){
        return ((LootPoolEntry) Reflex.getFieldValue(container, "entry"));
    }
    public static List<LootPoolEntryContainer> getEntries(@NotNull LootPool lootPool){
        return (List<LootPoolEntryContainer>) Reflex.getFieldValue(lootPool, NmsFields.ENTRIES);
    }
    // containers
    public static @NotNull List<LootPoolSingletonContainer> getAllSingletonContainers(@NotNull LootTable lootTable){
        List<LootPoolSingletonContainer> containers = new ArrayList<>();
        NmsUtils.getAllSingletonContainers(lootTable, containers::add);
        return containers;
    }
    public static void getAllSingletonContainers(@NotNull LootPoolEntryContainer container, Consumer<LootPoolSingletonContainer> consumer){
        if (container instanceof NestedLootTable){
            LootTable lootTable;
            Either<ResourceKey<LootTable>, LootTable> either = (Either<ResourceKey<LootTable>, LootTable>) Reflex.getFieldValue(container, NmsFields.CONTENTS);
            if (either.left() != null && either.left().isPresent()){
                lootTable = getLootTable(either.left().get());
            } else lootTable = either.right().get();

            getAllSingletonContainers(lootTable, consumer);
        }
        else if (container instanceof LootPoolSingletonContainer singletonContainer) {
            consumer.accept(singletonContainer);
        } else {
            List<LootPoolEntryContainer> childrenContainers = (List<LootPoolEntryContainer>) Reflex.getFieldValue(container, NmsFields.CHILDREN);
            childrenContainers.forEach(container1 -> getAllSingletonContainers(container1, consumer));
        }
    }
    public static void getAllSingletonContainers(@NotNull LootPool lootPool, @NotNull Consumer<LootPoolSingletonContainer> consumer){
        List<LootPoolEntryContainer> containers = (List<LootPoolEntryContainer>) Reflex.getFieldValue(lootPool, NmsFields.ENTRIES);
        containers.forEach(container -> getAllSingletonContainers(container, consumer));
    }
    public static void getAllSingletonContainers(@NotNull LootTable lootTable, @NotNull Consumer<LootPoolSingletonContainer> consumer){
        List<LootPool> lootPools = (List<LootPool>) Reflex.getFieldValue(lootTable, NmsFields.POOLS);
        lootPools.forEach(lootPool -> getAllSingletonContainers(lootPool, consumer));
    }
    // possible loot
    public static void getPossibleLoot(@NotNull LootTable lootTable, @NotNull Consumer<net.minecraft.world.item.ItemStack> consumer){
        getAllSingletonContainers(lootTable, container -> getPossibleLoot(container, consumer));
    }
    public static void getPossibleLoot(@NotNull LootPoolSingletonContainer container, @NotNull Consumer<net.minecraft.world.item.ItemStack> consumer){
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
    public static void getPossibleLoot(@NotNull LootPoolEntry entry, @NotNull Consumer<net.minecraft.world.item.ItemStack> consumer){
        entry.createItemStack(new Consumer<>() {
            @Override
            public void accept(net.minecraft.world.item.ItemStack itemStack) {
                if (itemStack.getCount() <= 0) itemStack.setCount(1);
                consumer.accept(itemStack);
            }
        }, Nms.get().getGenericLootContext());
    }
    // misc
    public static @Nullable LootPool getLootPoolByPredicate(@NotNull LootTable lootTable, @NotNull Predicate<net.minecraft.world.item.ItemStack> predicate){
        List<LootPool> pools = getPools(lootTable);
        AtomicBoolean found = new AtomicBoolean(false);
        for (LootPool pool : pools) {
            found.set(false);
            List<LootPoolSingletonContainer> containers = new ArrayList<>();
            getAllSingletonContainers(pool, containers::add);
            containers.forEach(container -> getPossibleLoot(container, itemStack -> {
                if (predicate.test(itemStack)) found.set(true);
            }));
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














