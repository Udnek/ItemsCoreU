package me.udnek.coreu.resourcepack;

import me.udnek.coreu.serializabledata.SerializableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourcepackInfo implements SerializableData {

    public @Nullable String extractDirectory;
    public @Nullable String checksum;

    public ResourcepackInfo(@NotNull String extractDirectory, @NotNull String checksum){
        this.extractDirectory = extractDirectory;
        this.checksum = checksum;
    }

    public ResourcepackInfo(){}

    @Override
    public @NotNull String serialize() {
        return extractDirectory+";"+checksum;
    }
    @Override
    public void deserialize(@Nullable String data) {
        if (data == null) return;
        String[] split = data.split(";");
        extractDirectory = split[0];
        checksum = split[1];
    }
    @Override
    public @NotNull String getDataName() {
        return "resourcepack_settings";
    }
}
