package me.udnek.itemscoreu.resourcepack.path;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SamePathsContainer {
    private final List<RPPath> paths = new ArrayList<>();
    public SamePathsContainer(@NotNull RPPath rpPathA, @NotNull RPPath rpPathB){
        Preconditions.checkArgument(rpPathA.isSame(rpPathB), "Paths are not same!");
        paths.add(rpPathA);
        paths.add(rpPathB);
    }

    public boolean canAdd(@NotNull RPPath rpPath){
        return paths.get(0).isSame(rpPath) && !paths.contains(rpPath);
    }

    public void add(@NotNull RPPath rpPath){
        if (!canAdd(rpPath)) return;
        paths.add(rpPath);
    }

    public List<RPPath> getAll(){
        return new ArrayList<>(paths);
    }

    public @NotNull RPPath getExample(){
        return paths.get(0);
    }
}
