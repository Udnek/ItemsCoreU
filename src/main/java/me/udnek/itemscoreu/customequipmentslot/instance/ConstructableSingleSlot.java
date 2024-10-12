package me.udnek.itemscoreu.customequipmentslot.instance;

import me.udnek.itemscoreu.customequipmentslot.AbstractCustomEquipmentSlot;
import me.udnek.itemscoreu.customequipmentslot.SingleSlot;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ConstructableSingleSlot extends AbstractCustomEquipmentSlot implements SingleSlot {

    protected final int slot;
    protected final String translation;
    protected final EquipmentSlotGroup vanilla;

    public ConstructableSingleSlot(@NotNull String rawId, int slot, @Nullable EquipmentSlotGroup vanilla, @NotNull String translation){
        super(rawId);
        this.slot = slot;
        this.translation = translation;
        this.vanilla = vanilla;

    }
    @Override
    public @NotNull Integer getSlot(@NotNull Entity entity){return slot;}
    @Override
    public boolean isAppropriateSlot(@NotNull Entity entity, int slot) {return slot == getSlot(entity);}
    @Override
    public @Nullable EquipmentSlotGroup getVanillaAlternative() {
        return vanilla;
    }
    @Override
    public void getAllSlots(@NotNull Entity entity, @NotNull Consumer<@NotNull Integer> consumer) {
        consumer.accept(getSlot(entity));
    }
    @Override
    public @NotNull String translationKey() {return translation;}
}
