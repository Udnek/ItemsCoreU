package me.udnek.itemscoreu.nms;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NmsItemConsumer implements Consumer<ItemStack> {

    protected List<ItemStack> itemStacks = new ArrayList<>();
    @Override
    public void accept(ItemStack itemStack) {
        itemStacks.add(itemStack);
    }
    public List<ItemStack> get() {
        return itemStacks;
    }
    public void clear() {
        itemStacks.clear();
    }
    public void copyTo(Consumer<ItemStack> other){
        itemStacks.forEach(other);
    }

    @Override
    public String toString() {
        return itemStacks.toString();
    }
}
