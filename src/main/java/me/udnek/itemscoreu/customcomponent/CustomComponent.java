package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;

public interface CustomComponent<HolderType, Type extends CustomComponentType<HolderType, ?>> {
    @NotNull Type getType();
}
