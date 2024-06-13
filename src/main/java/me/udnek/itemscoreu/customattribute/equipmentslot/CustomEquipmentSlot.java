package me.udnek.itemscoreu.customattribute.equipmentslot;

import net.kyori.adventure.translation.Translatable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlotGroup;

public abstract class CustomEquipmentSlot implements Translatable {
    protected CustomEquipmentSlot(){}
    public abstract boolean isAppropriateSlot(Entity entity, int slot);
    public EquipmentSlotGroup getVanillaAlternative(){return null;}

    public abstract int[] getAllSlots(Entity entity);
}
