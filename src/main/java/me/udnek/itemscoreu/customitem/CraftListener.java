package me.udnek.itemscoreu.customitem;

import com.google.common.base.Preconditions;
import io.papermc.paper.datacomponent.DataComponentTypes;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import me.udnek.itemscoreu.util.Utils;
import org.bukkit.Material;
import org.bukkit.block.Crafter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CraftListener extends SelfRegisteringListener {

    public CraftListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null) return;
        ItemStack result = event.getInventory().getResult();
        if (result == null) return;
        craft(
                event.getRecipe(),
                result,
                event.getInventory().getMatrix(),
                event.isRepair(),
                itemStack -> event.getInventory().setResult(itemStack)
        );

        Utils.consumeIfNotNull(CustomItem.get(event.getRecipe().getResult()), customItem ->
                customItem.onPrepareCraft(event, event.getInventory().getResult()));
    }

    @EventHandler
    public void onCrafterCraft(CrafterCraftEvent event) {
        craft(
                event.getRecipe(),
                event.getResult(),
                ((Crafter) event.getBlock().getState()).getInventory().getContents(),
                event.getRecipe().key().asString().equals("minecraft:repair_item"),
                event::setResult
        );
        Utils.consumeIfNotNull(CustomItem.get(event.getRecipe().getResult()), customItem ->
                customItem.onCrafterCraft(event));
    }

    public void craft(@NotNull final Recipe recipe, @NotNull final ItemStack result, final @Nullable ItemStack[] matrix, final boolean isRepair, @NotNull final Consumer<ItemStack> resultConsumer){
        List<RecipeChoice> recipeChoices;

        if (recipe instanceof ShapedRecipe shapedRecipe) {
            recipeChoices = new ArrayList<>(shapedRecipe.getChoiceMap().values());
        } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
            recipeChoices = shapelessRecipe.getChoiceList();
        } else if (recipe instanceof TransmuteRecipe transmuteRecipe) {
            recipeChoices = List.of(transmuteRecipe.getInput(), transmuteRecipe.getMaterial());
        } else if (isRepair) {
            ItemStack component0 = null;
            ItemStack component1 = null;
            for (ItemStack itemStack : matrix) {
                if (itemStack == null || itemStack.getType() == Material.AIR) continue;
                if (component0 == null) {
                    component0 = itemStack;
                }
                else {
                    component1 = itemStack;
                    break;
                }
            }
            Preconditions.checkArgument(component0 != null && component1 != null, "Repair item are null!");

            if (ItemUtils.isSameIds(component0, component1)){
                CustomItem customItem = CustomItem.get(component0);
                if (customItem == null) return;
                ItemStack newResult = customItem.getItem();
                newResult.setData(DataComponentTypes.DAMAGE, result.getDataOrDefault(DataComponentTypes.DAMAGE, 0));
                resultConsumer.accept(newResult);
            } else {
                resultConsumer.accept(new ItemStack(Material.AIR));
            }
            return;

        } else if (recipe instanceof ComplexRecipe) {
            for (@Nullable ItemStack itemStack : matrix) {
                if (itemStack == null) continue;
                if (CustomItem.isCustom(itemStack)) {
                    resultConsumer.accept(new ItemStack(Material.AIR));
                    return;
                }
            }
            return;
        } else return;

        int amountOfExactChoices = 0;
        for (RecipeChoice recipeChoice : recipeChoices ) {
            if (recipeChoice instanceof RecipeChoice.ExactChoice) {
                amountOfExactChoices++;
            }
        }

        int amountOfCustomItems = 0;
        for (ItemStack itemStack : matrix) {
            CustomItem customItem = CustomItem.get(itemStack);
            if (customItem != null && !VanillaItemManager.isReplaced(customItem)) {
                amountOfCustomItems++;
            }
        }


        if (amountOfExactChoices != amountOfCustomItems) {
            resultConsumer.accept(new ItemStack(Material.AIR));
        }
    }


    @EventHandler
    public void onFurnace(FurnaceSmeltEvent event){
        CookingRecipe<?> recipe = event.getRecipe();
        ItemStack itemStack = event.getSource();
        if (!CustomItem.isCustom(itemStack)) return;
        if (recipe.getInputChoice() instanceof RecipeChoice.MaterialChoice){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGrindstone(PrepareGrindstoneEvent event){
        ItemStack upperItem = event.getInventory().getUpperItem();
        ItemStack lowerItem = event.getInventory().getLowerItem();
        if (upperItem == null || lowerItem == null) return;
        if (ItemUtils.isSameIds(upperItem, lowerItem)) return;
        event.setResult(new ItemStack(Material.AIR));
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event){
        ItemStack firstItem = event.getInventory().getFirstItem();
        ItemStack secondItem = event.getInventory().getSecondItem();

        if (firstItem == null || secondItem == null) return;
        if (secondItem.getType() == Material.ENCHANTED_BOOK) return;
        if (ItemUtils.isSameIds(firstItem, secondItem)) return;

        if (!canBeRepaired(firstItem, secondItem)) event.setResult(new ItemStack(Material.AIR));
    }

    public boolean canBeRepaired(@NotNull ItemStack toBeRepaired, @NotNull ItemStack repairer){
        CustomItem toBeRepairedCustom = CustomItem.get(toBeRepaired);
        CustomItem repairerCustom = CustomItem.get(repairer);
        if (toBeRepairedCustom != null){
            RepairData repairData = toBeRepairedCustom.getRepairData();
            if (repairData == null) return true;
            return repairData.contains(repairer);
        }
        return repairerCustom == null;
    }
}















