package me.udnek.itemscoreu.customequipmentslot.slot;

import me.udnek.itemscoreu.customequipmentslot.universal.UniversalInventorySlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface SingleSlot extends CustomEquipmentSlot{
    @Nullable UniversalInventorySlot getUniversal();

    @Override
    default void getAllUniversal(@NotNull Consumer<@NotNull UniversalInventorySlot> consumer){
        @Nullable UniversalInventorySlot slot = getUniversal();
        if (slot != null) consumer.accept(slot);
    }

    @Override
    default void getAllSingle(@NotNull Consumer<@NotNull SingleSlot> consumer){
        consumer.accept(this);
    }

    @Override
    default boolean intersects(@NotNull CustomEquipmentSlot other) {
        if (other instanceof SingleSlot) return other == this;
        else return other.intersects(this);
    }
}
