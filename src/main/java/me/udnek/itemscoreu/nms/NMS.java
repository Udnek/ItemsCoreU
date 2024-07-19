package me.udnek.itemscoreu.nms;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.LongSet;
import me.udnek.itemscoreu.utils.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.GameTestAddMarkerDebugPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R4.CraftChunk;
import org.bukkit.craftbukkit.v1_20_R4.CraftLootTable;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.map.CraftMapCursor;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapCursor;
import org.bukkit.util.StructureSearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NMS{
    private static NMS instance;
    public static NMS get(){
        if (instance == null){
            instance = new NMS();
        }
        return instance;
    }
    ///////////////////////////////////////////////////////////////////////////
    // ITEMS
    ///////////////////////////////////////////////////////////////////////////

    public net.minecraft.world.item.ItemStack toNMSItemStack(ItemStack itemStack){
        return CraftItemStack.asNMSCopy(itemStack);
    }
    public ItemStack toBukkitItemStack(net.minecraft.world.item.ItemStack itemStack){
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    public Item toNMSItem(Material material){
        return CraftMagicNumbers.getItem(material);
    }

    public BlockPos toNMSBlockPosition(Location location){
        return new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public ServerLevel toNMSWorld(World world){
        return ((CraftWorld) world).getHandle();
    }

    ///////////////////////////////////////////////////////////////////////////
    // USAGES
    ///////////////////////////////////////////////////////////////////////////

    public float getCompostableChance(Material material){
        return ComposterBlock.COMPOSTABLES.getOrDefault(toNMSItem(material), 0);
    }

    public int getFuelTime(Material material){
        return AbstractFurnaceBlockEntity.getFuel().getOrDefault(toNMSItem(material), 0);
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

    public ArrayList<ItemStack> getPossibleLoot(LootTable lootTable) {

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


            for (LootPoolEntryContainer entry : lootPoolEntryContainers) {

                if (!(entry instanceof LootItem lootItem)) continue;

                ItemConsumer itemConsumer = new ItemConsumer();
                lootItem.createItemStack(itemConsumer, null);

                result.add(toBukkitItemStack(itemConsumer.itemStack));
            }
        }
        return result;
    }

    ///////////////////////////////////////////////////////////////////////////
    // STRUCTURE
    ///////////////////////////////////////////////////////////////////////////

    public ItemStack generateExplorerMap(Location location, Structure bukkitStructure, int radius, boolean skipKnownStructures){
        return generateExplorerMap(location, bukkitStructure, radius, skipKnownStructures, MapCursor.Type.RED_X);
    }
    public ItemStack generateExplorerMap(Location location, Structure bukkitStructure, int radius, boolean skipKnownStructures, MapCursor.Type type){
        ServerLevel serverLevel = toNMSWorld(location.getWorld());

        CraftStructure craftStructure = (CraftStructure) bukkitStructure;
        if (craftStructure == null){
            LogUtils.log("structure not found in registry!");
            return null;
        }
        net.minecraft.world.level.levelgen.structure.Structure structure = craftStructure.getHandle();
        HolderSet.Direct<net.minecraft.world.level.levelgen.structure.Structure> holders = HolderSet.direct(Holder.direct(structure));

        Pair<BlockPos, Holder<net.minecraft.world.level.levelgen.structure.Structure>> pair = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(serverLevel, holders, toNMSBlockPosition(location), radius, skipKnownStructures);
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
        MapItem.renderBiomePreviewMap(serverLevel, toNMSItemStack(itemStack));
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
            startForStructure = findStructureStartIn(toNMSWorld(location.getWorld()), structure, location.getChunk().getX(), location.getChunk().getZ(), radius);
        }
        
        LogUtils.log("time taken:" + (System.nanoTime() - startTime));


        if (startForStructure == null){
            LogUtils.log("start is null");
            return false;
        }

        BlockPos position = toNMSBlockPosition(location);

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
        GameTestAddMarkerDebugPayload payload = new GameTestAddMarkerDebugPayload(toNMSBlockPosition(location), color, name, time * 1000/20);
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundCustomPayloadPacket(payload));
    }

    public void showDebugBlock(Player player, Location location, int color, int time){
        showDebugBlock(player, location, color, "", time);
    }

}































