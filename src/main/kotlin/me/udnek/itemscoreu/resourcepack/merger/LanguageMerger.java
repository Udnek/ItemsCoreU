package me.udnek.itemscoreu.resourcepack.merger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LanguageMerger implements RpFileMerger {
    private final List<JsonObject> jsons = new ArrayList<>();
    private JsonObject merged = null;

    public LanguageMerger(){}
    @Override
    public void add(@NotNull JsonObject jsonObject){
        jsons.add(jsonObject);
    }

    @Override
    public void merge(){
        merged = new JsonObject();
        for (JsonObject json : jsons) {
            for (Map.Entry<String, JsonElement> entry : json.asMap().entrySet()) {
                merged.addProperty(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }


    @Override
    public @NotNull String getMergedAsString() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(merged.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String prettyJson;
        try {
            prettyJson = objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return prettyJson;
    }
}
