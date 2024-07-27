package me.udnek.itemscoreu.resourcepack;

import me.udnek.itemscoreu.serializabledata.SerializableData;

public class ResourcepackSettings implements SerializableData {

    private String extractDirectory;

    public ResourcepackSettings(String extractDirectory){
        this.extractDirectory = extractDirectory;
    }

    public String getExtractDirectory() {
        return extractDirectory;
    }

    @Override
    public String serialize() {
        return "extract_directory="+extractDirectory;
    }
    @Override
    public void deserialize(String data) {
        if (data == null) return;
        extractDirectory = data.substring(data.indexOf('=')+1);
    }
    @Override
    public String getDataName() {
        return "resourcepack_settings";
    }
}
