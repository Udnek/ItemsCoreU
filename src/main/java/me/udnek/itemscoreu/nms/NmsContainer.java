package me.udnek.itemscoreu.nms;

import com.nimbusds.jose.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class NmsContainer<T> implements Supplier<T> {

    protected T supply;

    public NmsContainer(@NotNull T supply){
        this.supply = supply;
    }

    @Override
    public T get() {
        return supply;
    }

    @Override
    public String toString() {
        String val;
        if (get() == null) val = "null";
        else val = get().toString();
        return getClass() +"[" + val +"]";
    }
}
