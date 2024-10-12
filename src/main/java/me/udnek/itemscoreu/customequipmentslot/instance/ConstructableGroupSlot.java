package me.udnek.itemscoreu.customequipmentslot.instance;

import me.udnek.itemscoreu.customequipmentslot.AbstractCustomEquipmentSlot;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customequipmentslot.GroupSlot;
import me.udnek.itemscoreu.customequipmentslot.SingleSlot;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

public class ConstructableGroupSlot extends AbstractCustomEquipmentSlot implements GroupSlot {
    protected final String translation;
    protected final EquipmentSlotGroup vanilla;
    protected final Set<SingleSlot> subs;

    public ConstructableGroupSlot(@NotNull String rawId, @NotNull Set<@NotNull SingleSlot> subs, @Nullable EquipmentSlotGroup vanilla, @NotNull String translation) {
        super(rawId);
        this.translation = translation;
        this.vanilla = vanilla;
        this.subs = subs;
    }
    @Override
    public boolean isAppropriateSlot(@NotNull Entity entity, int slot) {
        return subs.stream().anyMatch(sub -> sub.isAppropriateSlot(entity, slot));
    }
    @Override
    public @Nullable EquipmentSlotGroup getVanillaAlternative() {
        return vanilla;
    }
    @Override
    public void getAllSlots(@NotNull Entity entity, @NotNull Consumer<@NotNull Integer> consumer) {
        subs.forEach(singleSlot -> singleSlot.getAllSlots(entity, consumer));
    }
    @Override
    public @NotNull String translationKey() {
        return translation;
    }
    @Override
    public void getAllSubSlots(@NotNull Consumer<SingleSlot> consumer) {
        subs.forEach(consumer);
    }

    @Override
    public boolean test(@NotNull CustomEquipmentSlot slot) {
        return slot == this || subs.stream().anyMatch(s -> s == slot);
    }
}
