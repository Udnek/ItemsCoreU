package me.udnek.itemscoreu.customcomponent.test;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customitem.CustomItem;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.NotNull;

public class TestComponent implements CustomComponent<TestHolder, TestType> {
    @Override
    public @NotNull TestType getType() {
        return new TestType();
    }
}
