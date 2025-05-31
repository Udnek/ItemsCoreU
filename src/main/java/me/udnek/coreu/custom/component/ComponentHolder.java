package me.udnek.coreu.custom.component;

import org.jetbrains.annotations.NotNull;

public interface ComponentHolder<HolderType, Component extends CustomComponent<HolderType>> {
     @NotNull CustomComponentMap<HolderType, Component> getComponents();
}
