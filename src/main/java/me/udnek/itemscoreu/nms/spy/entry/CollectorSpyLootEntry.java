package me.udnek.itemscoreu.nms.spy.entry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;

import java.util.function.Consumer;

@Deprecated
public class CollectorSpyLootEntry extends SpyLootEntry{

    protected final MainSpyLootEntry mainSpy;

    public CollectorSpyLootEntry(MainSpyLootEntry mainSpy, LootPoolSingletonContainer container) {
        super(container);
        this.mainSpy = mainSpy;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext) {
        super.createItemStack(mainSpy.getSpyConsumer(), lootContext);
    }
}
