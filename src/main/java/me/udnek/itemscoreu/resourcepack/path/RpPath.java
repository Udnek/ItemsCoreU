package me.udnek.itemscoreu.resourcepack.path;

import me.udnek.itemscoreu.resourcepack.FileManager;
import me.udnek.itemscoreu.resourcepack.FileType;
import me.udnek.itemscoreu.resourcepack.VirtualResourcePack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RpPath {

    protected final VirtualResourcePack resourcePack;
    protected final String path;

    public RpPath(@Nullable VirtualResourcePack resourcePack, @NotNull String path){
        this.resourcePack = resourcePack;
        this.path = FileManager.removeSlashes(path);
    }

    public @NotNull RpPath withAdded(@NotNull String added){
        String newPath = FileManager.joinPaths(path, added);
        return new RpPath(resourcePack, newPath);
    }

    public @NotNull RpPath withRenamedLast(@NotNull String newName){
        if (path.isEmpty()) return this;
        return withLayerUp().withAdded(newName);
    }

    public @NotNull RpPath withLayerUp(){
        String newPath = FileManager.layerUp(path);
        return new RpPath(resourcePack, newPath);
    }

    public @NotNull RpPath withMergeId(@Nullable Integer mergeId){
        if (mergeId == null) return this;
        return withRenamedLast("(MANUAL_MERGE_" + resourcePack.getName() + "_" + mergeId + ")" + getLast());
    }

    public @NotNull List<RpPath> findFiles(){
        //LogUtils.pluginLog(this);
        //LogUtils.pluginLog("Checking is file");
        if (resourcePack.isFile(this)) return List.of(this);
        //LogUtils.pluginLog("Checking is empty directory");
        if (resourcePack.isDirectoryEmpty(this)) return List.of();

        //LogUtils.pluginLog("Checking for subdirectories");
        List<String> resources = resourcePack.getResources(this);
        //LogUtils.pluginLog("Found resources: " + resources);
        List<RpPath> subFiles = new ArrayList<>();
        for (String resource : resources) {
            subFiles.addAll(withAdded(resource).findFiles());
        }
        return subFiles;
    }

    public boolean isBelow(@NotNull RpPath other){
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

    public @NotNull InputStream getInputStream(){
        return resourcePack.getInputStream(this);
    }

    public @NotNull String getExtractPath(@NotNull String extract){
        return FileManager.joinPaths(extract, path);
    }

    public boolean isSame(@NotNull RpPath other){
        return this.path.equals(other.path);
    }
}
