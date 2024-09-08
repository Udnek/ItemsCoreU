package me.udnek.itemscoreu.customcomponent.test;

import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.jetbrains.annotations.NotNull;

public class TestType implements CustomComponentType<TestHolder, TestComponent> {
    private static final TestComponent DEFAULT = new TestComponent();

    public TestType(){

    }

    @Override
    public @NotNull TestComponent getDefault() {
        return DEFAULT;
    }
}
