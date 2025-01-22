package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractComponentHolder<HolderType> implements ComponentHolder<HolderType> {
    private CustomComponentMap<HolderType> components = null;

    @Override
    public @NotNull CustomComponentMap<HolderType> getComponents() {
        if (components == null) components = new CustomComponentMap<>();
        return components;
    }
}
