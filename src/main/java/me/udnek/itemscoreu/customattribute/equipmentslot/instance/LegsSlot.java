package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.SoloSlot;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;

public class LegsSlot extends SoloSlot {
    @Override
    public int getSlot() {return 37;}
    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.LEGS;}
}
