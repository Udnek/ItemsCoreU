package me.udnek.itemscoreu.customequipmentslot.instance;

import me.udnek.itemscoreu.customequipmentslot.AbstractCustomEquipmentSlot;
import me.udnek.itemscoreu.customequipmentslot.SingleSlot;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimalBodySlot extends AbstractCustomEquipmentSlot implements SingleSlot {
    public AnimalBodySlot(@NotNull String id) {
        super(id);
    }

    @Override
    public @Nullable Integer getSlot(@NotNull LivingEntity entity) {
        return null;
    }

    @Override
    public @Nullable EquipmentSlotGroup getVanillaGroup() {
        return EquipmentSlotGroup.BODY;
    }

    @Override
    public @Nullable EquipmentSlot getVanillaSlot() {
        return EquipmentSlot.BODY;
    }

    @Override
    public @NotNull String translationKey() {
        return "item.modifiers.body";
    }
}
