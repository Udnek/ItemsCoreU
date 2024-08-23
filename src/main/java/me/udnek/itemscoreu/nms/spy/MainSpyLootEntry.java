package me.udnek.itemscoreu.nms.spy;

import me.udnek.itemscoreu.customevent.LootTableGenerateEvent;
import me.udnek.itemscoreu.nms.ItemConsumer;
import me.udnek.itemscoreu.utils.LogUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;

import java.util.function.Consumer;

public class MainSpyLootEntry extends SpyLootEntry{
    protected final LootTable lootTable;
    protected ItemConsumer spyConsumer;
    public MainSpyLootEntry(LootTable lootTable, LootPoolSingletonContainer container) {
        super(container);
        this.spyConsumer = new ItemConsumer();
        this.lootTable = lootTable;
    }

    public ItemConsumer getSpyConsumer() {
        return spyConsumer;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext) {
        super.createItemStack(spyConsumer, lootContext);
        LootTableGenerateEvent event = new LootTableGenerateEvent(lootTable, spyConsumer, lootContext);
        boolean proceed = event.callEvent();
        if (proceed) spyConsumer.copyTo(consumer);
        spyConsumer.clear();
    }
}
