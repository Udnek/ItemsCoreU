package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customregistry.Registrable;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.entity.Entity;

public interface CustomAttribute extends Translatable, Registrable {
    double getDefaultValue();
    double getMinimum();
    double getMaximum();
    double calculate(Entity entity);
}
