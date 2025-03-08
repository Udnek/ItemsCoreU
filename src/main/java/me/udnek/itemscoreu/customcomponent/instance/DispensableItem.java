package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.event.block.BlockDispenseEvent;
import org.jetbrains.annotations.NotNull;

public interface DispensableItem extends CustomComponent<CustomItem> {
    DispensableItem DEFAULT = new DispensableItem() {
        @Override
        public void onDispense(@NotNull CustomItem item, @NotNull BlockDispenseEvent event) {}

        @Override
        public void onDrop(@NotNull CustomItem item, @NotNull BlockDispenseEvent event) {}
    };

    void onDispense(@NotNull CustomItem item, @NotNull BlockDispenseEvent event);

    void onDrop(@NotNull CustomItem item, @NotNull BlockDispenseEvent event);

    @Override
    default @NotNull CustomComponentType<CustomItem, ?> getType() {
        return CustomComponentType.DISPENSABLE_ITEM;
    }
}
