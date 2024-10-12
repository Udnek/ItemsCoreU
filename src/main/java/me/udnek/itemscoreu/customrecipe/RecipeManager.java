package me.udnek.itemscoreu.customrecipe;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.utils.ItemUtils;
import me.udnek.itemscoreu.utils.LogUtils;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeManager {

    private final HashMap<String, CustomRecipe<?>> customRecipes = new HashMap<>();
    private static RecipeManager instance;
    private RecipeManager(){}
    public static RecipeManager getInstance() {
        if (instance == null) instance = new RecipeManager();
        return instance;
    }
    public void register(@NotNull Recipe recipe){
        if (recipe instanceof CustomRecipe<?> customRecipe){
            Preconditions.checkArgument(customRecipe.key() != null, "Recipe results can not be null!");
            Preconditions.checkArgument(customRecipe.getResults() != null, "Recipe results can not be null!");
            Preconditions.checkArgument(customRecipe.getType() != null, "Recipe type can not be null!");
            Preconditions.checkArgument(
                    !customRecipes.containsKey(customRecipe.key().asString()),
                    "Recipe id duplicate: " + customRecipe.key().asString() + "!"
            );
            customRecipes.put(customRecipe.key().asString(), customRecipe);
        }
        else {
            Bukkit.addRecipe(recipe);
        }
    }
    public List<Recipe> getRecipesAsResult(ItemStack itemStack){
        List<Recipe> recipes = ItemUtils.getRecipesOfItemStack(itemStack);
        for (CustomRecipe<?> recipe : customRecipes.values()) {
            if (recipe.isResult(itemStack)) recipes.add(recipe);
        }
        return recipes;
    }

    public List<Recipe> getRecipesAsIngredient(ItemStack itemStack){
        List<Recipe> recipes = ItemUtils.getItemInRecipesUsages(itemStack);
        for (CustomRecipe<?> recipe : customRecipes.values()) {
            if (recipe.isIngredient(itemStack)) recipes.add(recipe);
        }
        return recipes;
    }

    // TODO: 8/25/2024 OPTIMIZE USING OTHER HASHMAP
    public <T extends CustomRecipe<?>> List<T> getByType(CustomRecipeType<T> type){
        List<T> recipes = new ArrayList<>();
        for (CustomRecipe<?> recipe : customRecipes.values()) {
            if (recipe.getType() == type) recipes.add((T) recipe);
        }
        return recipes;
    }

    public <T extends CustomRecipe<?>> @Nullable T getCustom(@NotNull CustomRecipeType<T> type, @NotNull NamespacedKey id){
        List<T> byType = getByType(type);
        for (T recipe : byType) {
            if (recipe.getKey().equals(id)) return recipe;
        }
        return null;
    }

    public void unregister(Recipe recipe){
        if (recipe instanceof CustomRecipe<?> customRecipe){
            customRecipes.remove(customRecipe.key().asString());
            LogUtils.pluginLog("Custom recipe was unregistered: " + customRecipe.key().asString());
        } else {
            if (!(recipe instanceof Keyed keyed)) return;
            Bukkit.removeRecipe((NamespacedKey) keyed.key());
            LogUtils.pluginLog("Vanilla recipe was unregistered: " + keyed.key().asString());
        }
    }
    public void unregister(NamespacedKey key){
        Bukkit.removeRecipe(key);
        customRecipes.remove(key.asString());
    }

    public void unregister(Iterable<Recipe> recipes){
        for (Recipe recipe : recipes) unregister(recipe);
    }
}






















