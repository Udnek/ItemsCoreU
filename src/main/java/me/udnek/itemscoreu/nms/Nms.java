package me.udnek.itemscoreu.nms;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.udnek.itemscoreu.nms.consumer.ItemConsumer;
import me.udnek.itemscoreu.nms.consumer.MultipleItemConsumer;
import me.udnek.itemscoreu.nms.consumer.SingleItemConsumer;
import me.udnek.itemscoreu.utils.LogUtils;
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
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
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
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R4.CraftChunk;
import org.bukkit.craftbukkit.v1_20_R4.CraftLootTable;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.map.CraftMapCursor;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftLocation;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapCursor;
import org.bukkit.util.StructureSearchResult;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Nms {

    private static final LootContext GENERIC_LOOT_CONTEXT;

    static {
        ServerLevel serverLevel = ((CraftWorld) Bukkit.getWorld("world")).getHandle();
        LootParams.Builder paramsBuilder = new LootParams.Builder(serverLevel);
        LootContextParamSet contextParamSet = new LootContextParamSet.Builder().build();
        paramsBuilder.withLuck(1);
        LootContext.Builder contextBuilder = new LootContext.Builder(paramsBuilder.create(contextParamSet));
        GENERIC_LOOT_CONTEXT = contextBuilder.create(Optional.empty());
    }


    private static Nms instance;
    public static Nms get(){
        if (instance == null){
            instance = new Nms();
        }
        return instance;
    }
    ///////////////////////////////////////////////////////////////////////////
    // ITEMS
    ///////////////////////////////////////////////////////////////////////////

    public net.minecraft.world.item.ItemStack toNmsItemStack(ItemStack itemStack){
        return CraftItemStack.asNMSCopy(itemStack);
    }
    public Item toNmsMaterial(Material material){
        return CraftMagicNumbers.getItem(material);
    }
    public ItemStack toBukkitItemStack(net.minecraft.world.item.ItemStack itemStack){
        return CraftItemStack.asBukkitCopy(itemStack);
    }
    public ServerLevel toNmsWorld(World world){
        return ((CraftWorld) world).getHandle();
    }

    ///////////////////////////////////////////////////////////////////////////
    // USAGES
    ///////////////////////////////////////////////////////////////////////////

    public float getCompostableChance(Material material){
        return ComposterBlock.COMPOSTABLES.getOrDefault(toNmsMaterial(material), 0);
    }

    public int getFuelTime(Material material){
        return AbstractFurnaceBlockEntity.getFuel().getOrDefault(toNmsMaterial(material), 0);
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
                            toBukkitItemStack(merchantOffer.getResult()),
                            merchantOffer.getMaxUses()
                    );
                    List<ItemStack> ingredients = new ArrayList<>();
                    ingredients.add(toBukkitItemStack(merchantOffer.getCostA()));
                    ingredients.add(toBukkitItemStack(merchantOffer.getCostB()));
                    merchantRecipe.setIngredients(ingredients);

                    recipes.add(merchantRecipe);
                }
            }
        }
        return recipes;
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
    public List<LootTable> getRegisteredLootTables(){
        List<LootTable> lootTables = new ArrayList<>();
        ReloadableServerRegistries.Holder registries = ((CraftServer) Bukkit.getServer()).getServer().reloadableRegistries();
        Collection<ResourceLocation> keys = registries.getKeys(Registries.LOOT_TABLE);
        for (ResourceLocation key : keys) {
            lootTables.add(registries.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, new ResourceLocation(key.toString()))).craftLootTable);
        }
        return lootTables;
    }
    public @NotNull LootTable getLootTable(String id){
        ResourceLocation resourceLocation = new ResourceLocation(id);
        ResourceKey<net.minecraft.world.level.storage.loot.LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, resourceLocation);
        return getLootTable(key);
    }
    protected @NotNull LootTable getLootTable(@NotNull ResourceKey<net.minecraft.world.level.storage.loot.LootTable> resourceKey){
        ReloadableServerRegistries.Holder registries = ((CraftServer) Bukkit.getServer()).getServer().reloadableRegistries();
        return registries.getLootTable(resourceKey).craftLootTable;
    }
    public List<ItemStack> getPossibleLoot(LootTable lootTable) {

        ArrayList<ItemStack> result = new ArrayList<>();

        net.minecraft.world.level.storage.loot.LootTable nmsLootTable = ((CraftLootTable) lootTable).getHandle();
        List<LootPool> lootPools;

        try {
            lootPools = (List<LootPool>) FieldUtils.readField(nmsLootTable, "pools", true);
        } catch (IllegalArgumentException e) {
            LogUtils.logDeclaredFields(nmsLootTable);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (LootPool lootPool : lootPools) {
            List<LootPoolEntryContainer> lootPoolEntryContainers;

            try {
                lootPoolEntryContainers = (List<LootPoolEntryContainer>) FieldUtils.readField(lootPool, "entries", true);
            } catch (IllegalArgumentException e) {
                LogUtils.logDeclaredFields(lootPool);
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }


            for (LootPoolEntryContainer entryContainer : lootPoolEntryContainers) {
                if (!(entryContainer instanceof LootPoolSingletonContainer container)) continue;

                LootPoolEntry poolEntry;

                try {
                    poolEntry = (LootPoolEntry) FieldUtils.readField(container, "entry", true);
                } catch (IllegalArgumentException e) {
                    LogUtils.logDeclaredFields(lootPool);
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                ItemConsumer itemConsumer = new MultipleItemConsumer();
                poolEntry.createItemStack(itemConsumer, GENERIC_LOOT_CONTEXT);


/*                ItemConsumer itemConsumer = null;

                if (entryContainer instanceof LootItem entry) {
                    entry.createItemStack(itemConsumer = new SingleItemConsumer(), null);
                } else if (entryContainer instanceof TagEntry entry) {
                    entry.createItemStack(itemConsumer = new MultipleItemConsumer(), null);
                    // TODO: 7/25/2024 ADD ALTERNATIVES ENTRY
                } else if (entryContainer instanceof NestedLootTable entry) {
                    entry.createItemStack(itemConsumer = new MultipleItemConsumer(), null);
                } else if (entryContainer instanceof DynamicLoot entry) {
                    entry.createItemStack(itemConsumer = new MultipleItemConsumer(), null);
                }
                // TODO: 7/25/2024 ADD ALTERNATIVES ENTRY

                if (itemConsumer == null) continue;*/
                for (net.minecraft.world.item.ItemStack itemStack : itemConsumer.get()) {
                    if (itemStack.getCount() == 0) itemStack.setCount(1);
                    result.add(toBukkitItemStack(itemStack));
                }
            }
        }
        return result;
    }
    public @NotNull LootTable getDeathLootTable(@NotNull org.bukkit.entity.LivingEntity bukkitEntity){
        LivingEntity entity = ((CraftLivingEntity) bukkitEntity).getHandle();
        return getLootTable(entity.getLootTable());

    }
    ///////////////////////////////////////////////////////////////////////////
    // STRUCTURE
    ///////////////////////////////////////////////////////////////////////////
    public ItemStack generateExplorerMap(Location location, Structure bukkitStructure, int radius, boolean skipKnownStructures){
        return generateExplorerMap(location, bukkitStructure, radius, skipKnownStructures, MapCursor.Type.RED_X);
    }
    public ItemStack generateExplorerMap(Location location, Structure bukkitStructure, int radius, boolean skipKnownStructures, MapCursor.Type type){
        ServerLevel serverLevel = toNmsWorld(location.getWorld());

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
/*
        try {
            Method privateMethod = MapItemSavedData.class.getDeclaredMethod("addDecoration");
            privateMethod.setAccessible(true);
            privateMethod.invoke(null, mapDecorationTypeHolder, null, "++", (double) structureLocation.getX(), (double) structureLocation.getZ(), (double) 0, Component.translatable("test.test"));


        } catch (NoSuchMethodException e) {
            LogUtils.log(Arrays.toString(MapItemSavedData.class.getDeclaredMethods()));
            //throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
*/

        return toBukkitItemStack(mapItem);
    }
    public void generateBiomePreviewMap(World world, ItemStack itemStack){
        ServerLevel serverLevel = ((CraftWorld) world).getHandle();
        MapItem.renderBiomePreviewMap(serverLevel, toNmsItemStack(itemStack));
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
            startForStructure = findStructureStartIn(toNmsWorld(location.getWorld()), structure, location.getChunk().getX(), location.getChunk().getZ(), radius);
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
        Biome.Precipitation precipitation = toNmsWorld(location.getWorld()).getBiome(blockPosition).value().getPrecipitationAt(blockPosition);
        return DownfallType.fromNMS(precipitation);
    }


}































