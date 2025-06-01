package me.udnek.coreu.resourcepack;

import me.udnek.coreu.serializabledata.SerializableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RPInfo implements SerializableData {

    public @Nullable String extractDirectory;
    public @Nullable String checksum;
    public int port = 25566;

    public RPInfo(@NotNull String extractDirectory, @NotNull String checksum){
        this.extractDirectory = extractDirectory;
        this.checksum = checksum;
    }

    public RPInfo(){}

    @Override
    public @NotNull String serialize() {
        return SerializableData.serializeMap(Map.of(
                "extract_directory", extractDirectory == null ? "null": extractDirectory ,
                "checksum", checksum == null ? "null": checksum,
                "port", port));
    }
    @Override
    public void deserialize(@Nullable String data) {
        if (data == null) return;
        Map<String, Object> map = SerializableData.deserializeMap(data);
        extractDirectory = map.get("extract_directory").toString();
        checksum = map.get("checksum").toString();
        port = Integer.parseInt(map.get("port").toString());
    }
    @Override
    public @NotNull String getDataName() {
        return "resourcepack_settings";
    }
}
