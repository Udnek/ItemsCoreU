package me.udnek.itemscoreu.resourcepack;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.resourcepack.path.RpPath;
import me.udnek.itemscoreu.resourcepack.path.VirtualRpJsonFile;
import me.udnek.itemscoreu.util.LogUtils;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VirtualResourcePack {

    private static final String ROOT_PATH = "resourcepack";

    protected final ResourcePackablePlugin plugin;
    protected List<RpPath> files = new ArrayList<>();

    public VirtualResourcePack(@NotNull ResourcePackablePlugin plugin){
        this.plugin = plugin;
    }

    public void initialize(){
        LogUtils.pluginLog("ResourcePack "+ plugin.getName() +" initialization started");
        RpPath rootPath = new RpPath(this, "");
        files = rootPath.findFiles();
        LogUtils.pluginLog("ResourcePack files:");
        for (RpPath file : files) {
            LogUtils.pluginLog(file);
        }
        LogUtils.pluginLog("adding missing:");
        for (CustomItem item : CustomRegistries.ITEM.getAllByPlugin(plugin)) {
            NamespacedKey namespacedKey = item.getKey();
            // item def
            RpPath path = new RpPath(this, "assets/" + namespacedKey.getNamespace() + "/items/" + namespacedKey.getKey() + ".json");
            if (!files.contains(path)){
                LogUtils.pluginLog(path);
                VirtualRpJsonFile virtualRpJsonFile = new VirtualRpJsonFile(this,
                        """
                                {
                                  "model": {
                                    "type": "minecraft:model",
                                    "model": "%namespace%:item/%key%"
                                  }
                                }
                                """.replace("%namespace%", namespacedKey.getNamespace()).replace("%key%", namespacedKey.getKey()),
                        path.getPath());
                files.add(virtualRpJsonFile);
            }
            // item model
            path = new RpPath(this, "assets/" + namespacedKey.getNamespace() + "/models/item/" + namespacedKey.getKey() + ".json");
            if (!files.contains(path)){
                LogUtils.pluginLog(path);
                VirtualRpJsonFile virtualRpJsonFile = new VirtualRpJsonFile(this,
                        """
                                {
                                    "parent": "minecraft:item/generated",
                                    "textures": {
                                        "layer0": "%namespace%:item/%key%"
                                    }
                                }
                                """.replace("%namespace%", namespacedKey.getNamespace()).replace("%key%", namespacedKey.getKey()),
                        path.getPath());
                files.add(virtualRpJsonFile);
            }
        }
        LogUtils.pluginLog("finished missing");
        LogUtils.pluginLog("ResourcePack "+ plugin.getName() +" initialization ended");
    }

    public @NotNull List<String> getResources(@NotNull RpPath path){
        return FileManager.getAllResources(plugin.getClass(), FileManager.joinPaths(ROOT_PATH, path.getPath()));
    }
    public @NotNull InputStream getInputStream(@NotNull RpPath path){
        return FileManager.getInputStream(plugin.getClass(), FileManager.joinPaths(ROOT_PATH, path.getPath()));
    }
    public boolean isFile(@NotNull RpPath path){
        return FileManager.isFile(plugin.getClass(), FileManager.joinPaths(ROOT_PATH, path.getPath()));
    }
    public boolean isDirectoryEmpty(@NotNull RpPath path){
        return FileManager.isDirectoryEmpty(plugin.getClass(), FileManager.joinPaths(ROOT_PATH, path.getPath()));
    }

    public @NotNull List<RpPath> getAllFoundFiles(){
        return new ArrayList<>(files);
    }

    public @NotNull String getName(){return plugin.getName();}
}














