package me.udnek.itemscoreu.customplayerdata;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomPlayerDatabase {

    private static final HashMap<String, CustomPlayerData> database = new HashMap<String, CustomPlayerData>();

    public static CustomPlayerData getData(Player player){
        String uuid = player.getUniqueId().toString();
        return getData(uuid);
    }

    private static CustomPlayerData getData(String uuid){
        if (database.containsKey(uuid)){
            return database.get(uuid);
        }
        CustomPlayerData customPlayerData = new CustomPlayerData();
        database.put(uuid, customPlayerData);
        return customPlayerData;

    }

    public static String toDebugString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, CustomPlayerData> entry : database.entrySet()) {
            String uuid = entry.getKey();
            result.append('\n').append(uuid).append(" ("+Bukkit.getPlayer(UUID.fromString(uuid)).getName() + ")");
            result.append('\n').append(entry.getValue().toString());
        }
        return result.toString();
    }


    public static void printToConsole(){
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("CustomPlayerDatabase:");
        String[] lines = CustomPlayerDatabase.toDebugString().split("\n");
        for (String line : lines) {
            Bukkit.getLogger().info(line);
        }
    }

}
