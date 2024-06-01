package me.udnek.itemscoreu.customattribute;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;

public abstract class CustomAttributeType implements Serializable {

    protected NamespacedKey namespacedKey;

    protected CustomAttributeType(){}

    void register(JavaPlugin javaPlugin){
        namespacedKey = new NamespacedKey(javaPlugin, getName());
    }

    protected String getId(){
        return getNamespacedKey().asString();
    }

/*    protected CustomAttributeType getInstance(){
        return CustomAttributeManager.getInstance(this);
    }*/

    protected NamespacedKey getNamespacedKey(){return namespacedKey;}

    public abstract double getDefaultValue();
    public abstract String getName();

    public double getDefaultValue(ItemStack itemStack){
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
    }


    ///////////////////////////////////////////////////////////////////////////
    // ITEM
    ///////////////////////////////////////////////////////////////////////////

/*    public void set(ItemMeta itemMeta, double value){
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(getNamespacedKey(), PersistentDataType.DOUBLE, value);
    }
    public void set(ItemStack itemStack, double value){
        ItemMeta itemMeta = itemStack.getItemMeta();
        set(itemMeta, value);
        itemStack.setItemMeta(itemMeta);
    }

    public double get(ItemMeta itemMeta){
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        if (dataContainer.has(getNamespacedKey(), PersistentDataType.DOUBLE)) {
            return dataContainer.get(getNamespacedKey(), PersistentDataType.DOUBLE);
        }
        return getDefaultValue();
    }

    public double get(ItemStack itemStack){
        if (!itemStack.hasItemMeta()) return getDefaultValue();
        ItemMeta itemMeta = itemStack.getItemMeta();
        return get(itemMeta);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ENTITY
    ///////////////////////////////////////////////////////////////////////////

    public static void set(Entity entity, double value){
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        dataContainer.set(getNamespacedKey(), PersistentDataType.DOUBLE, value);
    }

    public static double get(Entity entity){
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        if (dataContainer.has(getNamespacedKey(), PersistentDataType.DOUBLE)) {
            return dataContainer.get(getNamespacedKey(), PersistentDataType.DOUBLE);
        }
        return getDefaultValue();
    }*/


}
