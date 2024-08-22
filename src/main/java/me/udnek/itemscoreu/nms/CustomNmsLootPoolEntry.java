package me.udnek.itemscoreu.nms;

import me.udnek.itemscoreu.customevent.LootEntryCreateItemEvent;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class CustomNmsLootPoolEntry implements LootPoolEntry {

    private final LootPoolSingletonContainer container;
    private final LootTable lootTable;
    private final int weight;
    private final int quality;
    private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
    private static final Method createItemStackMethod;

    static {
        createItemStackMethod = Reflex.getMethod(
                LootPoolSingletonContainer.class, "createItemStack",
                Consumer.class, LootContext.class
        );
    }

    public CustomNmsLootPoolEntry(LootTable lootTable, LootPoolSingletonContainer container){
        this.lootTable = lootTable;
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
    public void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext) {
        ItemConsumer injectedConsumer = new ItemConsumer();
        Reflex.invokeMethod(container, createItemStackMethod, LootItemFunction.decorate(compositeFunction, injectedConsumer, lootContext), lootContext);

        LootEntryCreateItemEvent event = new LootEntryCreateItemEvent(lootTable, injectedConsumer);
        boolean shouldProceed = event.callEvent();
        if (!shouldProceed) return;

        injectedConsumer.copyTo(consumer);
    }
}












