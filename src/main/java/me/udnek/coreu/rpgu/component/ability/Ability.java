package me.udnek.coreu.rpgu.component.ability;

import me.udnek.coreu.custom.component.ComponentHolder;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot;
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot;
import me.udnek.coreu.rpgu.component.ability.property.AbilityProperty;
import me.udnek.coreu.util.Either;
import me.udnek.coreu.util.LoreBuilder;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface Ability<ActivationContext> extends ComponentHolder<Ability<?>, AbilityProperty<?, ?>> {
    void activate(@NotNull CustomItem customItem,
                  @NotNull LivingEntity livingEntity,
                  @NotNull Either<UniversalInventorySlot, SingleSlot> slot,
                  @NotNull ActivationContext activationContext);

    default void getLore(@NotNull LoreBuilder loreBuilder) {}
}
