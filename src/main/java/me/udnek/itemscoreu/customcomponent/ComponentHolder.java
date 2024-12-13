package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;

public interface ComponentHolder<HolderType> {
     @NotNull CustomComponentMap<HolderType> getComponents();
}
