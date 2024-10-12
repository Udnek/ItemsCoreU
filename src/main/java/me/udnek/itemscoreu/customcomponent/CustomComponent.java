package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;

public interface CustomComponent<HolderType> {
    @NotNull CustomComponentType<HolderType, ?> getType();
}
