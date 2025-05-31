package me.udnek.coreu.rpgu.component.ability;

import me.udnek.coreu.custom.component.AbstractComponentHolder;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot;
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot;
import me.udnek.coreu.rpgu.component.RPGUComponentTypes;
import me.udnek.coreu.rpgu.component.ability.property.AbilityProperty;
import me.udnek.coreu.util.Either;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAbility<ActivationContext> extends AbstractComponentHolder<Ability<?>, AbilityProperty<?, ?>> implements Ability<ActivationContext> {

    public void activate(@NotNull CustomItem customItem,
                         @NotNull LivingEntity livingEntity,
                         boolean canselIfCooldown,
                         @NotNull Either<UniversalInventorySlot, SingleSlot> slot,
                         @NotNull ActivationContext activationContext)
    {
        if (!(livingEntity instanceof Player player)) {
            action(customItem, livingEntity, slot, activationContext);
            return;
        }
        if (customItem.hasCooldown(player)) {
            if (canselIfCooldown && activationContext instanceof Cancellable cancellable){
                cancellable.setCancelled(true);
            }
            return;
        }
        ActionResult result = action(customItem, player, slot, activationContext);
        if (result == ActionResult.FULL_COOLDOWN || result == ActionResult.PENALTY_COOLDOWN){
            double cooldown = getComponents().getOrDefault(RPGUComponentTypes.ABILITY_COOLDOWN).get(player);
            if (result == ActionResult.PENALTY_COOLDOWN) cooldown = cooldown * getComponents().getOrDefault(RPGUComponentTypes.ABILITY_MISS_USAGE_COOLDOWN_MULTIPLIER).get(player);
            if (cooldown > 0) customItem.setCooldown(player, (int) cooldown);
        }
    }
    @Override
    public void activate(@NotNull CustomItem customItem, @NotNull LivingEntity livingEntity, @NotNull Either<UniversalInventorySlot, SingleSlot> slot, @NotNull ActivationContext activationContext){
        activate(customItem, livingEntity, false, slot, activationContext);
    }

    protected abstract @NotNull ActionResult action(@NotNull CustomItem customItem, @NotNull LivingEntity livingEntity,
                                                 @NotNull Either<UniversalInventorySlot, SingleSlot> slot, @NotNull ActivationContext activationContext);

    public enum ActionResult {
        FULL_COOLDOWN,
        PENALTY_COOLDOWN,
        NO_COOLDOWN
    }
}
