package me.udnek.itemscoreu.nms.util.consumer;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Collections;

public class SingleItemConsumer implements ItemConsumer {

    public ItemStack itemStack;
    @Override
    public void accept(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    @Override
    public Collection<ItemStack> get() {
        return Collections.singleton(itemStack);
    }
}
