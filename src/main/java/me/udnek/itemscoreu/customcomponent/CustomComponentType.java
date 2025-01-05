package me.udnek.itemscoreu.customcomponent;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customblock.CustomBlockType;
import me.udnek.itemscoreu.customcomponent.instance.*;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.customregistry.Registrable;
import org.jetbrains.annotations.NotNull;

public interface CustomComponentType<HolderType, Component extends CustomComponent<HolderType>> extends Registrable {

    CustomComponentType<CustomItem, CustomItemAttributesComponent>
            CUSTOM_ATTRIBUTED_ITEM = register(new ConstructableComponentType("custom_attributed_item", CustomItemAttributesComponent.DEFAULT_EMPTY));

    CustomComponentType<CustomItem, RightClickableItem>
            RIGHT_CLICKABLE_ITEM = register(new ConstructableComponentType("right_clickable_item", RightClickableItem.EMPTY));

    CustomComponentType<CustomBlockType, RightClickableBlock>
            RIGHT_CLICKABLE_BLOCK = register(new ConstructableComponentType("right_clickable_block", RightClickableBlock.EMPTY));

    CustomComponentType<CustomItem, VanillaAttributesComponent>
            VANILLA_ATTRIBUTED_ITEM = register(new ConstructableComponentType("vanilla_attributed_item", VanillaAttributesComponent.DEFAULT));

    @NotNull Component getDefault();

    private static <HolderType, Component extends CustomComponent<HolderType>> CustomComponentType<HolderType, Component> register(CustomComponentType<HolderType, Component> type){
        return (CustomComponentType<HolderType, Component>) CustomRegistries.COMPONENT_TYPE.register(ItemsCoreU.getInstance(), type);
    }
}
