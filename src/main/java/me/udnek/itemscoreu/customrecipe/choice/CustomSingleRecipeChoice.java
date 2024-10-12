package me.udnek.itemscoreu.customrecipe.choice;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;

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
