package me.udnek.itemscoreu.nms.util.consumer;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultipleItemConsumer implements ItemConsumer {

    protected List<ItemStack> itemStacks = new ArrayList<>();
    @Override
    public void accept(ItemStack itemStack) {
        itemStacks.add(itemStack);
    }
    @Override
    public Collection<ItemStack> get() {
        return itemStacks;
    }
}
