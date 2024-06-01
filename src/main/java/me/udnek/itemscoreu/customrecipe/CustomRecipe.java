package me.udnek.itemscoreu.customrecipe;

import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

public interface CustomRecipe extends Keyed, Recipe {

    // TODO: 3/9/2024 COMPLETE IT

    RecipeChoice[] getAllInputChoices();
    ItemStack getCraftingStation();

}
