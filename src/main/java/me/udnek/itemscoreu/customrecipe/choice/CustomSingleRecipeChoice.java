package me.udnek.itemscoreu.customrecipe.choice;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;

import java.util.Set;

public class CustomSingleRecipeChoice extends CustomCompatibleRecipeChoice {

    public CustomSingleRecipeChoice(CustomItem customItem) {
        super(Set.of(customItem), Set.of());
    }
    public CustomSingleRecipeChoice(Material material) {
        super(Set.of(), Set.of(material));
    }
}
