package me.udnek.itemscoreu.resourcepack;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customevent.ResourcepackInitializationEvent;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.resourcepack.path.RpPath;
import me.udnek.itemscoreu.resourcepack.path.VirtualRpJsonFile;
import me.udnek.itemscoreu.util.LogUtils;
import net.kyori.adventure.key.Key;
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
        LogUtils.pluginLog("AutoAdding:");
        List<VirtualRpJsonFile> toAdd = new ArrayList<>();
        for (CustomItem item : CustomRegistries.ITEM.getAllByPlugin(plugin)) {
            toAdd.addAll(item.getComponents().getOrDefault(CustomComponentType.AUTO_GENERATING_FILES_ITEM).getFiles(item));
        }
        ResourcepackInitializationEvent event = new ResourcepackInitializationEvent(this);
        event.callEvent();
        toAdd.addAll(event.getFiles());
        for (VirtualRpJsonFile file : toAdd) {
            if (files.contains(file)) continue;
            VirtualRpJsonFile pluginFile = new VirtualRpJsonFile(this, file.getData(), file.getPath());
            LogUtils.pluginLog(pluginFile);
            files.add(pluginFile);
        }
        LogUtils.pluginLog("finished AutoAdding");

        LogUtils.pluginLog("ResourcePack "+ plugin.getName() +" initialization ended");
    }

    public @NotNull ResourcePackablePlugin getPlugin() {
        return plugin;
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














