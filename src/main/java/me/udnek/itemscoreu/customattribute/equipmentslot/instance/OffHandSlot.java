package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;

public class OffHandSlot extends CustomEquipmentSlot {
    @Override
    public boolean isAppropriateSlot(Entity entity, int slot) {
        if (entity instanceof Player) return slot == 40;
        return slot == 98;
    }
    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.OFFHAND;}
}
