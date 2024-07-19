package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.utils.PluginInitializable;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class CustomAttribute implements Serializable, Translatable, PluginInitializable {

    protected String id;
    protected CustomAttribute(){}
    @Override
    public final void initialize(@NotNull JavaPlugin javaPlugin){
        id = new NamespacedKey(javaPlugin, getRawId()).asString();
    }
    protected final String getId(){return id;}
    public double getDefaultValue() {return 0;}
    public abstract String getRawId();

    public double calculate(Entity entity){
        return CustomAttributeUtils.calculate(this, entity);
    }

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
