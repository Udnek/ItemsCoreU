package me.udnek.itemscoreu.customattribute.equipmentslot;

import net.kyori.adventure.translation.Translatable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlotGroup;

public abstract class CustomEquipmentSlot implements Translatable {
    protected CustomEquipmentSlot(){}
    public abstract boolean isAppropriateSlot(Entity entity, int slot);
    public EquipmentSlotGroup getVanillaAlternative(){return null;}

    public abstract int[] getAllSlots(Entity entity);

    public static CustomEquipmentSlot getVanillaLikeSlotFromRawSlot(Entity entity, int slot){
        for (CustomEquipmentSlot equipmentSlot : new CustomEquipmentSlot[]
                {
                        CustomEquipmentSlots.HEAD,
                        CustomEquipmentSlots.CHEST,
                        CustomEquipmentSlots.LEGS,
                        CustomEquipmentSlots.FEET,

                        CustomEquipmentSlots.HAND,
                        CustomEquipmentSlots.OFF_HAND,
                }) {

            if (equipmentSlot.isAppropriateSlot(entity, slot)) return equipmentSlot;
        }
        return null;
    }
}
