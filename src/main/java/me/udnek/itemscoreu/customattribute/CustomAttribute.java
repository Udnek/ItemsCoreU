package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customregistry.Registrable;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface CustomAttribute extends Translatable, Registrable {
    double getDefaultValue();
    double getMinimum();
    double getMaximum();
    double calculate(@NotNull LivingEntity entity);
}
