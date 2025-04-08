package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractComponentHolder<HolderType, Component extends CustomComponent<HolderType>> implements ComponentHolder<HolderType, Component> {
    private CustomComponentMap<HolderType, Component> components = null;

    @Override
    public @NotNull CustomComponentMap<HolderType, Component> getComponents() {
        if (components == null) components = new CustomComponentMap<>();
        return components;
    }
}
