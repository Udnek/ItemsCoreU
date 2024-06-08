package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.SoloSlot;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;

public class FeetSlot extends SoloSlot {
    @Override
    public int getSlot() {return 36;}
    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.FEET;}
}
