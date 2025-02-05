package me.udnek.itemscoreu.resourcepack;

import me.udnek.itemscoreu.serializabledata.SerializableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourcepackSettings implements SerializableData {

    private String extractDirectory;

    public ResourcepackSettings(@Nullable String extractDirectory){
        this.extractDirectory = extractDirectory;
    }

    public String getExtractDirectory() {
        return extractDirectory;
    }

    @Override
    public @NotNull String serialize() {
        return "extract_directory="+extractDirectory;
    }
    @Override
    public void deserialize(@Nullable String data) {
        if (data == null) return;
        extractDirectory = data.substring(data.indexOf('=')+1);
    }
    @Override
    public @NotNull String getDataName() {
        return "resourcepack_settings";
    }
}
