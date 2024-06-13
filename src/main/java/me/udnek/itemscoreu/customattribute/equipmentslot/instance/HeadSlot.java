package me.udnek.itemscoreu.customattribute.equipmentslot.instance;

import me.udnek.itemscoreu.customattribute.equipmentslot.SoloSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class HeadSlot extends SoloSlot {
    @Override
    public int getSlot() {return 39;}

    @Override
    public @NotNull String translationKey() {
        return "item.modifiers.head";
    }
    @Override
    public EquipmentSlotGroup getVanillaAlternative() {return EquipmentSlotGroup.HEAD;}
}
