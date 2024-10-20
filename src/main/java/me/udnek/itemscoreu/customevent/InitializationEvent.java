package me.udnek.itemscoreu.customevent;

import me.udnek.itemscoreu.util.InitializationProcess;
import org.jetbrains.annotations.NotNull;

public class InitializationEvent extends CustomEvent{

    protected final InitializationProcess.Step step;

    public InitializationEvent(@NotNull InitializationProcess.Step step){
        this.step = step;
    }

    public InitializationProcess.Step getStep() {return step;}
}
