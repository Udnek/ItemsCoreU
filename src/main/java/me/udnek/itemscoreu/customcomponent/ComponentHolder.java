package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;

public interface ComponentHolder<HolderType, Component extends CustomComponent<HolderType>> {
     @NotNull CustomComponentMap<HolderType, Component> getComponents();
}
