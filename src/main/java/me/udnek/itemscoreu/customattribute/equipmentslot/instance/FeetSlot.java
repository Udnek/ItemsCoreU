package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.SoloSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class FeetSlot extends SoloSlot {
    @Override
    public int getSlot() {return 36;}

    @Override
    public @NotNull String translationKey() {
        return "item.modifiers.feet";
    }
    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.FEET;}
}
