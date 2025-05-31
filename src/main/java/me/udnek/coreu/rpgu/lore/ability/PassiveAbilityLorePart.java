package me.udnek.coreu.rpgu.lore.ability;

import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot;
import org.jetbrains.annotations.NotNull;

public interface PassiveAbilityLorePart extends AbilityLorePart{

    void setEquipmentSlot(@NotNull CustomEquipmentSlot slot);
}
