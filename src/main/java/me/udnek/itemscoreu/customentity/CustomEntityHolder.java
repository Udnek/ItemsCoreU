package me.udnek.itemscoreu.customentity;

import org.bukkit.entity.Entity;

public class CustomEntityHolder {

    public final Entity realEntity;
    public final CustomEntity customEntity;
    CustomEntityHolder(Entity realEntity, CustomEntity customEntity){
        this.realEntity = realEntity;
        this.customEntity = customEntity;
    }
}
