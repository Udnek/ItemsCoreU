package me.udnek.coreu.rpgu.component.ability.property.function;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MultiLineDescription {

    protected List<Component> components = new ArrayList<>();

    public MultiLineDescription(){

    }

    public @NotNull MultiLineDescription addToBeginning(@NotNull Component component){
        if (components.isEmpty()) components.add(Component.empty());
        components.set(0, component.append(components.getFirst()));
        return this;
    }
    public @NotNull MultiLineDescription add(@NotNull Component component){
        if (components.isEmpty()) components.add(Component.empty());
        components.set(components.size()-1, components.getLast().append(component));
        return this;
    }

    public @NotNull MultiLineDescription addLineToBeginning(@NotNull Component component){
        components.addFirst(component);
        return this;
    }
    public @NotNull MultiLineDescription addLine(@NotNull Component component){
        components.add(component);
        return this;
    }

    public @NotNull Component join(){
        TextComponent join = Component.empty();
        for (Component component : components) join = join.append(component);
        return join;
    }
    
    public @NotNull List<Component> getComponents() {return components;}
}
