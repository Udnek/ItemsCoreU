package me.udnek.itemscoreu.customequipmentslot;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface GroupSlot extends CustomEquipmentSlot{

    void getAllSubSlots(@NotNull Consumer<SingleSlot> consumer);
}
