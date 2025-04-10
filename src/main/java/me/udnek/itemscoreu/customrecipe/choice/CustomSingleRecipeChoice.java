package me.udnek.itemscoreu.customrecipe.choice;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CustomSingleRecipeChoice extends CustomCompatibleRecipeChoice {

    public CustomSingleRecipeChoice(@NotNull CustomItem customItem) {
        super(Set.of(customItem), Set.of());
    }
    public CustomSingleRecipeChoice(@NotNull Material material) {
        super(Set.of(), Set.of(material));
    }
}
