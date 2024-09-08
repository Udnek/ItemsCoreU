package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;

public interface CustomComponentType<HolderType, Component extends CustomComponent<HolderType, ?>> {
    @NotNull Component getDefault();
}
