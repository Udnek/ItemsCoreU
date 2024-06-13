package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.SoloSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class LegsSlot extends SoloSlot {
    @Override
    public int getSlot() {return 37;}
    @Override
    public @NotNull String translationKey() {
        return "item.modifiers.legs";
    }
    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.LEGS;}
}
