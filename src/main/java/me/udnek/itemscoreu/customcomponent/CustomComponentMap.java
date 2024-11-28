package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class CustomComponentMap<HolderType> implements Iterable<CustomComponent<HolderType>>{
    private final Map<CustomComponentType<HolderType, ?>, CustomComponent<HolderType>> map = new HashMap<>();

    public @NotNull <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> Component getOrDefault(@NotNull Type type) {
        return (Component) map.getOrDefault(type, type.getDefault());
    }

    public @NotNull <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> Component getOrException(@NotNull Type type){
        return Objects.requireNonNull(get(type), "Component " + type.getKey().asString() + " is not present!");
    }

    public @Nullable <Type extends CustomComponentType<HolderType, Component>, Component extends CustomComponent<HolderType>> Component get(@NotNull Type type) {
        return (Component) map.get(type);
    }
    public <Type extends CustomComponentType<HolderType, ?>> boolean has(@NotNull Type type) {
        return map.containsKey(type);
    }
    public <Component extends CustomComponent<HolderType>> void set(@NotNull Component component) {
        map.put(component.getType(), component);
    }
    public <Type extends CustomComponentType<HolderType, ?>> void remove(@NotNull Type type) {
        map.remove(type);
    }

    @Override
    public @NotNull Iterator<CustomComponent<HolderType>> iterator() {
        return map.values().iterator();
    }
}
