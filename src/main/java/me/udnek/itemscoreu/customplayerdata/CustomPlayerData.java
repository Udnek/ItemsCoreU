package me.udnek.itemscoreu.customplayerdata;

import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

public class CustomPlayerData {

    private final HashMap<String, Integer> integerDatabase = new HashMap<>();
    private final HashMap<String, String> stringDatabase = new HashMap<>();
    private final HashMap<String, Boolean> booleanDatabase = new HashMap<>();


    public void setValue(NamespacedKey namespacedKey, int value){
        this.integerDatabase.put(namespacedKey.toString(), value);
    }
    public void setValue(NamespacedKey namespacedKey, String value){
        this.stringDatabase.put(namespacedKey.toString(), value);
    }
    public void setValue(NamespacedKey namespacedKey, boolean value){
        this.booleanDatabase.put(namespacedKey.toString(), value);
    }


    public int getIntegerValue(NamespacedKey namespacedKey){
        return this.integerDatabase.getOrDefault(namespacedKey.toString(), null);
    }
    public int getIntegerValueOrDefault(NamespacedKey namespacedKey, int def){
        return this.integerDatabase.getOrDefault(namespacedKey.toString(), def);
    }


    public String getStringValue(NamespacedKey namespacedKey){
        return this.stringDatabase.getOrDefault(namespacedKey.toString(), null);
    }
    public String getStringValueOrDefault(NamespacedKey namespacedKey, String def){
        return this.stringDatabase.getOrDefault(namespacedKey.toString(), def);
    }


    public boolean getBooleanValue(NamespacedKey namespacedKey){
        return this.booleanDatabase.getOrDefault(namespacedKey.toString(), null);
    }
    public boolean getBooleanValueOrDefault(NamespacedKey namespacedKey, boolean def){
        return this.booleanDatabase.getOrDefault(namespacedKey.toString(), def);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("    Integer:");
        for (Map.Entry<String, Integer> entry : this.integerDatabase.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            result.append('\n').append("    "+"    " + key + ": " + value);
        }
        result.append('\n').append("    String:");
        for (Map.Entry<String, String> entry : this.stringDatabase.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            result.append('\n').append("    "+"    " + key + ": " + value);
        }
        result.append('\n').append("    Boolean:");
        for (Map.Entry<String, Boolean> entry : this.booleanDatabase.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            result.append('\n').append("    "+"    " + key + ": " + value);
        }
        return result.toString();
    }
}














