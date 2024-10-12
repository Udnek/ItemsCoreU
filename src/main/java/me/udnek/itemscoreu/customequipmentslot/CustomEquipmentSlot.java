package me.udnek.itemscoreu.customequipmentslot;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customequipmentslot.instance.ConstructableGroupSlot;
import me.udnek.itemscoreu.customequipmentslot.instance.ConstructableSingleSlot;
import me.udnek.itemscoreu.customequipmentslot.instance.MainHandSlot;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.customregistry.Registrable;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CustomEquipmentSlot extends Translatable, Registrable, Predicate<CustomEquipmentSlot> {

    SingleSlot MAIN_HAND = (SingleSlot) register(new MainHandSlot());
    SingleSlot OFF_HAND = (SingleSlot) register(new ConstructableSingleSlot("offhand", 40, EquipmentSlotGroup.OFFHAND, "item.modifiers.offhand"));
    GroupSlot HAND = (GroupSlot) register(new ConstructableGroupSlot("hand", Set.of(
            MAIN_HAND,
            OFF_HAND),
            EquipmentSlotGroup.HAND, "item.modifiers.hand"));

    SingleSlot HEAD = (SingleSlot) register(new ConstructableSingleSlot("head", 39, EquipmentSlotGroup.HEAD, "item.modifiers.head"));
    SingleSlot CHEST = (SingleSlot) register(new ConstructableSingleSlot("chest", 38, EquipmentSlotGroup.CHEST, "item.modifiers.chest"));
    SingleSlot LEGS = (SingleSlot) register(new ConstructableSingleSlot("legs", 37, EquipmentSlotGroup.LEGS, "item.modifiers.legs"));
    SingleSlot FEET = (SingleSlot) register(new ConstructableSingleSlot("feet", 36, EquipmentSlotGroup.FEET, "item.modifiers.feet"));

    GroupSlot ARMOR = (GroupSlot) register(new ConstructableGroupSlot("armor", Set.of(
            HEAD,
            CHEST,
            LEGS,
            FEET),
    EquipmentSlotGroup.ARMOR, "item.modifiers.armor"));

    @Override
    boolean test(@NotNull CustomEquipmentSlot slot);
    boolean isAppropriateSlot(@NotNull Entity entity, int slot);
    @Nullable EquipmentSlotGroup getVanillaAlternative();
    void getAllSlots(@NotNull Entity entity, @NotNull Consumer<@NotNull Integer> consumer);

    private static CustomEquipmentSlot register(CustomEquipmentSlot slot){
        return CustomRegistries.EQUIPMENT_SLOT.register(ItemsCoreU.getInstance(), slot);
    }
}
