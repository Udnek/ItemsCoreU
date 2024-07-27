package me.udnek.itemscoreu.resourcepack;

import me.udnek.itemscoreu.resourcepack.path.RPPath;
import me.udnek.itemscoreu.utils.LogUtils;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VirtualResourcePack {

    private static final String ROOT_PATH = "resourcepack";

    protected final ResourcePackablePlugin plugin;
    protected List<RPPath> files = new ArrayList<>();

    public VirtualResourcePack(ResourcePackablePlugin plugin){
        this.plugin = plugin;
    }

    public void initialize(){
        LogUtils.pluginLog("ResourcePack "+ plugin.getName() +" initialization started");
        RPPath rootPath = new RPPath(this, "");
        files = rootPath.findFiles();
        LogUtils.pluginLog("ResourcePack files:");
        for (RPPath file : files) {
            LogUtils.pluginLog(file);
        }
        LogUtils.pluginLog("ResourcePack "+ plugin.getName() +" initialization ended");
    }


    public @NotNull List<String> getRootResources(){
        return FileManager.getAllResources(plugin.getClass(), ROOT_PATH);
    }
    public @NotNull List<String> getResources(@NotNull RPPath path){
        return FileManager.getAllResources(plugin.getClass(), FileManager.joinPaths(ROOT_PATH, path.getPath()));
    }
    public @NotNull InputStream getFile(@NotNull RPPath path){
        return FileManager.getFile(plugin.getClass(), FileManager.joinPaths(ROOT_PATH, path.getPath()));
    }
    public boolean isFile(@NotNull RPPath path){
        return FileManager.isFile(plugin.getClass(), FileManager.joinPaths(ROOT_PATH, path.getPath()));
    }
    public boolean isDirectoryEmpty(@NotNull RPPath path){
        return FileManager.isDirectoryEmpty(plugin.getClass(), FileManager.joinPaths(ROOT_PATH, path.getPath()));
    }

    public List<RPPath> getAllFoundFiles(){
        return new ArrayList<>(files);
    }

    public String getName(){return plugin.getName();}
}














