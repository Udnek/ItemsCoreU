package me.udnek.coreu.nms.loot.util;

import me.udnek.coreu.nms.NmsContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NmsLootConditionsContainer extends NmsContainer<List<LootItemCondition>> {
    public NmsLootConditionsContainer(@NotNull List<LootItemCondition> supply) {
        super(supply);
    }

    @Override
    public List<LootItemCondition> get() {
        if (supply == null) return List.of();
        return new ArrayList<>(supply);
    }
}
