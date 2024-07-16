package me.udnek.itemscoreu.serializabledata;

public interface SerializableData{
    String serialize();
    void deserialize(String data);

    String getDataName();
}
