package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.instance.CoordinateWand;
import me.udnek.itemscoreu.customregistry.CustomRegistries;

public class Items {

    public static final CustomItem COORDINATE_WAND = register(new CoordinateWand());

    public static CustomItem register(CustomItem customItem){
        return CustomRegistries.ITEM.register(ItemsCoreU.getInstance(), customItem);
    }
}
