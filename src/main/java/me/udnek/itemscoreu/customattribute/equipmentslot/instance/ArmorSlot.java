package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlot;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class ArmorSlot extends CustomEquipmentSlot {

    @Override
    public boolean isAppropriateSlot(Entity entity, int slot) {
        return switch (slot) {
            case 39, 38, 37, 36 -> true;
            default -> false;
        };
    }

    @Override
    public @NotNull String translationKey() {
        return "item.modifiers.armor";
    }

    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.ARMOR;}

    @Override
    public int[] getAllSlots(Entity entity) {
        return new int[]{39, 38, 37, 36};
    }
}
