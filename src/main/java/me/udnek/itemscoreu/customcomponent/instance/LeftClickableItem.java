package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface LeftClickableItem extends CustomComponent<CustomItem> {

    LeftClickableItem EMPTY = (item, event) -> {};

    void onLeftClick(@NotNull CustomItem item, @NotNull PlayerInteractEvent event);

    @Override
    default @NotNull CustomComponentType<? extends CustomItem, ? extends CustomComponent<CustomItem>> getType() {
        return CustomComponentType.LEFT_CLICKABLE_ITEM;
    }
}
