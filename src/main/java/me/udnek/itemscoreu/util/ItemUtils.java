package me.udnek.itemscoreu.util;

import me.udnek.itemscoreu.customitem.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class ItemUtils {

    public static boolean containsSame(@NotNull ItemStack itemStack, @NotNull Collection<Material> materials, @NotNull Collection<CustomItem> customs){
        CustomItem customItem = CustomItem.get(itemStack);
        if (customItem != null){
            if (VanillaItemManager.isReplaced(customItem)) return customs.contains(customItem) || materials.contains(itemStack.getType());
            else return customs.contains(customItem);
        }
        return materials.contains(itemStack.getType());
    }

    public static boolean isVanillaMaterial(@NotNull ItemStack itemStack, @NotNull Material material){
        return !CustomItem.isCustom(itemStack) && itemStack.getType() == material;
    }

    public static boolean isSameIds(@NotNull ItemStack itemA, @NotNull ItemStack itemB){
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
    public static @NotNull Component getDisplayName(@NotNull ItemStack itemStack){
        if (itemStack.hasItemMeta()){
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.hasDisplayName()) return itemMeta.displayName();
            if (itemMeta.hasItemName()) return itemMeta.itemName();
        }
        return Component.translatable(itemStack.getType().getItemTranslationKey());
    }

    public static boolean isCustomItemOrMaterial(@NotNull String name){
        if (CustomItem.idExists(name)) return true;
        return Material.getMaterial(name.toUpperCase()) != null;
    }

    public static ItemStack getFromCustomItemOrMaterial(@NotNull String name){
        if (CustomItem.idExists(name)) return CustomItem.get(name).getItem();
        Material material = Material.getMaterial(name.toUpperCase());
        if (material == null) return new ItemStack(Material.AIR);
        return new ItemStack(material);
    }

    @Deprecated
    public static void getItemInRecipesUsages(@NotNull ItemStack itemStack, @NotNull Consumer<Recipe> consumer){
        if (CustomItem.isCustom(itemStack)){
            CustomItem customItem = CustomItem.get(itemStack);
            iterateTroughRecipesChoosing(consumer, new Predicate<RecipeChoice>() {
                @Override
                public boolean test(RecipeChoice recipeChoice) {
                    if (recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice)
                        return materialChoice.getChoices().stream().anyMatch(choice -> customItem.isThisItem(new ItemStack(choice)));
                    if (recipeChoice instanceof RecipeChoice.ExactChoice exactChoice)
                        return exactChoice.getChoices().stream().anyMatch(choice -> customItem.isThisItem(choice));
                    return false;
                }
            });
        }
        else {
            Material neededMaterial = itemStack.getType();
            iterateTroughRecipesChoosing(consumer, new Predicate<RecipeChoice>() {
                @Override
                public boolean test(RecipeChoice recipeChoice) {
                    if (!(recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice)) return false;
                    List<Material> choices = materialChoice.getChoices();
                    return choices.contains(neededMaterial);
                }
            });
        }
    }


    public static void iterateTroughRecipesChoosing(@NotNull Consumer<Recipe> recipes, @NotNull Predicate<RecipeChoice> predicate){
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()){
            Recipe recipe = recipeIterator.next();

            if (recipe instanceof ShapedRecipe shapedRecipe){
                for(RecipeChoice recipeChoice: shapedRecipe.getChoiceMap().values()) {
                    if (predicate.test(recipeChoice)){
                        recipes.accept(recipe);
                        break;
                    }
                }
            }
            else if (recipe instanceof ShapelessRecipe shapelessRecipe){
                for (RecipeChoice recipeChoice : shapelessRecipe.getChoiceList()) {
                    if (predicate.test(recipeChoice)){
                        recipes.accept(recipe);
                        break;
                    }
                }
            }
            else if (recipe instanceof CookingRecipe<?> cookingRecipe){
                if (predicate.test(cookingRecipe.getInputChoice())){
                    recipes.accept(recipe);
                }
            }
            else if (recipe instanceof StonecuttingRecipe stonecuttingRecipe){
                if (predicate.test(stonecuttingRecipe.getInputChoice())){
                    recipes.accept(recipe);
                }
            }
            else if (recipe instanceof SmithingTransformRecipe smithingTransformRecipe){
                if (
                        predicate.test(smithingTransformRecipe.getBase()) ||
                        predicate.test(smithingTransformRecipe.getTemplate()) ||
                        predicate.test(smithingTransformRecipe.getAddition())
                )
                {recipes.accept(recipe);}
            }
            else if (recipe instanceof SmithingTrimRecipe smithingTrimRecipe){
                if (
                        predicate.test(smithingTrimRecipe.getBase()) ||
                        predicate.test(smithingTrimRecipe.getTemplate()) ||
                        predicate.test(smithingTrimRecipe.getAddition())
                )
                {recipes.accept(recipe);}
            } else if (recipe instanceof TransmuteRecipe transmuteRecipe) {
                if (predicate.test(transmuteRecipe.getInput()) || predicate.test(transmuteRecipe.getMaterial())){
                    recipes.accept(recipe);
                }
            }
        }
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













