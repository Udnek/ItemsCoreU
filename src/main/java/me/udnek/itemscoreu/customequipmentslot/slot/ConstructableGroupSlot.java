package me.udnek.itemscoreu.customequipmentslot.slot;

import me.udnek.itemscoreu.customequipmentslot.universal.UniversalInventorySlot;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

public class ConstructableGroupSlot extends AbstractCustomEquipmentSlot implements GroupSlot {
    protected final String translation;
    protected final EquipmentSlotGroup vanillaGroup;
    private final EquipmentSlot vanillaSlot;
    protected final Set<SingleSlot> subs;

    public ConstructableGroupSlot(@NotNull String rawId, @NotNull Set<@NotNull SingleSlot> subs, @Nullable EquipmentSlotGroup vanillaGroup, @Nullable EquipmentSlot vanillaSlot, @NotNull String translation) {
        super(rawId);
        this.translation = translation;
        this.vanillaGroup = vanillaGroup;
        this.vanillaSlot = vanillaSlot;
        this.subs = subs;
    }
    @Override
    public @Nullable EquipmentSlotGroup getVanillaGroup() {
        return vanillaGroup;
    }

    @Override
    @Nullable
    public EquipmentSlot getVanillaSlot() {return vanillaSlot;}

    @Override
    public void getAllUniversal(@NotNull Consumer<@NotNull UniversalInventorySlot> consumer) {
        subs.forEach(singleSlot -> singleSlot.getAllUniversal(consumer));
    }

    @Override
    public @NotNull String translationKey() {
        return translation;
    }
    @Override
    public void getAllSingle(@NotNull Consumer<@NotNull SingleSlot> consumer) {
        subs.forEach(consumer);
    }

    @Override
    public boolean intersects(@NotNull CustomEquipmentSlot other) {
        return other == this || subs.stream().anyMatch(s -> other == s);
    }
}














