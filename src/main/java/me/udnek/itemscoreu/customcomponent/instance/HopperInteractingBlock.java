package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customentitylike.block.CustomBlockType;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.jetbrains.annotations.NotNull;

public interface HopperInteractingBlock extends CustomComponent<CustomBlockType> {

    HopperInteractingBlock DENY = new HopperInteractingBlock() {
        @Override
        public void onHopperInteract(@NotNull HopperInventorySearchEvent event) {
            event.setInventory(null);
        }
        @Override
        public void onItemMoveInto(@NotNull InventoryMoveItemEvent event) {
            event.setCancelled(true);
        }
        @Override
        public void onItemMoveFrom(@NotNull InventoryMoveItemEvent event) {
            event.setCancelled(true);
        }
    };

    void onHopperInteract(@NotNull HopperInventorySearchEvent event);

    void onItemMoveInto(@NotNull InventoryMoveItemEvent event);
    void onItemMoveFrom(@NotNull InventoryMoveItemEvent event);


    @Override
    @NotNull
    default CustomComponentType<CustomBlockType, ?> getType() {
        return CustomComponentType.HOPPER_INTERACTING_BLOCK;
    }
}
