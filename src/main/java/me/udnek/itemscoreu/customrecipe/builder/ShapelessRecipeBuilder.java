package me.udnek.itemscoreu.customrecipe.builder;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipeBuilder {
    private final ItemStack replace;
    private String recipeKey;
    private final List<RecipeChoice> recipeChoices = new ArrayList<>();

    public ShapelessRecipeBuilder(@NotNull Material recipeMaterial) {
        this.replace = new ItemStack(recipeMaterial);
        this.recipeKey = recipeMaterial.getKey().getKey();
    }

    public ShapelessRecipeBuilder(@NotNull CustomItem recipeCustomItem) {
        this.replace = recipeCustomItem.getItem();
        this.recipeKey = recipeCustomItem.getNewRecipeKey().getKey();
    }

    public ShapelessRecipeBuilder recipeKey(@NotNull String recipeKey){
        this.recipeKey = recipeKey;
        return this;
    }

    public ShapelessRecipeBuilder addIngredient(@NotNull CustomItem customItemAlloy, int amount){
        for (int i = 0; i < amount; i++){this.recipeChoices.add(new RecipeChoice.ExactChoice(customItemAlloy.getItem()));}
        return this;
    }

    public ShapelessRecipeBuilder addIngredient(@NotNull Material materialAlloy, int amount){
        for (int i = 0; i < amount; i++){this.recipeChoices.add(new RecipeChoice.MaterialChoice(materialAlloy));}
        return this;
    }

    public ShapelessRecipeBuilder addIngredient(@NotNull Tag<Material> materialAddition, int amount){
        for (int i = 0; i < amount; i++){this.recipeChoices.add(new RecipeChoice.MaterialChoice(materialAddition));}
        return this;
    }

    public ShapelessRecipeBuilder build(@NotNull Plugin plugin){
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(plugin, recipeKey), replace);
        for (RecipeChoice recipeChoice : recipeChoices){
            recipe.addIngredient(recipeChoice);
        }
        RecipeManager.getInstance().register(recipe);
        return this;
    }
}
