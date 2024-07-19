package me.udnek.itemscoreu.customrecipe;

import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CustomRecipe extends Recipe, Keyed {
    @NotNull Collection<ItemStack> getResults();
    boolean isResult(ItemStack itemStack);
    boolean isIngredient(ItemStack itemStack);
    @NotNull CustomRecipeType getType();
    @Override
    @Deprecated
    @NotNull ItemStack getResult();
}
