package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ComponentHolder<HolderType> {
    <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType, Type>> @NotNull Component getComponentOrDefault(@NotNull Type type);
    <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType, Type>> @Nullable Component getComponent(@NotNull Type type);
    <Type extends CustomComponentType<HolderType, ?>> boolean containsComponent(@NotNull Type type);
    <Type extends CustomComponentType<HolderType, ?>, Component extends CustomComponent<HolderType, Type>> void setComponent(@NotNull Component component);
    <Type extends CustomComponentType<HolderType, ?>> void removeComponent(@NotNull Type type);
    void iterateTroughAllComponents(Consumer<CustomComponent<HolderType, ?>> consumer);
}
