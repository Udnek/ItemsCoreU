package me.udnek.coreu.custom.attribute;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface CustomAttributeConsumer extends BiConsumer<CustomAttribute, CustomAttributeModifier> {
    void accept(@NotNull CustomAttribute attribute, @NotNull CustomAttributeModifier modifier);
}
