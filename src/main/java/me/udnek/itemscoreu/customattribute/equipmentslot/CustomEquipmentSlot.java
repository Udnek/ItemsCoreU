package me.udnek.itemscoreu.customattribute.equipmentslot;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;

public abstract class CustomEquipmentSlot {
    protected CustomEquipmentSlot(){};
    public abstract boolean isAppropriateSlot(Entity entity, int slot);
    public EquipmentSlotGroup getVanillaAlternative(){return null;}
}
