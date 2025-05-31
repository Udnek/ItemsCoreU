package me.udnek.coreu.custom.recipe.choice;

import me.udnek.coreu.custom.item.CustomItem;
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
