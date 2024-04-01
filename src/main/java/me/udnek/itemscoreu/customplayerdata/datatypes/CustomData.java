package me.udnek.itemscoreu.customplayerdata.datatypes;

import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomData<T>{

    protected T value;

    public CustomData(T value){
        this.value = value;
    }

    public T getValue(){
        return this.value;
    }
    public void setValue(T value){
        this.value = value;
    }
}
