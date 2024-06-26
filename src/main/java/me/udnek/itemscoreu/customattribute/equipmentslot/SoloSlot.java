package me.udnek.itemscoreu.customattribute.equipmentslot;

import org.bukkit.entity.Entity;

public abstract class SoloSlot extends CustomEquipmentSlot {

    public abstract int getSlot();
    @Override
    public boolean isAppropriateSlot(Entity entity, int slot) {return slot == getSlot();}

    @Override
    public int[] getAllSlots(Entity entity) {return new int[]{getSlot()};}
}
