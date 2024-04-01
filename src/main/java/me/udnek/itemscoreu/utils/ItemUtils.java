package me.udnek.itemscoreu.utils;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.nms.NMSHelper;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static me.udnek.itemscoreu.utils.CustomItemUtils.*;

public class ItemUtils {
    public static List<Recipe> getRecipesOfItemStack(ItemStack itemStack){
        if (isCustomItem(itemStack)){
            CustomItem customItem = getFromId(getId(itemStack));
            return customItem.getRecipes();
        }
        else {
            List<Recipe> rawRecipes = Bukkit.getRecipesFor(itemStack);
            List<Recipe> recipes = new ArrayList<>();
            for (Recipe recipe : rawRecipes) {
                if (!(isCustomItem(recipe.getResult()))) {
                    recipes.add(recipe);
                }
            }
            return recipes;
        }
    }

    public static List<LootTable> getWhereItemStackInLootTable(ItemStack itemStack){
        ArrayList<LootTable> result = new ArrayList<LootTable>();

        if (CustomItemUtils.isCustomItem(itemStack)) return result;

        Iterator<LootTables> lootTablesIterator = Registry.LOOT_TABLES.iterator();
        //SKIPS EMPTY
        lootTablesIterator.next();
        while (lootTablesIterator.hasNext()){
            LootTable lootTable = lootTablesIterator.next().getLootTable();
            ArrayList<ItemStack> itemStacks = NMSHelper.getNMS().getPossibleLoot(lootTable);

            for (ItemStack loot : itemStacks) {
                if (loot.getType() == itemStack.getType()){
                    result.add(lootTable);
                    break;
                }
            }
        }
        return result;
    }


    public static List<Recipe> getItemInRecipesUsages(ItemStack itemStack){
        if (isCustomItem(itemStack)){
            return new ArrayList<Recipe>();
        }
        else {
            Material neededMaterial = itemStack.getType();
            Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
            ArrayList<Recipe> recipes = new ArrayList<Recipe>();
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

    public static FireworkEffectMeta setFireworkColor(FireworkEffectMeta itemMeta, Color color){
        FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withColor(color);
        itemMeta.setEffect(builder.build());
        return itemMeta;
    }


}













