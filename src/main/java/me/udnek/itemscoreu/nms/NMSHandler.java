package me.udnek.itemscoreu.nms;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public interface NMSHandler {

    net.minecraft.world.item.ItemStack getNMSItemStack(ItemStack itemStack);
    net.minecraft.world.item.ItemStack getNMSItemStack(Material material);
    ItemStack getNormalItemStack(net.minecraft.world.item.ItemStack itemStack);
    Item getNMSItem(Material material);

    float getCompostableChance(Material material);
    int getFuelTime(Material material);
    int getFoodNutrition(Material material);
    float getFoodSaturation(Material material);
    FoodProperties getFoodProperties(Material material);

    boolean isBrewableIngredient(Material material);
    boolean isBrewablePotion(ItemStack itemStack);
    boolean hasBrewingMix(ItemStack potion, Material ingredient);
    ItemStack getBrewingMix(ItemStack potion, Material ingredient);

    ArrayList<ItemStack> getPossibleLoot(LootTable lootTable);

    void glowEntityFor(Entity entity, List<Entity> forEntities, boolean isGlow);
}
