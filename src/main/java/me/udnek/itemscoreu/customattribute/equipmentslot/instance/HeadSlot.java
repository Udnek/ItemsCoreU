package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.SoloSlot;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;

public class HeadSlot extends SoloSlot {
    @Override
    public int getSlot() {return 39;}
    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.HEAD;}
}
