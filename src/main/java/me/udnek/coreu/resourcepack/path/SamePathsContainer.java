package me.udnek.coreu.resourcepack.path;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SamePathsContainer {
    private final List<RpPath> paths = new ArrayList<>();
    public SamePathsContainer(@NotNull RpPath rpPathA, @NotNull RpPath rpPathB){
        Preconditions.checkArgument(rpPathA.isSame(rpPathB), "Paths are not same!");
        paths.add(rpPathA);
        paths.add(rpPathB);
    }

    public boolean canAdd(@NotNull RpPath rpPath){
        return paths.getFirst().isSame(rpPath);
    }

    public void add(@NotNull RpPath rpPath){
        Preconditions.checkArgument(canAdd(rpPath), "Can not add " + rpPath + " to container with sames: " + this);
        paths.add(rpPath);
    }

    public @NotNull List<RpPath> getMostPrioritized(){
        Preconditions.checkArgument(!paths.isEmpty(), "paths is empty!");
        ArrayList<RpPath> list = new ArrayList<>(paths);
        list.sort(Comparator.comparingInt(RpPath::getPriorityValue));
        int priority = list.getLast().getPriorityValue();
        return list.stream().filter((Predicate<RpPath>) input -> input.getPriorityValue() == priority).toList();
    }

    public @NotNull List<RpPath> getAll(){
        return new ArrayList<>(paths);
    }

    public @NotNull RpPath getExample(){
        return paths.getFirst();
    }

    @Override
    public String toString() {
        return "SamePathsContainer{" +
                "paths=" + paths +
                '}';
    }
}
