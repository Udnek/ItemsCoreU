package me.udnek.coreu.nms.loot.util;

import me.udnek.coreu.nms.NmsContainer;
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
        if (supply == null) return List.of();
        return new ArrayList<>(supply);
    }
}
