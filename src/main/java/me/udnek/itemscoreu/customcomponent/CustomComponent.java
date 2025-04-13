package me.udnek.itemscoreu.customcomponent;

import org.jetbrains.annotations.NotNull;

public interface CustomComponent<HolderType> {

    default void throwCanNotChangeDefault(){
        throw new RuntimeException("Can not change default component: " + this + ", create default or apply new firstly");
    }

    @NotNull CustomComponentType<? extends HolderType, ? extends CustomComponent<HolderType>> getType();
}
