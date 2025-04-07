package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;

public interface CustomComponent<HolderType> {
    @NotNull CustomComponentType<? extends HolderType, ? extends CustomComponent<HolderType>> getType();
}
