package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customattribute.CustomAttributeModifier;
import me.udnek.itemscoreu.customattribute.CustomAttributesContainer;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.jetbrains.annotations.NotNull;

public class CustomItemAttributesComponent implements CustomComponent<CustomItem>  {

    public static final CustomItemAttributesComponent DEFAULT_EMPTY = new CustomItemAttributesComponent(){
        @Override
        public void addAttribute(@NotNull CustomAttribute attribute, @NotNull CustomAttributeModifier modifier) {
            throw new RuntimeException("Can not add attribute to default empty component. Set new component to item or use #safeAddAttribute if you want to modify attributes");
        }
    };

    public static void safeAddAttribute(@NotNull CustomItem customItem, @NotNull CustomAttribute attribute, @NotNull CustomAttributeModifier modifier){
        CustomItemAttributesComponent component = customItem.getComponents().get(CustomComponentType.CUSTOM_ATTRIBUTED_ITEM);
        if (component == null) component = new CustomItemAttributesComponent();
        component.addAttribute(attribute, modifier);
        customItem.getComponents().set(component);
    }

    protected CustomAttributesContainer container;

    public CustomItemAttributesComponent(){
        this(CustomAttributesContainer.empty());
    }

    public CustomItemAttributesComponent(@NotNull CustomAttributesContainer container){
        this.container = container;
    }

    public void addAttribute(@NotNull CustomAttribute attribute, @NotNull CustomAttributeModifier modifier){
        container = new CustomAttributesContainer.Builder().add(container).add(attribute, modifier).build();
    }

    public @NotNull CustomAttributesContainer getAttributes() {return container;}

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return CustomComponentType.CUSTOM_ATTRIBUTED_ITEM;
    }
}
