package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customattribute.CustomKeyedAttributeModifier;
import me.udnek.itemscoreu.customattribute.VanillaAttributesContainer;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaAttributesComponent implements CustomComponent<CustomItem> {

    public static final VanillaAttributesComponent EMPTY = new VanillaAttributesComponent(){
        @Override
        public void addAttribute(@NotNull Attribute attribute, @NotNull CustomKeyedAttributeModifier modifier) {
            throw new RuntimeException("Can not add attribute to default empty component. Set new component to item or use #safeAddAttribute if you want to modify attributes");
        }
    };

    VanillaAttributesContainer container;

    public static void safeAddAttribute(@NotNull CustomItem customItem, @NotNull Attribute attribute, @NotNull CustomKeyedAttributeModifier modifier){
        @Nullable VanillaAttributesComponent component = customItem.getComponents().get(CustomComponentType.VANILLA_ATTRIBUTED_ITEM);
        if (component == null) component = new VanillaAttributesComponent();
        component.addAttribute(attribute, modifier);
        customItem.getComponents().set(component);
    }

    public VanillaAttributesComponent(){
        this(VanillaAttributesContainer.empty());
    }

    public VanillaAttributesComponent(@NotNull VanillaAttributesContainer container){
        this.container = container;
    }

    public @NotNull VanillaAttributesContainer getAttributes(@NotNull CustomItem customItem) {return container;}

    public void addAttribute(@NotNull Attribute attribute, @NotNull CustomKeyedAttributeModifier modifier){
        container = new VanillaAttributesContainer.Builder().add(container).add(attribute, modifier).build();
    }

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return CustomComponentType.VANILLA_ATTRIBUTED_ITEM;
    }

}
