package me.udnek.itemscoreu.customattribute;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;

public abstract class CustomAttribute implements Serializable {

    protected String id;
    protected CustomAttribute(){}
    final void register(JavaPlugin javaPlugin){
        id = new NamespacedKey(javaPlugin, getRawId()).asString();
    }
    protected final String getId(){return id;}
    public double getDefaultValue() {return 0;}
    public abstract String getRawId();

/*    public double getDefaultValue(ItemStack itemStack){
        return getDefaultValue(itemStack.getType());
    }
    public double getDefaultValue(Material material){
        return getDefaultValue();
    }
    public double getDefaultValue(Entity entity){
        return getDefaultValue(entity.getType());
    }
    public double getDefaultValue(EntityType entityType){
        return getDefaultValue();
    }*/

}
