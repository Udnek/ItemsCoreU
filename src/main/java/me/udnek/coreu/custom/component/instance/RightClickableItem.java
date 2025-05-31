package me.udnek.coreu.custom.component.instance;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.CustomItemComponent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface RightClickableItem extends CustomItemComponent {

    RightClickableItem EMPTY = (item, event) -> {};

    void onRightClick(@NotNull CustomItem item, @NotNull PlayerInteractEvent event);

    @Override
    default @NotNull CustomComponentType<? extends CustomItem, ? extends CustomComponent<CustomItem>> getType() {
        return CustomComponentType.RIGHT_CLICKABLE_ITEM;
    }
}
