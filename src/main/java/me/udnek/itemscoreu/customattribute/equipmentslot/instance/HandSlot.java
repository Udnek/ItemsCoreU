package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class HandSlot extends CustomEquipmentSlot {

    @Override
    public boolean isAppropriateSlot(Entity entity, int slot) {
        if (entity instanceof Player player) return slot == 40 || slot == player.getInventory().getHeldItemSlot();
        return slot == 98 || slot == 99;
    }
    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.HAND;}

    @Override
    public @NotNull String translationKey() {
        return "item.modifiers.hand";
    }

    @Override
    public int[] getAllSlots(Entity entity) {
        if (entity instanceof Player player){
            return new int[]{player.getInventory().getHeldItemSlot(), 40};
        }
        return new int[]{98, 99};
    }

}
