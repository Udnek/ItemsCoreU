package me.udnek.itemscoreu.nms.util.consumer;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.function.Consumer;

public interface ItemConsumer extends Consumer<ItemStack> {
    Collection<ItemStack> get();
}
