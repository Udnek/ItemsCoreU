package me.udnek.itemscoreu.nms.entry;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public interface ItemStackCreator {
    @NotNull ItemStack createItemStack(@NotNull LootContext lootContext);

    class Simple implements ItemStackCreator{
        protected ItemStack itemStack;
        public Simple(@NotNull ItemStack itemStack){
            this.itemStack = itemStack;
        }
        @Override
        public @NotNull ItemStack createItemStack(@NotNull LootContext lootContext) {
            return itemStack.clone();
        }
    }
    class CustomSimple implements ItemStackCreator{
        protected CustomItem customItem;
        public CustomSimple(@NotNull CustomItem customItem){
            this.customItem = customItem;
        }
        @Override
        public @NotNull ItemStack createItemStack(@NotNull LootContext lootContext) {
            return customItem.getItem();
        }
    }
}
