package me.udnek.itemscoreu.resourcepack.path;

import me.udnek.itemscoreu.util.LogUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SortedPathsContainer {

    private final List<RPPath> all = new ArrayList<>();
    private final List<SamePathsContainer> sames = new ArrayList<>();

    public SortedPathsContainer(){}

    public void add(@NotNull RPPath newPath){
        if (all.contains(newPath)) return;

        for (SamePathsContainer container : sames) {
            if (container.canAdd(newPath)){
                //LogUtils.pluginWarning("Same path + " + newPath + " added to container");
                all.add(newPath);
                container.add(newPath);
                return;
            }
        }
        for (RPPath path : all) {
            if (path.isSame(newPath)){
                //LogUtils.pluginWarning("Found new same paths: " + newPath + ", " + path);
                sames.add(new SamePathsContainer(path, newPath));
                all.add(newPath);
                return;
            }
        }
        //LogUtils.pluginLog("Added path " + newPath);
        all.add(newPath);
    }

    public List<SamePathsContainer> getSames() {
        return sames;
    }

    public void debug(){
        LogUtils.pluginLog("Paths container:");
        LogUtils.pluginLog("Total: " + all.size());
        LogUtils.pluginLog("Same:");
        for (SamePathsContainer samePathsContainer : sames) {
            for (RPPath path : samePathsContainer.getAll()) {
                LogUtils.pluginLog(path);
            }
            LogUtils.pluginLog("----------");
        }
    }

    public List<RPPath> getAllExcludingSame(){
        List<RPPath> toExclude = new ArrayList<>();
        for (RPPath path : all) {
            for (SamePathsContainer same : sames) {
                if (same.getExample().isSame(path)) {
                    LogUtils.pluginLog("Excluding: " + path );
                    toExclude.add(path);
                    break;
                }
            }
        }
        all.removeAll(toExclude);
        return all;
    }


}
