package me.udnek.itemscoreu.customequipmentslot;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface SingleSlot extends CustomEquipmentSlot{
    @Nullable Integer getSlot(@NotNull Entity entity);

    @Override
    default void getAllSlots(@NotNull Entity entity, @NotNull Consumer<@NotNull Integer> consumer){
        Integer slot = getSlot(entity);
        if (slot != null) consumer.accept(slot);
    }

    @Override
    default boolean test(@NotNull CustomEquipmentSlot slot) {
        return slot == this;
    }
}
