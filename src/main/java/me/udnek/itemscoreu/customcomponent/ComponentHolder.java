package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ComponentHolder<HolderType> {
    <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> @NotNull Component getComponentOrDefault(@NotNull Type type);
    <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> @Nullable Component getComponent(@NotNull Type type);
    default  <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> void consumeIfHasComponent(@NotNull Type type, Consumer<Component> consumer){
        Component component = getComponent(type);
        if (component != null) consumer.accept(component);
    }
    <Type extends CustomComponentType<HolderType, ?>> boolean hasComponent(@NotNull Type type);
    <Component extends CustomComponent<HolderType>> void setComponent(@NotNull Component component);
    <Type extends CustomComponentType<HolderType, ?>> void removeComponent(@NotNull Type type);
    void iterateTroughAllComponents(Consumer<CustomComponent<HolderType>> consumer);
}
