package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;

public class MainHandSlot extends CustomEquipmentSlot {
    // TODO: 6/8/2024 TEST IF IT IS EVENT WORKING
    @Override
    public boolean isAppropriateSlot(Entity entity, int slot) {
        if (entity instanceof Player player) return player.getInventory().getHeldItemSlot() == slot;
        return slot == 98;
    }
    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.MAINHAND;}

    @Override
    public int[] getAllSlots(Entity entity) {
        if (entity instanceof Player player){
            return new int[]{player.getInventory().getHeldItemSlot()};
        }
        return new int[]{98};
    }
}
