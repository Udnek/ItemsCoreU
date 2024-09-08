package me.udnek.itemscoreu.customrecipe.choice;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomCompatibleRecipeChoice implements CustomRecipeChoice {

    protected List<CustomItem> customs = new ArrayList<>();
    protected List<Material> materials = new ArrayList<>();

    public CustomCompatibleRecipeChoice(@NotNull List<CustomItem> customItems, @NotNull List<Material> materialItems){
        Preconditions.checkArgument(customItems.size() + materialItems.size() > 0, "Choice can not be empty!");
        for (CustomItem custom : customItems) {
            Preconditions.checkArgument(custom != null, "Item can not be null!");
            if (customs.contains(custom)) continue;
            customs.add(custom);
        }
        for (Material material : materialItems) {
            Preconditions.checkArgument(material != null, "Item can not be null!");
            if (materials.contains(material)) continue;
            materials.add(material);
        }
    }
    public static CustomCompatibleRecipeChoice fromCustomItems(@NotNull CustomItem ...customItems){
        return new CustomCompatibleRecipeChoice(List.of(customItems), List.of());
    }
    public static CustomCompatibleRecipeChoice fromMaterials(@NotNull Material ...materialItems){
        return new CustomCompatibleRecipeChoice(List.of(), List.of(materialItems));
    }

    @Override
    public @NotNull RecipeChoice clone() {
        return new CustomCompatibleRecipeChoice(customs, materials);
    }

    @Override
    public boolean test(@NotNull ItemStack itemStack) {
        CustomItem customItem = CustomItem.get(itemStack);
        if (customItem != null){
            return customs.contains(customItem);
        }
        return materials.contains(itemStack.getType());
    }

    @Override
    public List<ItemStack> getAllPossible() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (Material material : materials) itemStacks.add(new ItemStack(material));
        for (CustomItem customItem : customs) itemStacks.add(customItem.getItem());
        return itemStacks;
    }

    @Override
    public boolean replaceItem(@NotNull ItemStack oldItem, @NotNull ItemStack newItem) {
        if (!removeItem(oldItem)) return false;
        return addItem(newItem);
    }

    @Override
    public boolean addItem(@NotNull ItemStack itemStack) {
        if (test(itemStack)) return false;
        CustomItem customItem = CustomItem.get(itemStack);
        if (customItem != null) return customs.add(customItem);
        else return materials.add(itemStack.getType());
    }

    @Override
    public boolean removeItem(@NotNull ItemStack itemStack) {
        if (!test(itemStack)) return false;
        CustomItem customItem = CustomItem.get(itemStack);
        if (customItem != null) return customs.remove(customItem);
        else return materials.remove(itemStack.getType());
    }
}

















