package me.udnek.itemscoreu.customenchantment;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public abstract class CustomEnchantment extends Enchantment {

/*    protected void setNamespacedKey(JavaPlugin javaPlugin){
        Class<?> clazz = this.getClass();
        while (true){
            if (clazz.getName().equals(Enchantment.class.getName())){
                break;
            }
            clazz = clazz.getSuperclass();
        }
        try {
            Field field = clazz.getDeclaredField("key");
            field.setAccessible(true);
            field.set(this, new NamespacedKey(javaPlugin, getName()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomEnchantment() {
        super();
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public @NotNull Component displayName(int i) {
        return  Component.translatable("enchantment."+getKey().getKey()+"."+getName()).append(Component.text(" ")).append(Component.translatable("enchantment.level." + i));
    }*/
}
