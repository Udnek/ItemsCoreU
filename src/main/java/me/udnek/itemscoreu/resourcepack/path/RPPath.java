package me.udnek.itemscoreu.resourcepack.path;

import me.udnek.itemscoreu.resourcepack.FileManager;
import me.udnek.itemscoreu.resourcepack.VirtualResourcePack;
import me.udnek.itemscoreu.resourcepack.filetype.FileType;
import me.udnek.itemscoreu.utils.LogUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RPPath {

    protected final VirtualResourcePack resourcePack;
    protected final String path;

    public RPPath(VirtualResourcePack resourcePack, String path){
        this.resourcePack = resourcePack;
        this.path = FileManager.removeSlashes(path);
    }

    public RPPath withAdded(String added){
        String newPath = FileManager.joinPaths(path, added);
        return new RPPath(resourcePack, newPath);
    }

    public RPPath withOneLevelUp(){
        String newPath = FileManager.oneLayerUp(path);
        return new RPPath(resourcePack, newPath);
    }

    public @NotNull List<RPPath> findFiles(){
        LogUtils.pluginLog(this);
        LogUtils.pluginLog("Checking is file");
        if (resourcePack.isFile(this)) return List.of(this);
        LogUtils.pluginLog("Checking is empty directory");
        if (resourcePack.isDirectoryEmpty(this)) return List.of();

        LogUtils.pluginLog("Checking for subdirectories");
        List<String> resources = resourcePack.getResources(this);
        LogUtils.pluginLog("Found resources: " + resources);
        List<RPPath> subFiles = new ArrayList<>();
        for (String resource : resources) {
            subFiles.addAll(withAdded(resource).findFiles());
        }
        return subFiles;
    }

    public String getPath() {
        return path;
    }

    public @NotNull FileType getFileType(){
        return FileType.get(path);
    }
    @Override
    public String toString() {
        return "RPPath{" + resourcePack.getName() + ", " + path + "}";
    }
}
