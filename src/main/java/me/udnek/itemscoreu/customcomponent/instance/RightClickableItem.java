package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public abstract class RightClickableItem implements CustomComponent<CustomItem> {

    public static final RightClickableItem EMPTY = new RightClickableItem() {
        @Override
        public void onRightClick(@NotNull CustomItem item, @NotNull PlayerInteractEvent event) {}
    };

    public abstract void onRightClick(@NotNull CustomItem item, @NotNull PlayerInteractEvent event);

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return CustomComponentType.RIGHT_CLICKABLE_ITEM;
    }
}
