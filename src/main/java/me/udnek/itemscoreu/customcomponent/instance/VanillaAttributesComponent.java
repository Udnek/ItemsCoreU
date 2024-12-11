package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customattribute.VanillaAttributesContainer;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.jetbrains.annotations.NotNull;

public class VanillaAttributesComponent implements CustomComponent<CustomItem> {

    public static final VanillaAttributesComponent DEFAULT = new VanillaAttributesComponent(VanillaAttributesContainer.empty());

    VanillaAttributesContainer container;

    public VanillaAttributesComponent(@NotNull VanillaAttributesContainer container){
        this.container = container;
    }

    public @NotNull VanillaAttributesContainer getAttributes(@NotNull CustomItem customItem) {return container;}

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return CustomComponentType.VANILLA_ATTRIBUTED_ITEM;
    }

}
