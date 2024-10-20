package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.util.SelfRegisteringListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class CraftListener extends SelfRegisteringListener {

    public CraftListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPrepareItemCraftEven(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null) {
            return;
        }

        Recipe recipe = event.getRecipe();
        ItemStack[] matrix = event.getInventory().getMatrix();
        ArrayList<RecipeChoice> recipeChoices;

        if (recipe instanceof ShapedRecipe) {
            recipeChoices = new ArrayList<>(((ShapedRecipe) recipe).getChoiceMap().values());
        } else if (recipe instanceof ShapelessRecipe) {
            recipeChoices = (ArrayList<RecipeChoice>) ((ShapelessRecipe) recipe).getChoiceList();
        } else {
            return;
        }

        int amountOfExactChoices = 0;
        for (RecipeChoice recipeChoice : recipeChoices ) {
            if (recipeChoice instanceof RecipeChoice.ExactChoice) {
                amountOfExactChoices++;
            }
        }

        int amountOfCustomItems = 0;
        for (ItemStack itemStack : matrix) {
            if (itemStack != null && CustomItem.isCustom(itemStack)) {
                amountOfCustomItems++;
            }
        }


        if (amountOfExactChoices != amountOfCustomItems) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }

        if (CustomItem.isCustom(event.getRecipe().getResult())){
            event.getInventory().setResult(CustomItem.get(event.getRecipe().getResult()).onPrepareCraft(event));
        }
    }

    @EventHandler
    public void onFurnaceStartSmelt(FurnaceSmeltEvent event){
        CookingRecipe<?> recipe = event.getRecipe();
        ItemStack itemStack = event.getSource();
        if (!CustomItem.isCustom(itemStack)){
            return;
        }
        if (recipe.getInputChoice() instanceof RecipeChoice.MaterialChoice){
            event.setCancelled(true);
        }
    }
}
