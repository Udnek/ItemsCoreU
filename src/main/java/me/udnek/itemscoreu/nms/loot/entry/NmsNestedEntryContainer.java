package me.udnek.itemscoreu.nms.loot.entry;

import com.mojang.datafixers.util.Either;
import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.nms.loot.table.NmsDefaultLootTableContainer;
import me.udnek.itemscoreu.nms.loot.table.NmsLootTableContainer;
import me.udnek.itemscoreu.nms.loot.util.NmsFields;
import me.udnek.itemscoreu.util.Reflex;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.List;

public class NmsNestedEntryContainer extends NmsSingletonEntryContainer{
    public NmsNestedEntryContainer(@NotNull NestedLootTable supply) {
        super(supply);
    }

    public static @NotNull NmsNestedEntryContainer newFrom(@NotNull NmsLootTableContainer contents){
        Constructor<NestedLootTable> constructor = Reflex.getFirstConstructor(NestedLootTable.class);
        Either<ResourceKey<LootTable>, LootTable> right = Either.right(contents.get());
        NestedLootTable nested = Reflex.construct(
                constructor,
                right,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                List.of(),
                List.of()
        );
        return new NmsNestedEntryContainer(nested);
    }

    public @NotNull NmsLootTableContainer getLootTable(){
        Either<ResourceKey<LootTable>, LootTable> fieldValue = (Either<ResourceKey<LootTable>, LootTable>) Reflex.getFieldValue(supply, NmsFields.CONTENTS);
        LootTable lootTable;
        if (fieldValue.left().isPresent()) lootTable = NmsUtils.getLootTable(fieldValue.left().get());
        else lootTable = fieldValue.right().get();
        return new NmsDefaultLootTableContainer(lootTable);
    }

    public void setLooTable(@NotNull NmsLootTableContainer looTable){
        Either<ResourceKey<LootTable>, LootTable> right = Either.right(looTable.get());
        Reflex.setFieldValue(supply, NmsFields.CONTENTS, right);
    }

    @Override
    public NestedLootTable get() {
        return (NestedLootTable) supply;
    }
}
