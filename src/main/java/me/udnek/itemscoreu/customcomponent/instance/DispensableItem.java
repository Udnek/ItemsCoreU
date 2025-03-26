package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
            Item entity = (Item) block.getWorld().spawnEntity(block.getLocation(), EntityType.ITEM);
            entity.setVelocity(event.getVelocity());
            ItemStack itemStack = event.getItem();
            entity.setItemStack(itemStack);
            Inventory inventory = ((Dispenser) block.getState()).getInventory();
            int firstItem = inventory.first(itemStack);
            inventory.setItem(firstItem, itemStack.getAmount() == 1 ? null : itemStack.add(-1));
        }

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
