package me.udnek.itemscoreu.customattribute;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface CustomAttributeConsumer extends BiConsumer<CustomAttribute, CustomAttributeModifier> {
    void accept(@NotNull CustomAttribute attribute, @NotNull CustomAttributeModifier modifier);
}
