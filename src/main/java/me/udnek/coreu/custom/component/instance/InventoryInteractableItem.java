package me.udnek.coreu.custom.component.instance;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.CustomItemComponent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface InventoryInteractableItem extends CustomItemComponent {
    InventoryInteractableItem EMPTY = new InventoryInteractableItem() {
        @Override
        public void onBeingClicked(@NotNull CustomItem item, @NotNull InventoryClickEvent event) {}
        @Override
        public void onClickWith(@NotNull CustomItem item, @NotNull InventoryClickEvent event) {}
    };

    void onBeingClicked(@NotNull CustomItem item, @NotNull InventoryClickEvent event);
    void onClickWith(@NotNull CustomItem item, @NotNull InventoryClickEvent event);

    default @NotNull CustomComponentType<? extends CustomItem, ? extends CustomComponent<CustomItem>> getType() {
        return CustomComponentType.INVENTORY_INTERACTABLE_ITEM;
    }
}
