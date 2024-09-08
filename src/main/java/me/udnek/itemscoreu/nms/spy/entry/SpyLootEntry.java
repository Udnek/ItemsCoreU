package me.udnek.itemscoreu.nms.spy.entry;

import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Deprecated
public abstract class SpyLootEntry implements LootPoolEntry {

    protected final LootPoolSingletonContainer container;
    protected final int weight;
    protected final int quality;
    protected final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
    protected static final Method createItemStackMethod;

    static {
        createItemStackMethod = Reflex.getMethod(
                LootPoolSingletonContainer.class, "createItemStack",
                Consumer.class, LootContext.class
        );
    }

    public SpyLootEntry(LootPoolSingletonContainer container){
        this.container = container;
        weight = (int) Reflex.getFieldValue(container, "weight");
        quality = (int) Reflex.getFieldValue(container, "quality");
        compositeFunction = (BiFunction<ItemStack, LootContext, ItemStack>) Reflex.getFieldValue(container, "compositeFunction");
    }
    @Override
    public int getWeight(float var0) {
        return Math.max(Mth.floor((float)weight + (float)quality * var0), 0);
    }
    @Override
    public void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext){
        Reflex.invokeMethod(container, createItemStackMethod, LootItemFunction.decorate(compositeFunction, consumer, lootContext), lootContext);
    }
}












