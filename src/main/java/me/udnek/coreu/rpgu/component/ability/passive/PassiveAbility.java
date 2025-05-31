package me.udnek.coreu.rpgu.component.ability.passive;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot;
import me.udnek.coreu.rpgu.component.ability.Ability;
import me.udnek.coreu.rpgu.component.PassiveAbilityItem;
import org.jetbrains.annotations.NotNull;

public interface PassiveAbility<ActivationContext> extends Ability<ActivationContext>, CustomComponent<PassiveAbilityItem> {
    @NotNull CustomEquipmentSlot getSlot();
}
