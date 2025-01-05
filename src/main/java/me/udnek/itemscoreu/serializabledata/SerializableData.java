package me.udnek.itemscoreu.serializabledata;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SerializableData{
    @NotNull String serialize();
    void deserialize(@Nullable String data);

    @NotNull String getDataName();
}
