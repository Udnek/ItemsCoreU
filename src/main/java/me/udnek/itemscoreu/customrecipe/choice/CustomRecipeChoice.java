package me.udnek.itemscoreu.customrecipe.choice;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CustomRecipeChoice extends RecipeChoice {
    List<ItemStack> getAllPossible();
    boolean replaceItem(@NotNull ItemStack oldItem, @NotNull ItemStack newItem);
    boolean addItem(@NotNull ItemStack itemStack);
    boolean removeItem(@NotNull ItemStack itemStack);

    @Override @Deprecated
    @NotNull
    default ItemStack getItemStack(){
        return getAllPossible().getFirst();
    }
}
