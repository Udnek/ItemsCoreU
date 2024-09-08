package me.udnek.itemscoreu.customrecipe.choice;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CustomSingleRecipeChoice extends CustomCompatibleRecipeChoice {

    public CustomSingleRecipeChoice(CustomItem customItem) {
        super(Collections.singletonList(customItem), List.of());
    }
    public CustomSingleRecipeChoice(Material material) {
        super(List.of(), Collections.singletonList(material));
    }
}
