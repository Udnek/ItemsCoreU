package me.udnek.coreu.resourcepack;

import me.udnek.coreu.serializabledata.SerializableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RPInfo implements SerializableData {

    public @Nullable String extractDirectory;
    public @Nullable String checksum_zip;
    public @Nullable String checksum_folder;
    public @NotNull String ip = "127.0.0.1";
    public int port = 25566;

    public RPInfo(@NotNull String extractDirectory, @NotNull String checksum){
        this.extractDirectory = extractDirectory;
        this.checksum_zip = checksum;
    }

    public RPInfo(){}

    @Override
    public @NotNull String serialize() {
        return SerializableData.serializeMap(Map.of(
                "extract_directory", extractDirectory == null ? "null": extractDirectory ,
                "checksum_zip", checksum_zip == null ? "null": checksum_zip,
                "checksum_folder", checksum_folder == null ? "null": checksum_folder,
                "ip", ip,
                "port", port));
    }
    @Override
    public void deserialize(@Nullable String data) {
        if (data == null) return;
        Map<String, Object> map = SerializableData.deserializeMap(data);
        extractDirectory = map.get("extract_directory").toString();
        checksum_zip = map.get("checksum_zip").toString();
        checksum_folder = map.get("checksum_folder").toString();
        ip = map.get("ip").toString();
        port = Integer.parseInt(map.get("port").toString());
    }
    @Override
    public @NotNull String getDataName() {
        return "resourcepack_settings";
    }
}
