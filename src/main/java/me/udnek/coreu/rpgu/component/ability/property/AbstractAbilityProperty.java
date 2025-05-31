package me.udnek.coreu.rpgu.component.ability.property;

import me.udnek.coreu.rpgu.component.ability.property.function.PropertyFunction;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAbilityProperty<Context, Value> implements AbilityProperty<Context, Value> {

    protected @NotNull PropertyFunction<Context, Value> function;

    public AbstractAbilityProperty(@NotNull PropertyFunction<Context, Value> function){
        this.function = function;
    }

    public @NotNull PropertyFunction<Context, Value> getFunction() {
        return function;
    }

    @Override
    public @NotNull Value getBase() {return function.getBase();}

}
