package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.SoloSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class ChestSlot extends SoloSlot {
    @Override
    public int getSlot() {return 38;}

    @Override
    public @NotNull String translationKey() {
        return "item.modifiers.chest";
    }

    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.CHEST;}
}
