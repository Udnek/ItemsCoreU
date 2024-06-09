package me.udnek.itemscoreu.nms;

import me.udnek.itemscoreu.utils.LogUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R4.CraftLootTable;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

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

    public net.minecraft.world.item.ItemStack getNMSItemStack(ItemStack itemStack){
        return CraftItemStack.asNMSCopy(itemStack);
    }
    public net.minecraft.world.item.ItemStack getNMSItemStack(Material material){
        return getNMSItemStack(new ItemStack(material));
    }

    public ItemStack getNormalItemStack(net.minecraft.world.item.ItemStack itemStack){
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    public Item getNMSItem(Material material){
        return CraftMagicNumbers.getItem(material);
    }

    ///////////////////////////////////////////////////////////////////////////
    // FOOD
    ///////////////////////////////////////////////////////////////////////////

/*
    public int getFoodNutrition(Material material) {
        FoodProperties foodProperties = this.getFoodProperties(material);
        if (foodProperties == null) return 0;
        return foodProperties.getNutrition();
    }

    public float getFoodSaturation(Material material) {
        FoodProperties foodProperties = this.getFoodProperties(material);
        if (foodProperties == null) return 0;
        return foodProperties.saturation();
    }

    public FoodProperties getFoodProperties(Material material) {
        new ItemStack(Material.GOLDEN_APPLE).getItemMeta().getAsComponentString()
        return getNMSItem(material).components();
    }
*/

    ///////////////////////////////////////////////////////////////////////////
    // USAGES
    ///////////////////////////////////////////////////////////////////////////


    public float getCompostableChance(Material material){
        return ComposterBlock.COMPOSTABLES.getOrDefault(getNMSItem(material), 0);
    }

    public int getFuelTime(Material material){
        return AbstractFurnaceBlockEntity.getFuel().getOrDefault(getNMSItem(material), 0);
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

                result.add(getNormalItemStack(itemConsumer.itemStack));
            }
        }
        return result;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ENTITY
    ///////////////////////////////////////////////////////////////////////////



    public net.minecraft.world.entity.Entity getNMSEntity(Entity entity) {
        return ((CraftEntity) entity).getHandle();
    }


/*    public void followEntity(Entity bukkitFollower, Entity bukkitTarget) {
        net.minecraft.world.entity.Entity follower = getNMSEntity(bukkitFollower);
        if (!(follower instanceof Mob)) return;
        PathNavigation navigation = ((Mob) follower).getNavigation();
        navigation.createPath()
    }*/
}































