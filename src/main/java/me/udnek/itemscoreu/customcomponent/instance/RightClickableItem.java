package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface RightClickableItem extends CustomComponent<CustomItem> {

    RightClickableItem EMPTY = (item, event) -> {};

    void onRightClick(@NotNull CustomItem item, @NotNull PlayerInteractEvent event);

    @Override
    default @NotNull CustomComponentType<CustomItem, ?> getType() {
        return CustomComponentType.RIGHT_CLICKABLE_ITEM;
    }
}
