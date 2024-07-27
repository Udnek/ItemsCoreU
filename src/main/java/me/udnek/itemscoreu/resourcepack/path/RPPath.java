package me.udnek.itemscoreu.resourcepack.path;

import me.udnek.itemscoreu.resourcepack.FileManager;
import me.udnek.itemscoreu.resourcepack.FileType;
import me.udnek.itemscoreu.resourcepack.VirtualResourcePack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RPPath {

    protected final VirtualResourcePack resourcePack;
    protected final String path;

    public RPPath(@Nullable VirtualResourcePack resourcePack, @NotNull String path){
        this.resourcePack = resourcePack;
        this.path = FileManager.removeSlashes(path);
    }

    public @NotNull RPPath withAdded(@NotNull String added){
        String newPath = FileManager.joinPaths(path, added);
        return new RPPath(resourcePack, newPath);
    }

    public @NotNull RPPath withRenamedLast(@NotNull String newName){
        if (path.isEmpty()) return this;
        return withLayerUp().withAdded(newName);
    }

    public @NotNull RPPath withLayerUp(){
        String newPath = FileManager.layerUp(path);
        return new RPPath(resourcePack, newPath);
    }

    public @NotNull RPPath withMergeId(@Nullable Integer mergeId){
        if (mergeId == null) return this;
        return withRenamedLast("(MANUAL_MERGE_" + resourcePack.getName() + "_" + mergeId + ")" + getLast());
    }

    public @NotNull List<RPPath> findFiles(){
        //LogUtils.pluginLog(this);
        //LogUtils.pluginLog("Checking is file");
        if (resourcePack.isFile(this)) return List.of(this);
        //LogUtils.pluginLog("Checking is empty directory");
        if (resourcePack.isDirectoryEmpty(this)) return List.of();

        //LogUtils.pluginLog("Checking for subdirectories");
        List<String> resources = resourcePack.getResources(this);
        //LogUtils.pluginLog("Found resources: " + resources);
        List<RPPath> subFiles = new ArrayList<>();
        for (String resource : resources) {
            subFiles.addAll(withAdded(resource).findFiles());
        }
        return subFiles;
    }

    public boolean isBelow(@NotNull RPPath other){
        return  (path.startsWith(other.path));
    }

    public @NotNull String getPath() {
        return path;
    }
    public @NotNull String getLast(){
        if (path.isEmpty()) return "";
        int i = path.lastIndexOf("/");
        if (i == -1) return path;
        return path.substring(i);
    }

    public @NotNull FileType getFileType(){
        return FileType.get(path);
    }
    @Override
    public @NotNull String toString() {
        return "RPPath{ " + path + " (" + resourcePack.getName() + ") }";
    }

    public @NotNull InputStream getFile(){
        return resourcePack.getFile(this);
    }

    public @NotNull String getExtractPath(@NotNull String extract){
        return FileManager.joinPaths(extract, path);
    }

    public boolean isSame(@NotNull RPPath other){
        return this.path.equals(other.path);
    }
}
