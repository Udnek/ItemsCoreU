package me.udnek.itemscoreu.nms;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.udnek.itemscoreu.customenchantment.NmsEnchantmentContainer;
import me.udnek.itemscoreu.nms.loot.entry.NmsCustomLootEntryBuilder;
import me.udnek.itemscoreu.nms.loot.table.NmsDefaultLootTableContainer;
import me.udnek.itemscoreu.nms.loot.table.NmsLootTableContainer;
import me.udnek.itemscoreu.util.LogUtils;
import me.udnek.itemscoreu.util.Reflex;
import net.kyori.adventure.key.Key;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.GameTestAddMarkerDebugPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_21_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R2.CraftLootTable;
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.craftbukkit.v1_21_R2.entity.*;
import org.bukkit.craftbukkit.v1_21_R2.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R2.map.CraftMapCursor;
import org.bukkit.craftbukkit.v1_21_R2.util.CraftLocation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.map.MapCursor;
import org.bukkit.util.StructureSearchResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Nms {

    private LootContext genericLootContext;

    private static Nms instance;
    public static Nms get(){
        if (instance == null) instance = new Nms();
        return instance;
    }

    public @NotNull LootContext getGenericLootContext(){
        if (genericLootContext == null){
            ServerLevel serverLevel = NmsUtils.toNmsWorld(Bukkit.getWorld("world"));

            LootParams.Builder paramsBuilder = new LootParams.Builder(serverLevel);
            ContextKeySet.Builder keyBuilder = new ContextKeySet.Builder();

            keyBuilder.required(LootContextParams.ORIGIN);
            paramsBuilder.withParameter(LootContextParams.ORIGIN, new Vec3(0, 0, 0));
            paramsBuilder.withLuck(1);

            LootContext.Builder contextBuilder = new LootContext.Builder(paramsBuilder.create(keyBuilder.build()));
            genericLootContext = contextBuilder.create(Optional.empty());
        }
        return genericLootContext;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ITEMS
    ///////////////////////////////////////////////////////////////////////////

    public float getCompostableChance(@NotNull Material material){
        return ComposterBlock.COMPOSTABLES.getOrDefault(NmsUtils.toNmsMaterial(material), 0);
    }

    public @Nullable ConsumableComponent getConsumableComponent(@NotNull ItemStack bukkitItem){
        Consumable consumable = NmsUtils.toNmsItemStack(bukkitItem).getComponents().get(DataComponents.CONSUMABLE);
        if (consumable == null) return null;
        return new ConsumableComponent(consumable);
    }
    public @Nullable ConsumableComponent getConsumableComponent(@NotNull Material material){
        Consumable consumable = NmsUtils.toNmsMaterial(material).components().get(DataComponents.CONSUMABLE);
        if (consumable == null) return null;
        return new ConsumableComponent(consumable);
    }
    public @NotNull ItemStack setConsumableComponent(@NotNull ItemStack bukkitStack, @Nullable ConsumableComponent component){
        net.minecraft.world.item.ItemStack itemStack = NmsUtils.toNmsItemStack(bukkitStack);
        if (component == null) itemStack.set(DataComponents.CONSUMABLE, null);
        else itemStack.set(DataComponents.CONSUMABLE, component.getNms());
        return NmsUtils.toBukkitItemStack(itemStack);
    }

    ///////////////////////////////////////////////////////////////////////////
    // USAGES
    ///////////////////////////////////////////////////////////////////////////

    public @NotNull ItemStack getSpawnEggByType(@NotNull EntityType type){
        net.minecraft.world.entity.EntityType<?> aClass = CraftEntityType.bukkitToMinecraft(type);
        return CraftItemStack.asNewCraftStack(SpawnEggItem.byId(aClass));
    }

    ///////////////////////////////////////////////////////////////////////////
    // ENCHANTMENT
    ///////////////////////////////////////////////////////////////////////////

    public @NotNull NmsEnchantmentContainer getEnchantment(@NotNull Enchantment bukkitEnchantment){
        Registry<net.minecraft.world.item.enchantment.Enchantment> registry = NmsUtils.getRegistry(Registries.ENCHANTMENT);
        net.minecraft.world.item.enchantment.Enchantment enchantment = registry.getValue(NmsUtils.getResourceLocation(bukkitEnchantment.getKey()));
        return new NmsEnchantmentContainer(enchantment);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ENCHANTMENT
    ///////////////////////////////////////////////////////////////////////////

    public @NotNull Holder<MobEffect> registerEffect(@NotNull MobEffect effect, @NotNull Key key){

        Reflex.setFieldValue(BuiltInRegistries.MOB_EFFECT, "frozen", false);

        Registry<MobEffect> registry = NmsUtils.getRegistry(Registries.MOB_EFFECT);

        Class<?> tagSetClass;
        try {tagSetClass = Class.forName("net.minecraft.core.MappedRegistry$TagSet");
        } catch (ClassNotFoundException e) {throw new RuntimeException(e);}
        Method unboundMethod = Reflex.getMethod(tagSetClass, "unbound");
        Object tags = Reflex.invokeMethod(null, unboundMethod);
        Reflex.setFieldValue(registry, "allTags", tags);

        Holder.Reference<MobEffect> holder = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, NmsUtils.getResourceLocation(key), effect);
        BuiltInRegistries.MOB_EFFECT.freeze();
        return holder;
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOOT
    ///////////////////////////////////////////////////////////////////////////
    public List<MerchantRecipe> getAllPossibleTrades(){
        List<MerchantRecipe> recipes = new ArrayList<>();

        Entity entity = ((CraftEntity) Bukkit.getOnlinePlayers().toArray()[0]).getHandle();
        for (Map.Entry<VillagerProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>> proffesionEntry : VillagerTrades.TRADES.entrySet()) {
            Int2ObjectMap<VillagerTrades.ItemListing[]> tradesMap = proffesionEntry.getValue();
            for (VillagerTrades.ItemListing[] itemListings : tradesMap.values()) {
                for (VillagerTrades.ItemListing itemListing : itemListings) {
                    MerchantOffer merchantOffer = itemListing.getOffer(entity, null);
                    if (merchantOffer == null) continue;
                    MerchantRecipe merchantRecipe = new MerchantRecipe(
                            NmsUtils.toBukkitItemStack(merchantOffer.getResult()),
                            merchantOffer.getMaxUses()
                    );
                    List<ItemStack> ingredients = new ArrayList<>();
                    ingredients.add(NmsUtils.toBukkitItemStack(merchantOffer.getCostA()));
                    ingredients.add(NmsUtils.toBukkitItemStack(merchantOffer.getCostB()));
                    merchantRecipe.setIngredients(ingredients);

                    recipes.add(merchantRecipe);
                }
            }
        }
        return recipes;
    }


    public @NotNull NmsLootTableContainer getLootTableContainer(org.bukkit.loot.LootTable lootTable){
        return new NmsDefaultLootTableContainer(NmsUtils.toNmsLootTable(lootTable));
    }

    public void removeAllEntriesContains(@NotNull org.bukkit.loot.LootTable bukkitLootTable, @NotNull Predicate<ItemStack> predicate){
        replaceAllEntriesContains(bukkitLootTable, predicate, null);
    }
    public void replaceAllEntriesContains(@NotNull org.bukkit.loot.LootTable bukkitLootTable, @NotNull Predicate<ItemStack> predicate, @Nullable NmsCustomLootEntryBuilder newEntry){
        LootTable lootTable = NmsUtils.toNmsLootTable(bukkitLootTable);

        List<LootPoolEntryContainer> toReplace = new ArrayList<>();
        for (LootPoolSingletonContainer container : NmsUtils.getAllSingletonContainers(lootTable)) {
            LootPoolEntry entry = NmsUtils.getEntry(container);
            AtomicBoolean contains = new AtomicBoolean(false);
            NmsUtils.getPossibleLoot(entry, itemStack -> {
                if (predicate.test(NmsUtils.toBukkitItemStack(itemStack))) {
                    contains.set(true);
                }
            });
            if (contains.get()) toReplace.add(container);

        }
        for (LootPool pool : NmsUtils.getPools(lootTable)) {
            List<LootPoolEntryContainer> newContainers = new ArrayList<>();
            boolean changed = false;
            for (LootPoolEntryContainer container : NmsUtils.getEntries(pool)) {
                if (toReplace.contains(container)){
                    changed = true;
                    if (newEntry != null) newContainers.add(newEntry.build());
                } else { newContainers.add(container); }

            }
            if (changed){
                LogUtils.pluginLog("Changed loot entry container from: " + lootTable.craftLootTable.getKey().toString());
                Reflex.setFieldValue(pool, "entries", newContainers);
            }
        }
    }
    public @Nullable org.apache.commons.lang3.tuple.Pair<Integer, Integer> getWeightAndQuality(@NotNull org.bukkit.loot.LootTable bukkitLootTable, @NotNull Predicate<ItemStack> predicate){
        LootTable lootTable = NmsUtils.toNmsLootTable(bukkitLootTable);

        final Integer[] result = {null, null};
        AtomicBoolean found = new AtomicBoolean(false);
        for (LootPoolSingletonContainer container : NmsUtils.getAllSingletonContainers(lootTable)) {
            LootPoolEntry entry = NmsUtils.getEntry(container);
            NmsUtils.getPossibleLoot(entry, itemStack -> {
                if (found.get()) return;
                if (predicate.test(NmsUtils.toBukkitItemStack(itemStack))) {
                    int weight = (int) Reflex.getFieldValue(container, "weight");
                    int quality = (int) Reflex.getFieldValue(container, "quality");
                    result[0] = weight;
                    result[1] = quality;
                    found.set(true);
                }
            });

            if (found.get()) return org.apache.commons.lang3.tuple.Pair.of(result[0], result[1]);
        }
        return null;
    }

    public List<String> getRegisteredLootTableIds(){
        List<String> ids = new ArrayList<>();
        ReloadableServerRegistries.Holder registries = ((CraftServer) Bukkit.getServer()).getServer().reloadableRegistries();
        Collection<ResourceLocation> keys = registries.getKeys(Registries.LOOT_TABLE);
        for (ResourceLocation key : keys) {
            ids.add(key.toString());
        }
        return ids;
    }
    public List<org.bukkit.loot.LootTable> getRegisteredLootTables(){
        List<org.bukkit.loot.LootTable> lootTables = new ArrayList<>();
        ReloadableServerRegistries.Holder registries = ((CraftServer) Bukkit.getServer()).getServer().reloadableRegistries();
        Collection<ResourceLocation> keys = registries.getKeys(Registries.LOOT_TABLE);
        for (ResourceLocation key : keys) {
            lootTables.add(registries.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(key.toString()))).craftLootTable);
        }
        return lootTables;
    }
    public @NotNull org.bukkit.loot.LootTable getLootTable(String id){
        ResourceLocation resourceLocation = ResourceLocation.parse(id);
        ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, resourceLocation);
        return NmsUtils.getLootTable(key).craftLootTable;
    }

    public List<ItemStack> getPossibleLoot(org.bukkit.loot.LootTable lootTable) {
        List<ItemStack> result = new ArrayList<>();
        LootTable nmsLootTable = ((CraftLootTable) lootTable).getHandle();
        NmsUtils.getPossibleLoot(nmsLootTable, itemStack -> result.add(NmsUtils.toBukkitItemStack(itemStack)));
        return result;
    }
    public @Nullable org.bukkit.loot.LootTable getDeathLootTable(@NotNull org.bukkit.entity.LivingEntity bukkitEntity){
        LivingEntity entity = ((CraftLivingEntity) bukkitEntity).getHandle();
        Optional<ResourceKey<LootTable>> lootTable = entity.getLootTable();
        if (lootTable.isEmpty()) return null;
        return NmsUtils.getLootTable(lootTable.get()).craftLootTable;
    }

    public void setDeathLootTable(@NotNull org.bukkit.entity.Mob bukkitMob, @Nullable org.bukkit.loot.LootTable lootTable){
        Mob mob = ((CraftMob) bukkitMob).getHandle();
        if (lootTable == null){
            mob.lootTable = Optional.empty();
        } else {
            mob.lootTable = Optional.of(NmsUtils.getResourceKeyLootTable(lootTable.getKey().toString()));
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // STRUCTURE
    ///////////////////////////////////////////////////////////////////////////
    public ItemStack generateExplorerMap(Location location, org.bukkit.generator.structure.Structure bukkitStructure, int radius, boolean skipKnownStructures){
        return generateExplorerMap(location, bukkitStructure, radius, skipKnownStructures, MapCursor.Type.RED_X);
    }
    public ItemStack generateExplorerMap(Location location, org.bukkit.generator.structure.Structure bukkitStructure, int radius, boolean skipKnownStructures, MapCursor.Type type){
        ServerLevel serverLevel = NmsUtils.toNmsWorld(location.getWorld());

        CraftStructure craftStructure = (CraftStructure) bukkitStructure;
        if (craftStructure == null){
            LogUtils.pluginLog("structure not found in registry!");
            return null;
        }
        Structure structure = craftStructure.getHandle();
        HolderSet.Direct<Structure> holders = HolderSet.direct(Holder.direct(structure));

        Pair<BlockPos, Holder<Structure>> pair =
                serverLevel.getChunkSource().getGenerator().findNearestMapStructure(
                        serverLevel, holders, CraftLocation.toBlockPosition(location), radius, skipKnownStructures);
        if (pair == null){
            LogUtils.pluginLog("structure not found in world!");
            return null;
        }

        Holder<MapDecorationType> mapDecorationTypeHolder = CraftMapCursor.CraftType.bukkitToMinecraftHolder(type);

        BlockPos structureLocation = pair.getFirst();
        net.minecraft.world.item.ItemStack mapItem = MapItem.create(serverLevel, structureLocation.getX(), structureLocation.getZ(), (byte) 2, true, true);
        MapItem.renderBiomePreviewMap(serverLevel, mapItem);
        MapItemSavedData.addTargetDecoration(mapItem, structureLocation, "+", mapDecorationTypeHolder);

        return NmsUtils.toBukkitItemStack(mapItem);
    }
    public void generateBiomePreviewMap(World world, ItemStack itemStack){
        ServerLevel serverLevel =NmsUtils.toNmsWorld(world);
        MapItem.renderBiomePreviewMap(serverLevel, NmsUtils.toNmsItemStack(itemStack));
    }
    public boolean containStructure(Chunk bukitChunk, org.bukkit.generator.structure.Structure bukkitStructure){
        Structure targerStructure = ((CraftStructure) bukkitStructure).getHandle();
        ChunkAccess chunk = ((CraftChunk) bukitChunk).getHandle(ChunkStatus.FEATURES);
        return chunk.getReferencesForStructure(targerStructure) != null;
    }
    // TODO: 6/21/2024 REMOVE DEBUG 
    public boolean isInStructure(Location location, org.bukkit.generator.structure.Structure bukkitStructure, StructureStartSearchMethod method, int radius){

        long startTime = System.nanoTime();

        StructureStart startForStructure;

        if (method == StructureStartSearchMethod.LOCATE_STRUCTURE){
            StructureSearchResult searchResult = location.getWorld().locateNearestStructure(location, bukkitStructure, radius, false);
            if (searchResult == null){
                LogUtils.log("search result is null");
                return false;
            }
            Location searchLocation = searchResult.getLocation();

            Structure structure = ((CraftStructure) bukkitStructure).getHandle();

            ChunkAccess chunk = ((CraftChunk) searchLocation.getChunk()).getHandle(ChunkStatus.FULL);
            startForStructure = chunk.getStartForStructure(structure);
        }
        else {
            Structure structure = ((CraftStructure) bukkitStructure).getHandle();
            startForStructure = findStructureStartIn((NmsUtils.toNmsWorld(location.getWorld())), structure, location.getChunk().getX(), location.getChunk().getZ(), radius);
        }
        
        LogUtils.log("time taken:" + (System.nanoTime() - startTime));


        if (startForStructure == null){
            LogUtils.log("start is null");
            return false;
        }

        BlockPos position = CraftLocation.toBlockPosition(location);

        for (StructurePiece piece : startForStructure.getPieces()) {
            if (piece.getBoundingBox().isInside(position)) return true;
        }
        LogUtils.log("not in bounds");
        return false;
    }
    private StructureStart findStructureStartIn(ServerLevel world, Structure structure, int xCenter, int zCenter, int quadRadius){
        for (int x = xCenter-quadRadius; x <= xCenter+quadRadius; x++) {
            for (int z = zCenter-quadRadius; z <= zCenter+quadRadius; z++) {
                LevelChunk chunk = world.getChunk(x, z);
                StructureStart startForStructure = chunk.getStartForStructure(structure);
                if (startForStructure != null) return startForStructure;
            }
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////
    public void showDebugBlock(Player player, Location location, int color, String name, int time){
        Color rgb = Color.fromRGB(color);
        color = rgb.getBlue() | (rgb.getGreen() << 8) | (rgb.getRed() << 16) | (rgb.getAlpha() << 24);
        GameTestAddMarkerDebugPayload payload = new GameTestAddMarkerDebugPayload(CraftLocation.toBlockPosition(location), color, name, time * 1000/20);
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundCustomPayloadPacket(payload));
    }
    public void showDebugBlock(Player player, Location location, int color, int time){
        showDebugBlock(player, location, color, "", time);
    }
    ///////////////////////////////////////////////////////////////////////////
    // BIOME
    ///////////////////////////////////////////////////////////////////////////
    public DownfallType getDownfallType(Location location){
        BlockPos blockPosition = CraftLocation.toBlockPosition(location);
        Biome.Precipitation precipitation = NmsUtils.toNmsWorld(location.getWorld()).getBiome(blockPosition).value().getPrecipitationAt(blockPosition, location.getWorld().getSeaLevel());
        return DownfallType.fromNMS(precipitation);
    }


}































