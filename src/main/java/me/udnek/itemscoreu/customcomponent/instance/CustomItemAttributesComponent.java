package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customattribute.CustomAttributesContainer;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.jetbrains.annotations.NotNull;

public class CustomItemAttributesComponent implements CustomComponent<CustomItem>  {

    public static final CustomItemAttributesComponent EMPTY = new CustomItemAttributesComponent(CustomAttributesContainer.empty());

    CustomAttributesContainer container;

    public CustomItemAttributesComponent(@NotNull CustomAttributesContainer container){
        this.container = container;
    }

    public @NotNull CustomAttributesContainer getAttributes() {return container;}

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return CustomComponentType.CUSTOM_ATTRIBUTED_ITEM;
    }
}
