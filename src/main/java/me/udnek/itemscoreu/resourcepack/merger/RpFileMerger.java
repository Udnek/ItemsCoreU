package me.udnek.itemscoreu.resourcepack.merger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public interface RpFileMerger {
    void add(@NotNull JsonObject jsonObject);
    default void add(@NotNull BufferedReader reader){
       add((JsonObject) JsonParser.parseReader(reader));
    }
    void merge();
    @NotNull String getMergedAsString();
}
