package me.udnek.itemscoreu.customplayerdata.datatypes;

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
