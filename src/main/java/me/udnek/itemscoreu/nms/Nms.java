package me.udnek.itemscoreu.nms;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.GameTestAddMarkerDebugPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
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
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_21_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R1.CraftLootTable;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.entity.*;
import org.bukkit.craftbukkit.v1_21_R1.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R1.map.CraftMapCursor;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftLocation;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.map.MapCursor;
import org.bukkit.util.StructureSearchResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Nms {

    private static final LootContext GENERIC_LOOT_CONTEXT;

    static {
        ServerLevel serverLevel = NmsUtils.toNmsWorld(Bukkit.getWorld("world"));
        LootParams.Builder paramsBuilder = new LootParams.Builder(serverLevel);
        LootContextParamSet contextParamSet = new LootContextParamSet.Builder().build();
        paramsBuilder.withLuck(1);
        LootContext.Builder contextBuilder = new LootContext.Builder(paramsBuilder.create(contextParamSet));
        GENERIC_LOOT_CONTEXT = contextBuilder.create(Optional.empty());
    }

    private static Nms instance;
    public static Nms get(){
        if (instance == null) instance = new Nms();
        return instance;
    }

    ///////////////////////////////////////////////////////////////////////////
    // USAGES
    ///////////////////////////////////////////////////////////////////////////

    public float getCompostableChance(Material material){
        return ComposterBlock.COMPOSTABLES.getOrDefault(NmsUtils.toNmsMaterial(material), 0);
    }

    public int getFuelTime(Material material){
        return AbstractFurnaceBlockEntity.getFuel().getOrDefault(NmsUtils.toNmsMaterial(material), 0);
    }

    public ItemStack getSpawnEggByType(EntityType type){
        net.minecraft.world.entity.EntityType<?> aClass = CraftEntityType.bukkitToMinecraft(type);
        return CraftItemStack.asNewCraftStack(SpawnEggItem.byId(aClass));
    }

    ///////////////////////////////////////////////////////////////////////////
    // BREWING
    ///////////////////////////////////////////////////////////////////////////
    // TODO: 6/1/2024 FIX BREWING
/*    public boolean isBrewableIngredient(Material material){
        return PotionBrewing.isIngredient(getNMSItemStack(material));
    }
    public boolean isBrewablePotion(ItemStack itemStack){
        net.minecraft.world.item.ItemStack nmsItemStack = getNMSItemStack(itemStack);
        Potion potion = PotionUtils.getPotion(nmsItemStack);
        return PotionBrewing.isBrewablePotion(potion);
    }
    public boolean hasBrewingMix(ItemStack potion, Material ingredient){
        return PotionBrewing.hasMix(getNMSItemStack(potion), getNMSItemStack(ingredient));
    }
    public ItemStack getBrewingMix(ItemStack potion, Material ingredient){
        net.minecraft.world.item.ItemStack mixed = PotionBrewing.mix(getNMSItemStack(potion), getNMSItemStack(ingredient));
        return getNormalItemStack(mixed);
    }*/
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

    public void injectLootPoolListeners(){
        for (org.bukkit.loot.LootTable bukkitLootTable : getRegisteredLootTables()) {
            LootTable lootTable = ((CraftLootTable) bukkitLootTable).getHandle();

            for (LootPoolSingletonContainer singletonContainer : getAllSingletonContainers(lootTable)) {
                Reflex.setFieldValue(singletonContainer, "entry", new CustomNmsLootPoolEntry(lootTable, singletonContainer));
            }
        }
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
        ResourceKey<net.minecraft.world.level.storage.loot.LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, resourceLocation);
        return getLootTable(key).craftLootTable;
    }
    private @NotNull ResourceKey<net.minecraft.world.level.storage.loot.LootTable> getResourceKeyLootTable(String id){
        ResourceLocation resourceLocation = ResourceLocation.parse(id);
        return ResourceKey.create(Registries.LOOT_TABLE, resourceLocation);
    }
    private @NotNull LootTable getLootTable(@NotNull ResourceKey<net.minecraft.world.level.storage.loot.LootTable> resourceKey){
        ReloadableServerRegistries.Holder registries = ((CraftServer) Bukkit.getServer()).getServer().reloadableRegistries();
        return registries.getLootTable(resourceKey);
    }

    public List<ItemStack> getPossibleLoot(org.bukkit.loot.LootTable lootTable) {
        List<ItemStack> result = new ArrayList<>();
        net.minecraft.world.level.storage.loot.LootTable nmsLootTable = ((CraftLootTable) lootTable).getHandle();
        List<LootPoolEntry> entries = getAllEntries(getAllSingletonContainers(nmsLootTable));

        ItemConsumer itemConsumer = new ItemConsumer();
        for (LootPoolEntry entry : entries) {
            entry.createItemStack(itemConsumer, GENERIC_LOOT_CONTEXT);
            for (net.minecraft.world.item.ItemStack itemStack : itemConsumer.get()) {
                if (itemStack.getCount() == 0) itemStack.setCount(1);
                result.add(NmsUtils.toBukkitItemStack(itemStack));
            }
            itemConsumer.clear();
        }
        return result;
    }
    private List<LootPoolSingletonContainer> getAllSingletonContainers(LootTable lootTable){
        List<LootPool> lootPools = (List<LootPool>) Reflex.getFieldValue(lootTable, "pools");

        List<LootPoolSingletonContainer> singletonContainers = new ArrayList<>();
        for (LootPool lootPool : lootPools) {
            List<LootPoolEntryContainer> containers = (List<LootPoolEntryContainer>) Reflex.getFieldValue(lootPool, "entries");
            singletonContainers.addAll(getAllSingletonContainers(containers));
        }
        return singletonContainers;
    }

    private List<LootPoolSingletonContainer> getAllSingletonContainers(List<LootPoolEntryContainer> containers){
        List<LootPoolSingletonContainer> result = new ArrayList<>();
        for (LootPoolEntryContainer container : containers) {
            if (container instanceof NestedLootTable){
                LootTable lootTable;
                Either<ResourceKey<LootTable>, LootTable> either = (Either<ResourceKey<LootTable>, LootTable>) Reflex.getFieldValue(container, "contents");
                if (either.left() != null && either.left().isPresent()){
                    lootTable = getLootTable(either.left().get());
                } else lootTable = either.right().get();

                result.addAll(getAllSingletonContainers(lootTable));
            }
            else if (container instanceof LootPoolSingletonContainer singletonContainer) {
                result.add(singletonContainer);
            } else {
                List<LootPoolEntryContainer> childrenContainers = (List<LootPoolEntryContainer>) Reflex.getFieldValue(container, "children");
                result.addAll(getAllSingletonContainers(childrenContainers));
            }
        }
        return result;
    }

    private List<LootPoolEntry> getAllEntries(List<LootPoolSingletonContainer> containers){
        List<LootPoolEntry> entries = new ArrayList<>();
        for (LootPoolSingletonContainer container : containers) {
            entries.add((LootPoolEntry) Reflex.getFieldValue(container, "entry"));
        }
        return entries;
    }

/*    private List<LootPoolEntry> getAllEntries(List<LootPoolEntryContainer> containers){
        List<LootPoolEntry> entries = new ArrayList<>();
        for (LootPoolEntryContainer container : containers) {
            entries.addAll(getAllEntries(container));
        }
        return entries;
    }
    private List<LootPoolEntry> getAllEntries(LootPoolEntryContainer container){
        if (container instanceof LootPoolSingletonContainer){
            LootPoolEntry entry = (LootPoolEntry) Reflex.getFieldValue(container, "entry");
            return List.of(entry);
        } else {
            List<LootPoolEntry> entries = new ArrayList<>();
            List<LootPoolEntryContainer> childrenContainers = (List<LootPoolEntryContainer>) Reflex.getFieldValue(container, "children");
            for (LootPoolEntryContainer childContainer : childrenContainers) {
                entries.addAll(getAllEntries(childContainer));
            }
            return entries;
        }
    }*/


    public @NotNull org.bukkit.loot.LootTable getDeathLootTable(@NotNull org.bukkit.entity.LivingEntity bukkitEntity){
        LivingEntity entity = ((CraftLivingEntity) bukkitEntity).getHandle();
        ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootTable = entity.getLootTable();
        return getLootTable(lootTable).craftLootTable;
    }

    public void setDeathLootTable(@NotNull org.bukkit.entity.Mob bukkitMob, @Nullable org.bukkit.loot.LootTable lootTable){
        Mob mob = ((CraftMob) bukkitMob).getHandle();
        if (lootTable == null){
            mob.lootTable = getResourceKeyLootTable("minecraft:empty");
        } else {
            mob.lootTable = getResourceKeyLootTable(lootTable.getKey().asString());
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // STRUCTURE
    ///////////////////////////////////////////////////////////////////////////
    public ItemStack generateExplorerMap(Location location, Structure bukkitStructure, int radius, boolean skipKnownStructures){
        return generateExplorerMap(location, bukkitStructure, radius, skipKnownStructures, MapCursor.Type.RED_X);
    }
    public ItemStack generateExplorerMap(Location location, Structure bukkitStructure, int radius, boolean skipKnownStructures, MapCursor.Type type){
        ServerLevel serverLevel = NmsUtils.toNmsWorld(location.getWorld());

        CraftStructure craftStructure = (CraftStructure) bukkitStructure;
        if (craftStructure == null){
            LogUtils.log("structure not found in registry!");
            return null;
        }
        net.minecraft.world.level.levelgen.structure.Structure structure = craftStructure.getHandle();
        HolderSet.Direct<net.minecraft.world.level.levelgen.structure.Structure> holders = HolderSet.direct(Holder.direct(structure));

        Pair<BlockPos, Holder<net.minecraft.world.level.levelgen.structure.Structure>> pair =
                serverLevel.getChunkSource().getGenerator().findNearestMapStructure(
                        serverLevel, holders, CraftLocation.toBlockPosition(location), radius, skipKnownStructures);
        if (pair == null){
            LogUtils.log("structure not found in world!");
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
    public boolean containStructure(Chunk bukitChunk, Structure bukkitStructure){
        net.minecraft.world.level.levelgen.structure.Structure targerStructure = ((CraftStructure) bukkitStructure).getHandle();
        ChunkAccess chunk = ((CraftChunk) bukitChunk).getHandle(ChunkStatus.FEATURES);
        return chunk.getReferencesForStructure(targerStructure) != null;
    }
    // TODO: 6/21/2024 REMOVE DEBUG 
    public boolean isInStructure(Location location, Structure bukkitStructure, StructureStartSearchMethod method, int radius){

        long startTime = System.nanoTime();

        StructureStart startForStructure;

        if (method == StructureStartSearchMethod.LOCATE_STRUCTURE){
            StructureSearchResult searchResult = location.getWorld().locateNearestStructure(location, bukkitStructure, radius, false);
            if (searchResult == null){
                LogUtils.log("search result is null");
                return false;
            }
            Location searchLocation = searchResult.getLocation();

            net.minecraft.world.level.levelgen.structure.Structure structure = ((CraftStructure) bukkitStructure).getHandle();

            ChunkAccess chunk = ((CraftChunk) searchLocation.getChunk()).getHandle(ChunkStatus.FULL);
            startForStructure = chunk.getStartForStructure(structure);
        }
        else {
            net.minecraft.world.level.levelgen.structure.Structure structure = ((CraftStructure) bukkitStructure).getHandle();
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
    private StructureStart findStructureStartIn(ServerLevel world, net.minecraft.world.level.levelgen.structure.Structure structure, int xCenter, int zCenter, int quadRadius){
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
        Biome.Precipitation precipitation = NmsUtils.toNmsWorld(location.getWorld()).getBiome(blockPosition).value().getPrecipitationAt(blockPosition);
        return DownfallType.fromNMS(precipitation);
    }


}































