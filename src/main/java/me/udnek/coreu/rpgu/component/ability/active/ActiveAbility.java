package me.udnek.coreu.rpgu.component.ability.active;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot;
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot;
import me.udnek.coreu.rpgu.component.ability.Ability;
import me.udnek.coreu.rpgu.component.ActiveAbilityItem;
import me.udnek.coreu.util.Either;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ActiveAbility<ActivationContext> extends Ability<ActivationContext>, CustomComponent<ActiveAbilityItem> {
    void activate(@NotNull CustomItem customItem, @NotNull Player player, @NotNull Either<UniversalInventorySlot, SingleSlot> slot, @NotNull ActivationContext context);
}
