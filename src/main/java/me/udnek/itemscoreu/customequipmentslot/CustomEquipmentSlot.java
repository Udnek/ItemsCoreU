package me.udnek.itemscoreu.customequipmentslot;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customequipmentslot.instance.ConstructableGroupSlot;
import me.udnek.itemscoreu.customequipmentslot.instance.ConstructableSingleSlot;
import me.udnek.itemscoreu.customequipmentslot.instance.MainHandSlot;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.customregistry.Registrable;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CustomEquipmentSlot extends Translatable, Registrable, Predicate<CustomEquipmentSlot> {

    SingleSlot MAIN_HAND = (SingleSlot) register(new MainHandSlot());
    SingleSlot OFF_HAND = (SingleSlot) register(new ConstructableSingleSlot("offhand", 40, EquipmentSlotGroup.OFFHAND, EquipmentSlot.OFF_HAND, "item.modifiers.offhand"));
    GroupSlot HAND = (GroupSlot) register(new ConstructableGroupSlot("hand", Set.of(
            MAIN_HAND,
            OFF_HAND),
            EquipmentSlotGroup.HAND,
            null,
            "item.modifiers.hand"));

    SingleSlot HEAD = (SingleSlot) register(new ConstructableSingleSlot("head", 39, EquipmentSlotGroup.HEAD, EquipmentSlot.HEAD, "item.modifiers.head"));
    SingleSlot CHEST = (SingleSlot) register(new ConstructableSingleSlot("chest", 38, EquipmentSlotGroup.CHEST, EquipmentSlot.CHEST, "item.modifiers.chest"));
    SingleSlot LEGS = (SingleSlot) register(new ConstructableSingleSlot("legs", 37, EquipmentSlotGroup.LEGS, EquipmentSlot.LEGS, "item.modifiers.legs"));
    SingleSlot FEET = (SingleSlot) register(new ConstructableSingleSlot("feet", 36, EquipmentSlotGroup.FEET, EquipmentSlot.FEET, "item.modifiers.feet"));

    GroupSlot ARMOR = (GroupSlot) register(new ConstructableGroupSlot("armor", Set.of(
            HEAD,
            CHEST,
            LEGS,
            FEET),
            EquipmentSlotGroup.ARMOR,
            null,
            "item.modifiers.armor"));

    GroupSlot ANY = (GroupSlot) register(new ConstructableGroupSlot("any", Set.of(
            MAIN_HAND,
            OFF_HAND,
            HEAD,
            CHEST,
            LEGS,
            FEET
    ), EquipmentSlotGroup.ANY, null, "item.modifiers.any"));

    static @NotNull CustomEquipmentSlot getFromVanilla(@NotNull EquipmentSlot slot){
        return switch (slot){
            case HEAD -> HEAD;
            case CHEST -> CHEST;
            case LEGS -> LEGS;
            case FEET -> FEET;
            case HAND -> HAND;
            case BODY -> ARMOR;
            case OFF_HAND -> OFF_HAND;
        };
    }
    static @NotNull CustomEquipmentSlot getFromVanilla(@NotNull EquipmentSlotGroup slot){
        if (slot == EquipmentSlotGroup.HEAD) return HEAD;
        if (slot == EquipmentSlotGroup.CHEST) return CHEST;
        if (slot == EquipmentSlotGroup.LEGS) return LEGS;
        if (slot == EquipmentSlotGroup.FEET) return FEET;

        if (slot == EquipmentSlotGroup.HAND) return HAND;
        if (slot == EquipmentSlotGroup.OFFHAND) return OFF_HAND;
        if (slot == EquipmentSlotGroup.ARMOR) return ARMOR;
        if (slot == EquipmentSlotGroup.BODY) return ARMOR;
        return ANY;
    }

    @Override
    boolean test(@NotNull CustomEquipmentSlot slot);
    boolean isAppropriateSlot(@NotNull Entity entity, int slot);
    @Nullable EquipmentSlotGroup getVanillaGroup();
    @Deprecated
    @Nullable EquipmentSlot getVanillaSlot();
    void getAllSlots(@NotNull Entity entity, @NotNull Consumer<@NotNull Integer> consumer);

    private static CustomEquipmentSlot register(CustomEquipmentSlot slot){
        return CustomRegistries.EQUIPMENT_SLOT.register(ItemsCoreU.getInstance(), slot);
    }
}
