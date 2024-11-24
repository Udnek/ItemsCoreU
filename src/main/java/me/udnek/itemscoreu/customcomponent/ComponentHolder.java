package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ComponentHolder<HolderType> {
     @NotNull CustomComponentMap<HolderType> getComponents();
}
