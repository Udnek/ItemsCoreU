package me.udnek.itemscoreu.serializabledata;

import me.udnek.itemscoreu.ItemsCoreU;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SerializableDataManager {

    private static FileConfiguration config;
    private static final String PLUGIN_PATH = "plugin";
    private static final String PLAYER_PATH = "player";

    ///////////////////////////////////////////////////////////////////////////
    // READING
    ///////////////////////////////////////////////////////////////////////////
    public static void read(@NotNull SerializableData data, @NotNull Plugin plugin, @NotNull Player player){
        String readRaw = readRaw(getPath(data, plugin, player));
        data.deserialize(readRaw);
    }
    public static void read(@NotNull SerializableData data, @NotNull Plugin plugin){
        String readRaw = readRaw(getPath(data, plugin));
        data.deserialize(readRaw);
    }
    private static String readRaw(@NotNull String path){
        return config.getString(path);
    }
    ///////////////////////////////////////////////////////////////////////////
    // WRITING
    ///////////////////////////////////////////////////////////////////////////
    public static void write(@NotNull SerializableData data, @NotNull Plugin plugin, @NotNull Player player){
        writeRaw(data, getPath(data, plugin, player));
    }
    public static void write(@NotNull SerializableData data, @NotNull Plugin plugin){
        writeRaw(data, getPath(data, plugin));
    }
    private static void writeRaw(@NotNull SerializableData serializableData, @NotNull String path){
        config.set(path, serializableData.serialize());
    }
    ///////////////////////////////////////////////////////////////////////////
    // INITIAL
    ///////////////////////////////////////////////////////////////////////////
    private static String getPath(@NotNull SerializableData data, @NotNull Plugin plugin, @NotNull Player player){
        return toStringPath(
                PLAYER_PATH,
                player.getUniqueId().toString(),
                plugin.getName() + ":" + data.getDataName()
        );
    }
    private static @NotNull String getPath(@NotNull SerializableData data, @NotNull Plugin plugin){
        return toStringPath(
                PLUGIN_PATH,
                plugin.getName(),
                data.getDataName()
        );
    }

    private static @NotNull String toStringPath(@NotNull String ...path){
        return String.join(".", path);
    }

    public static void loadConfig() {config = ItemsCoreU.getInstance().getConfig();}
    public static void saveConfig() {ItemsCoreU.getInstance().saveConfig();}
}
