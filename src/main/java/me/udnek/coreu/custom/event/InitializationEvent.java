package me.udnek.coreu.custom.event;

import me.udnek.coreu.custom.registry.InitializationProcess;
import org.jetbrains.annotations.NotNull;

public class InitializationEvent extends CustomEvent{

    protected final InitializationProcess.Step step;

    public InitializationEvent(@NotNull InitializationProcess.Step step){
        this.step = step;
    }

    public InitializationProcess.Step getStep() {return step;}
}
