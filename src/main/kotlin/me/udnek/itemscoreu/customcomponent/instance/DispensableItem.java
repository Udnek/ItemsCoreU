package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.nms.Nms;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public interface DispensableItem extends CustomComponent<CustomItem> {
    DispensableItem DEFAULT = new DispensableItem() {
        @Override
        public void onDispense(@NotNull CustomItem item, @NotNull BlockDispenseEvent event) {}

        @Override
        public void onDrop(@NotNull CustomItem item, @NotNull BlockDispenseEvent event) {}
    };

    DispensableItem ALWAYS_DROP = new DispensableItem() {
        @Override
        public void onDispense(@NotNull CustomItem item, @NotNull BlockDispenseEvent event) {
            event.setCancelled(true);
            Block block = event.getBlock();
            ItemStack itemStack = event.getItem();
            Nms.get().simulateDropperDrop(itemStack, block);

            Inventory inventory = ((Dispenser) block.getState()).getInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventory.removeItemAnySlot(itemStack);
                }
            }.runTaskLater(ItemsCoreU.getInstance(), 1);
        }

        @Override
        public void onDrop(@NotNull CustomItem item, @NotNull BlockDispenseEvent event) {}
    };

    void onDispense(@NotNull CustomItem item, @NotNull BlockDispenseEvent event);

    void onDrop(@NotNull CustomItem item, @NotNull BlockDispenseEvent event);

    @Override
    default @NotNull CustomComponentType<? extends CustomItem, ? extends CustomComponent<CustomItem>> getType() {
        return CustomComponentType.DISPENSABLE_ITEM;
    }
}
