package me.udnek.itemscoreu.customequipmentslot.instance;

import me.udnek.itemscoreu.customequipmentslot.AbstractCustomEquipmentSlot;
import me.udnek.itemscoreu.customequipmentslot.SingleSlot;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ConstructableSingleSlot extends AbstractCustomEquipmentSlot implements SingleSlot {

    protected final int slot;
    protected final String translation;
    protected final EquipmentSlotGroup vanillaGroup;
    protected final EquipmentSlot vanillaSlot;

    public ConstructableSingleSlot(@NotNull String rawId, int slot, @Nullable EquipmentSlotGroup vanillaGroup, @Nullable EquipmentSlot vanillaSlot, @NotNull String translation){
        super(rawId);
        this.slot = slot;
        this.translation = translation;
        this.vanillaGroup = vanillaGroup;
        this.vanillaSlot = vanillaSlot;
    }
    @Override
    public @NotNull Integer getSlot(@NotNull LivingEntity entity){return slot;}
    @Override
    public @Nullable EquipmentSlotGroup getVanillaGroup() {
        return vanillaGroup;
    }
    @Override
    public @Nullable EquipmentSlot getVanillaSlot() {return vanillaSlot;}
    @Override
    public void getAllSlots(@NotNull LivingEntity entity, @NotNull Consumer<@NotNull Integer> consumer) {
        consumer.accept(getSlot(entity));
    }
    @Override
    public @NotNull String translationKey() {return translation;}
}
