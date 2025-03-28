package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CustomComponentMap<HolderType> implements Iterable<CustomComponent<HolderType>>{
    private @Nullable Map<CustomComponentType<HolderType, ?>, CustomComponent<HolderType>> map = null;

    public @NotNull <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> Component getOrDefault(@NotNull Type type) {
        if (map == null) return type.getDefault();
        return (Component) map.getOrDefault(type, type.getDefault());
    }

    public @NotNull <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> Component getOrCreateDefault(@NotNull Type type) {
        Component component = get(type);
        if (component == null) {
            Component newDefault = type.createNewDefault();
            set(newDefault);
            return newDefault;
        }
        return component;
    }

    public @NotNull <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> Component getOrException(@NotNull Type type){
        return Objects.requireNonNull(map == null ? null : get(type), "Component " + type.getKey().asString() + " is not present!");
    }

    public @Nullable <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> Component get(@NotNull Type type) {
        if (map == null) return null;
        return (Component) map.get(type);
    }
    public <Type extends CustomComponentType<HolderType, ?>> boolean has(@NotNull Type type) {
        if (map == null) return false;
        return map.containsKey(type);
    }
    public <Component extends CustomComponent<HolderType>> void set(@NotNull Component component) {
        if (map == null) map = new HashMap<>();
        map.put(component.getType(), component);
    }
    public <Type extends CustomComponentType<HolderType, ?>> void remove(@NotNull Type type) {
        if (map != null) map.remove(type);
    }

    @Override
    public @NotNull Iterator<CustomComponent<HolderType>> iterator() {
        if (map == null) return Collections.emptyIterator();
        return map.values().iterator();
    }
}
