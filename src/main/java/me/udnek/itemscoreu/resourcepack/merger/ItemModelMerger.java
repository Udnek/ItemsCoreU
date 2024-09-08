package me.udnek.itemscoreu.resourcepack.merger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;

public class ItemModelMerger {

    public static final String CUSTOM_MODEL_DATA = "custom_model_data";
    public static final String OVERRIDES = "overrides";
    public static final String PREDICATE = "predicate";

    private final List<JsonObject> jsons = new ArrayList<>();
    private JsonObject merged = null;

    public ItemModelMerger(){}
    public void add(@NotNull JsonObject jsonObject){
        jsons.add(jsonObject);
    }
    public void add(@NotNull BufferedReader reader){
        jsons.add((JsonObject) JsonParser.parseReader(reader));
    }

    public void merge(){
        List<JsonObject> allOverrides = new ArrayList<>();

        for (JsonObject json : jsons) {
            JsonArray overrides = json.getAsJsonArray(OVERRIDES);
            for (JsonElement override : overrides) {
                if (containsSame(allOverrides, override)) {
                    continue;
                }
                allOverrides.add(override.getAsJsonObject());
            }
        }
        allOverrides.sort(new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject o1, JsonObject o2) {
                JsonObject pA = o1.getAsJsonObject(PREDICATE);
                JsonObject pB = o2.getAsJsonObject(PREDICATE);

                //if (isBiggerCMD(pA, pB)) return -1;

                Set<String> keys = new HashSet<>(pA.asMap().keySet());
                keys.addAll(pB.asMap().keySet());

                for (String key : keys) {
                    JsonElement vA = pA.get(key);
                    JsonElement vB = pB.get(key);
                    if (vA == null) return -1;
                    if (vB == null) return 1;
                    int compare = Integer.compare(vA.getAsInt(), vB.getAsInt());
                    if (compare == 0) continue;
                    return compare;
                }

                return 0;
            }

/*            public boolean isBiggerCMD(JsonObject pA, JsonObject pB){
                JsonElement cmdA = pA.get(CUSTOM_MODEL_DATA);
                if (cmdA == null) return false;
                JsonElement cmdB = pB.get(CUSTOM_MODEL_DATA);
                if (cmdB == null) return true;
                return cmdA.getAsInt() > cmdB.getAsInt();
            }*/
        });


        JsonObject mainJson = jsons.get(0).deepCopy();
        mainJson.remove(OVERRIDES);
        JsonArray overridesJson = new JsonArray();

        for (JsonObject override : allOverrides) {
            overridesJson.add(override);
        }

        mainJson.add(OVERRIDES, overridesJson);
        merged = mainJson;
    }


    private static boolean containsSame(List<JsonObject> jsons, JsonElement element){
        for (JsonObject json : jsons) {
            if (json.equals(element)) return true;
        }
        return false;
    }

    public String getMergedAsString() {
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
