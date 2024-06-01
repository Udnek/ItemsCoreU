package me.udnek.itemscoreu.nms;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ItemConsumer implements Consumer<ItemStack> {

    public ItemStack itemStack;

    @Override
    public void accept(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @NotNull
    @Override
    public Consumer<ItemStack> andThen(@NotNull Consumer<? super ItemStack> after) {
        return Consumer.super.andThen(after);
    }
}
