package me.udnek.itemscoreu.customitem;

import com.google.common.base.Preconditions;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Repairable;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.util.ItemUtils;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import me.udnek.itemscoreu.util.Utils;
import me.udnek.itemscoreu.util.VanillaItemManager;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftComplexRecipe;
import org.bukkit.event.EventHandler;
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

public class CraftListener extends SelfRegisteringListener {

    public CraftListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null) {
            return;
        }

        Recipe recipe = event.getRecipe();
        ItemStack[] matrix = event.getInventory().getMatrix();
        List<RecipeChoice> recipeChoices;

        if (recipe instanceof ShapedRecipe shapedRecipe) {
            recipeChoices = new ArrayList<>(shapedRecipe.getChoiceMap().values());
        } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
            recipeChoices = shapelessRecipe.getChoiceList();
        } else if (recipe instanceof TransmuteRecipe transmuteRecipe) {
            recipeChoices = List.of(transmuteRecipe.getInput(), transmuteRecipe.getMaterial());
        } else if (event.isRepair()) {
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
                newResult.setData(DataComponentTypes.DAMAGE, event.getInventory().getResult().getDataOrDefault(DataComponentTypes.DAMAGE, 0));
                event.getInventory().setResult(newResult);
                customItem.onPrepareCraft(event, newResult);
            } else {
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
            return;

        } else if (recipe instanceof ComplexRecipe complexRecipe) {
            for (@Nullable ItemStack itemStack : event.getInventory().getMatrix()) {
                if (itemStack == null) continue;
                if (CustomItem.isCustom(itemStack)) {
                    event.getInventory().setResult(new ItemStack(Material.AIR));
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
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }

        Utils.consumeIfNotNull(CustomItem.get(event.getRecipe().getResult()), customItem ->
                customItem.onPrepareCraft(event, event.getInventory().getResult()));
    }

    @EventHandler
    public void onFurnace(FurnaceSmeltEvent event){
        CookingRecipe<?> recipe = event.getRecipe();
        ItemStack itemStack = event.getSource();
        if (!CustomItem.isCustom(itemStack)){
            return;
        }
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

        //Repairable materialRepairable = toBeRepaired.getDataOrDefault(DataComponentTypes.REPAIRABLE, toBeRepaired.getType().getDefaultData(DataComponentTypes.REPAIRABLE));
        if (toBeRepairedCustom != null){
            RepairData repairData = toBeRepairedCustom.getRepairData();
            if (repairData == null) return true;
            return repairData.contains(repairer);
        }
        return repairerCustom == null;




/*        if (materialRepairable != null && repairerCustom == null){
            if (!materialRepairable.types().contains(TypedKey.create(RegistryKey.ITEM, repairer.getType().key()))) return false;
        }
        if (toBeRepairedCustom != null && repairerCustom != null){
            if (!toBeRepairedCustom.getComponents().getOrDefault(CustomComponentType.REPAIRABLE_WITH_CUSTOM_ITEM).canBeRepairedWith(repairerCustom)) return false;
        }
        return true;*/
    }
}















