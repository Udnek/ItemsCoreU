package me.udnek.itemscoreu.custominventory;

import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface SmartIntractableCustomInventory extends CustomInventory {
    boolean canPlaceItem(@Nullable ItemStack itemStack, int slot);
    boolean canTakeItem(@Nullable ItemStack itemStack, int slot);
    @Override
    default void onPlayerClicksItem(InventoryClickEvent event) {
        int slot = event.getSlot();
/*        LogUtils.log("slot: " + slot);
        LogUtils.log("actn:" + event.getAction());*/
        if (event.getClickedInventory() == getInventory()){
            if (isPlaceAction(event.getAction())) {
                if (canPlaceItem(event.getCursor(), slot)) return;
                event.setCancelled(true);
            } else if (isTakeAction(event.getAction())){
                if (canTakeItem(event.getCurrentItem(), slot)) return;
                event.setCancelled(true);
            }
        }
        else {
            // TODO: 7/18/2024 FIX MOVE TO OTHER INVENTORY
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
            }
            //LogUtils.log("slot: " + event.getSlot());
            //LogUtils.log("rawSlot: " + event.getRawSlot());
            //LogUtils.log("item: " + event.getCurrentItem());
        }

    }

    @Override
    default void onPlayerDragsItem(InventoryDragEvent event) {
        // TODO: 8/25/2024 FIX (NOT WORKING CURRENTLY)
        for (Integer slot : event.getInventorySlots()) {
            if (!canPlaceItem(null, slot)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    static boolean isPlaceAction(InventoryAction action){
        return switch (action) {
            case PLACE_ALL, PLACE_SOME, PLACE_ONE, SWAP_WITH_CURSOR -> true;
            default -> false;
        };
    }
    static boolean isTakeAction(InventoryAction action){
        return switch (action) {
            case PICKUP_ALL,
                    PICKUP_ONE,
                    PICKUP_HALF,
                    PICKUP_SOME,
                    SWAP_WITH_CURSOR,
                    MOVE_TO_OTHER_INVENTORY,
                    COLLECT_TO_CURSOR,
                    DROP_ONE_SLOT,
                    DROP_ALL_SLOT -> true;
            default -> false;
        };
    }

}
