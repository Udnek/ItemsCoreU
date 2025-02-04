package me.udnek.itemscoreu.customattribute;

import org.jetbrains.annotations.NotNull;

public interface CustomAttributeConsumer {
    void accept(@NotNull CustomAttribute attribute, @NotNull CustomAttributeModifier modifier);
}
