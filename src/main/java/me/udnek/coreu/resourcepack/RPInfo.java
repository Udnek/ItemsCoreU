package me.udnek.coreu.resourcepack;

import me.udnek.coreu.serializabledata.SerializableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RPInfo implements SerializableData {

    public @Nullable String extractDirectory;
    public @Nullable String checksumZipFile;
    public @Nullable String checksumFolder;
    public @NotNull String ip = "127.0.0.1";
    public int port = 25566;

    public RPInfo(@NotNull String extractDirectory, @NotNull String checksum){
        this.extractDirectory = extractDirectory;
        this.checksumZipFile = checksum;
    }

    public RPInfo(){}

    @Override
    public @NotNull String serialize() {
        return SerializableData.serializeMap(Map.of(
                "extract_directory", extractDirectory == null ? "null": extractDirectory ,
                "checksumZipFile", checksumZipFile == null ? "null": checksumZipFile,
                "checksumFolder", checksumFolder == null ? "null": checksumFolder,
                "ip", ip,
                "port", port));
    }
    @Override
    public void deserialize(@Nullable String data) {
        if (data == null) return;
        Map<String, Object> map = SerializableData.deserializeMap(data);
        extractDirectory = map.get("extract_directory").toString();
        checksumZipFile = map.get("checksum").toString();
        checksumFolder = map.get("checksumFolder").toString();
        ip = map.get("ip").toString();
        port = Integer.parseInt(map.get("port").toString());
    }
    @Override
    public @NotNull String getDataName() {
        return "resourcepack_settings";
    }
}
