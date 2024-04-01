package me.udnek.itemscoreu.customentity;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class CustomDumbEntityHolder {

    public final Entity entity;
    public final CustomDumbTickingEntity customDumbTickingEntity;

    CustomDumbEntityHolder(Entity entity, CustomDumbTickingEntity customDumbTickingEntity){
        this.entity = entity;
        this.customDumbTickingEntity = customDumbTickingEntity;
    }


}
