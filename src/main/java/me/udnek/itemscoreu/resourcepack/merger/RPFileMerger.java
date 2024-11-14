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

public interface RPFileMerger {
    void add(@NotNull JsonObject jsonObject);
    default void add(@NotNull BufferedReader reader){
       add((JsonObject) JsonParser.parseReader(reader));
    }
    void merge();
    @NotNull String getMergedAsString();
}
