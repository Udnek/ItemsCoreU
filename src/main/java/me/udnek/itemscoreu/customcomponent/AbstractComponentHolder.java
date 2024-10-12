package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractComponentHolder<HolderType> implements ComponentHolder<HolderType> {
    private final Map<CustomComponentType<HolderType, ?>, CustomComponent<HolderType>> components = new HashMap<>();
    @Override
    public @NotNull <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> Component getComponentOrDefault(@NotNull Type type) {
        return (Component) components.getOrDefault(type, type.getDefault());
    }
    @Override
    public @Nullable <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> Component getComponent(@NotNull Type type) {
        return (Component) components.get(type);
    }
    @Override
    public <Type extends CustomComponentType<HolderType, ?>> boolean hasComponent(@NotNull Type type) {
        return components.containsKey(type);
    }
    @Override
    public <Component extends CustomComponent<HolderType>> void setComponent(@NotNull Component component) {
        components.put(component.getType(), component);
    }
    @Override
    public <Type extends CustomComponentType<HolderType, ?>> void removeComponent(@NotNull Type type) {
        components.remove(type);
    }
    @Override
    public void iterateTroughAllComponents(Consumer<CustomComponent<HolderType>> consumer) {
        components.values().forEach(consumer);
    }
}
