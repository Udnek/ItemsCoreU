package me.udnek.itemscoreu.utils;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


public class ItemUtils {

    public static boolean isSameIds(ItemStack itemA, ItemStack itemB){
        CustomItem customA = CustomItem.get(itemA);
        CustomItem customB = CustomItem.get(itemB);
        if (customA == null && customB == null) return itemA.getType() == itemB.getType();
        return customA == customB;
    }
    public static String getId(@NotNull ItemStack itemStack){
        CustomItem customItem = CustomItem.get(itemStack);
        if (customItem != null) return customItem.getId();
        return itemStack.getType().toString().toLowerCase();
    }

    public static boolean isCustomItemOrMaterial(String name){
        if (CustomItem.idExists(name)) return true;
        return Material.getMaterial(name.toUpperCase()) != null;
    }

    public static ItemStack getFromCustomItemOrMaterial(String name){
        if (CustomItem.idExists(name)) return CustomItem.get(name).getItem();
        Material material = Material.getMaterial(name.toUpperCase());
        if (material == null) return new ItemStack(Material.AIR);
        return new ItemStack(material);
    }

    @Deprecated
    public static List<Recipe> getRecipesOfItemStack(ItemStack itemStack){
        if (CustomItem.isCustom(itemStack)){
            CustomItem customItem = CustomItem.get(itemStack);
            ArrayList<Recipe> recipes = new ArrayList<>();
            customItem.getRecipes(recipes::add);
            return recipes;
        }
        else {
            List<Recipe> rawRecipes = Bukkit.getRecipesFor(itemStack);
            List<Recipe> recipes = new ArrayList<>();
            for (Recipe recipe : rawRecipes) {
                if (!(CustomItem.isCustom(recipe.getResult()))) {
                    recipes.add(recipe);
                }
            }
            return recipes;
        }
    }

    @Deprecated
    public static List<Recipe> getItemInRecipesUsages(ItemStack itemStack){
        // TODO: 7/26/2024 CUSTOM ITEM
        if (CustomItem.isCustom(itemStack)){
            return new ArrayList<>();
        }
        else {
            Material neededMaterial = itemStack.getType();
            Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
            ArrayList<Recipe> recipes = new ArrayList<>();
            Recipe recipe;
            while (recipeIterator.hasNext()){
                recipe = recipeIterator.next();
                if (recipe instanceof ShapedRecipe){
                    for(RecipeChoice recipeChoice: ((ShapedRecipe) recipe).getChoiceMap().values()) {
                        if (isMaterialInRecipeChoice(neededMaterial, recipeChoice)){
                            recipes.add(recipe);
                            break;
                        }
                    }
                }
                else if (recipe instanceof ShapelessRecipe){
                    for (RecipeChoice recipeChoice : ((ShapelessRecipe) recipe).getChoiceList()) {
                        if (isMaterialInRecipeChoice(neededMaterial, recipeChoice)){
                            recipes.add(recipe);
                            break;
                        }
                    }
                }
                else if (recipe instanceof CookingRecipe){
                    if (isMaterialInRecipeChoice(neededMaterial, ((CookingRecipe<?>) recipe).getInputChoice())){
                        recipes.add(recipe);
                    }
                }
                else if (recipe instanceof StonecuttingRecipe){
                    if (isMaterialInRecipeChoice(neededMaterial, ((StonecuttingRecipe) recipe).getInputChoice())){
                        recipes.add(recipe);
                    }
                }
                else if (recipe instanceof SmithingTransformRecipe){
                    if (isMaterialInRecipeChoice(neededMaterial, ((SmithingTransformRecipe) recipe).getBase()) ||
                            isMaterialInRecipeChoice(neededMaterial, ((SmithingTransformRecipe) recipe).getTemplate()) ||
                            isMaterialInRecipeChoice(neededMaterial, ((SmithingTransformRecipe) recipe).getAddition())){

                        recipes.add(recipe);
                    }
                }
                else if (recipe instanceof SmithingTrimRecipe){
                    if (isMaterialInRecipeChoice(neededMaterial, ((SmithingTrimRecipe) recipe).getBase()) ||
                            isMaterialInRecipeChoice(neededMaterial, ((SmithingTrimRecipe) recipe).getTemplate()) ||
                            isMaterialInRecipeChoice(neededMaterial, ((SmithingTrimRecipe) recipe).getAddition())){

                        recipes.add(recipe);
                    }
                }
            }
            return recipes;
        }
    }

    private static boolean isMaterialInRecipeChoice(Material material, RecipeChoice recipeChoice){
        if (!(recipeChoice instanceof RecipeChoice.MaterialChoice)) return false;
        List<Material> choices = ((RecipeChoice.MaterialChoice) recipeChoice).getChoices();
        return choices.contains(material);
    }

    public static void setFireworkColor(FireworkEffectMeta itemMeta, Color color){
        FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withColor(color);
        itemMeta.setEffect(builder.build());
    }
    public static void setFireworkColor(FireworkEffectMeta itemMeta, int color){
        setFireworkColor(itemMeta, Color.fromRGB(color));
    }

}













