package me.udnek.itemscoreu.resourcepack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum FileType {

    PNG("png"),
    JSON("json"),
    MCMETA("mcmeta"),
    UNKNOWN(null);

    public final @Nullable String extension;
    FileType(@Nullable String extension){
        this.extension = extension;
    }
    public static @NotNull FileType get(String path){
        for (FileType value : values()) {
            if (value.extension == null) continue;
            if (path.endsWith("."+value.extension)) return value;
        }
        return UNKNOWN;
    }
}
