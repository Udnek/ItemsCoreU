package me.udnek.itemscoreu.customitem;

import io.papermc.paper.datacomponent.DataComponentType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.papermc.paper.datacomponent.DataComponentTypes.*;
import static io.papermc.paper.datacomponent.DataComponentTypes.ITEM_NAME;

public interface ComponentUpdatingCustomItem extends CustomItem{

    default void getComponentsToUpdate(@NotNull ConstructableCustomItem.ComponentConsumer consumer){
        consumer.accept(MAX_DAMAGE);
        consumer.accept(REPAIR_COST);
        consumer.accept(CONSUMABLE);
        consumer.accept(RARITY);
        consumer.accept(LORE);
        consumer.accept(ATTRIBUTE_MODIFIERS);
        consumer.accept(FOOD);
        consumer.accept(ITEM_MODEL);
        consumer.accept(REPAIRABLE);
        consumer.accept(ITEM_NAME);
    }

    @Override
    @Nullable
    default ItemStack update(@NotNull ItemStack itemStack) {
        ItemStack relevantItem = getItem();
        itemStack = itemStack.withType(relevantItem.getType());
        @NotNull ItemStack finalItemStack = itemStack;
        getComponentsToUpdate(new ConstructableCustomItem.ComponentConsumer() {
            @Override
            public <T> void accept(DataComponentType.@NotNull Valued<T> type) {
                T data = relevantItem.getData(type);
                if (data == null) finalItemStack.resetData(type);
                else finalItemStack.setData(type, data);
            }

            @Override
            public void accept(DataComponentType.@NotNull NonValued type) {
                if (finalItemStack.hasData(type) != relevantItem.hasData(type)) {
                    if (finalItemStack.hasData(type)) finalItemStack.resetData(type);
                    else finalItemStack.setData(type);
                }
            }
        });
        return itemStack;
    }
}