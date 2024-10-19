package me.udnek.itemscoreu.nms.loot.util;

import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NmsLootFunctionsContainer extends NmsContainer<List<LootItemFunction>> {
    public NmsLootFunctionsContainer(@NotNull List<LootItemFunction> supply) {
        super(supply);
    }

    @Override
    public List<LootItemFunction> get() {
        return new ArrayList<>(supply);
    }
}
