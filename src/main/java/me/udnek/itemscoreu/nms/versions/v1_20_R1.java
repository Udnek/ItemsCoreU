package me.udnek.itemscoreu.nms.versions;

import me.udnek.itemscoreu.nms.ItemConsumer;
import me.udnek.itemscoreu.nms.NMSHandler;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_20_R1.CraftLootTable;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class v1_20_R1 implements NMSHandler {

    ///////////////////////////////////////////////////////////////////////////
    // ITEMS
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public net.minecraft.world.item.ItemStack getNMSItemStack(ItemStack itemStack){
        return CraftItemStack.asNMSCopy(itemStack);
    }
    @Override
    public net.minecraft.world.item.ItemStack getNMSItemStack(Material material){
        return getNMSItemStack(new ItemStack(material));
    }

    public ItemStack getNormalItemStack(net.minecraft.world.item.ItemStack itemStack){
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    @Override
    public Item getNMSItem(Material material){
        return CraftMagicNumbers.getItem(material);
    }

    ///////////////////////////////////////////////////////////////////////////
    // FOOD
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int getFoodNutrition(Material material) {
        FoodProperties foodProperties = this.getFoodProperties(material);
        if (foodProperties == null) return 0;
        return foodProperties.getNutrition();
    }

    @Override
    public float getFoodSaturation(Material material) {
        FoodProperties foodProperties = this.getFoodProperties(material);
        if (foodProperties == null) return 0;
        return foodProperties.getSaturationModifier();
    }

    @Override
    public FoodProperties getFoodProperties(Material material) {
        return getNMSItem(material).getFoodProperties();
    }

    ///////////////////////////////////////////////////////////////////////////
    // USAGES
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public float getCompostableChance(Material material){
        return ComposterBlock.COMPOSTABLES.getOrDefault(getNMSItem(material), 0);
    }

    @Override
    public int getFuelTime(Material material){
        return AbstractFurnaceBlockEntity.getFuel().get(getNMSItem(material));
    }

    ///////////////////////////////////////////////////////////////////////////
    // BREWING
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isBrewableIngredient(Material material){
        return PotionBrewing.isIngredient(getNMSItemStack(material));
    }

    @Override
    public boolean isBrewablePotion(ItemStack itemStack){
        net.minecraft.world.item.ItemStack nmsItemStack = getNMSItemStack(itemStack);
        Potion potion = PotionUtils.getPotion(nmsItemStack);
        return PotionBrewing.isBrewablePotion(potion);
    }

    @Override
    public boolean hasBrewingMix(ItemStack potion, Material ingredient){
        return PotionBrewing.hasMix(getNMSItemStack(potion), getNMSItemStack(ingredient));
    }

    @Override
    public ItemStack getBrewingMix(ItemStack potion, Material ingredient){
        net.minecraft.world.item.ItemStack mixed = PotionBrewing.mix(getNMSItemStack(potion), getNMSItemStack(ingredient));
        return getNormalItemStack(mixed);
    }

    @Override
    public ArrayList<ItemStack> getPossibleLoot(LootTable lootTable) {

        ArrayList<ItemStack> result = new ArrayList<>();

        net.minecraft.world.level.storage.loot.LootTable nmsLootTable = ((CraftLootTable) lootTable).getHandle();
        LootPool[] lootPools;

        try {
            lootPools = (LootPool[]) FieldUtils.readField(nmsLootTable, "f", true);
        } catch (IllegalAccessException e) {throw new RuntimeException(e);}

/*        Class clazz = lootPools[0].getClass();
        Bukkit.getLogger().info(Arrays.toString(clazz.getDeclaredFields()));*/

        for (LootPool lootPool : lootPools) {
            LootPoolEntryContainer[] lootPoolEntryContainers;
            try {
                lootPoolEntryContainers = (LootPoolEntryContainer[]) FieldUtils.readField(lootPool, "a", true);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }


            for (LootPoolEntryContainer entry : lootPoolEntryContainers) {

                if (!(entry instanceof LootItem)) continue;


                LootItem lootItem = (LootItem) entry;

                ItemConsumer itemConsumer = new ItemConsumer();
                lootItem.createItemStack(itemConsumer, null);

                result.add(getNormalItemStack(itemConsumer.itemStack));
            }
        }
        return result;
    }

    @Override
    public void glowEntityFor(Entity entity, List<Entity> forEntities, boolean isGlow) {

    }

}































