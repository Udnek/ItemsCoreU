package me.udnek.itemscoreu.customrecipe.builder;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ShapedRecipeBuilder {
    private final ItemStack replace;
    private String recipeKey;
    private String[] recipeShape;
    private boolean rewriteRecipe = true;
    private Map<Character, Material> materialIngredients = new HashMap<>();
    private Map<Character, CustomItem> customItemIngredients = new HashMap<>();
    private Map<Character, Tag<Material>> tagIngredients = new HashMap<>();

    public ShapedRecipeBuilder(@NotNull Material replaceMaterial){
        this.replace = new ItemStack(replaceMaterial);
        this.recipeKey = replaceMaterial.getKey().getKey().toLowerCase();
    }

    public ShapedRecipeBuilder recipeKey(@NotNull String recipeKey){
        this.recipeKey = recipeKey;
        return this;
    }

    public ShapedRecipeBuilder recipeShape(@NotNull String[] recipeShape){
        this.recipeShape = recipeShape;
        return this;
    }

    public ShapedRecipeBuilder materialIngredients(@NotNull Map<Character, Material> materialIngredients){
        this.materialIngredients = materialIngredients;
        return this;
    }

    public ShapedRecipeBuilder customItemIngredients(@NotNull Map<Character, CustomItem> customItemIngredients){
        this.customItemIngredients = customItemIngredients;
        return this;
    }

    public ShapedRecipeBuilder tagIngredients(@NotNull Map<Character, Tag<Material>> tagIngredients){
        this.tagIngredients = tagIngredients;
        return this;
    }

    public ShapedRecipeBuilder setAmount(int amount){
        this.replace.setAmount(amount);
        return this;
    }

    public ShapedRecipeBuilder rewriteRecipe(boolean rewriteRecipe) {
        this.rewriteRecipe = rewriteRecipe;
        return this;
    }

    public ShapedRecipeBuilder build(@NotNull Plugin plugin){
        if (rewriteRecipe){
            RecipeManager.getInstance().unregister(NamespacedKey.minecraft(recipeKey));
        }

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, recipeKey), replace);
        recipe.shape(recipeShape);

        for (Map.Entry<Character, Material> material : materialIngredients.entrySet()) {
            recipe.setIngredient(material.getKey(), new RecipeChoice.MaterialChoice(material.getValue()));
        }
        for (Map.Entry<Character, CustomItem> customItem : customItemIngredients.entrySet()) {
            recipe.setIngredient(customItem.getKey(), new RecipeChoice.ExactChoice(customItem.getValue().getItem()));
        }
        for (Map.Entry<Character, Tag<Material>> tag : tagIngredients.entrySet()) {
            recipe.setIngredient(tag.getKey(), new RecipeChoice.MaterialChoice(tag.getValue()));
        }

        RecipeManager.getInstance().register(recipe);
        return this;
    }
}
