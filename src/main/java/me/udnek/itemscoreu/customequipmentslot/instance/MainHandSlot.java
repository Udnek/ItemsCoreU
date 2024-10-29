package me.udnek.itemscoreu.customequipmentslot.instance;

import me.udnek.itemscoreu.customequipmentslot.AbstractCustomEquipmentSlot;
import me.udnek.itemscoreu.customequipmentslot.SingleSlot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MainHandSlot extends AbstractCustomEquipmentSlot implements SingleSlot {


    public MainHandSlot() {
        super("main_hand");
    }

    // TODO: 6/8/2024 TEST IF IT IS EVENT WORKING
    @Override
    public boolean isAppropriateSlot(@NotNull Entity entity, int slot) {
        if (entity instanceof Player player) return player.getInventory().getHeldItemSlot() == slot;
        return slot == 98;
    }

    @Override
    public @NotNull String translationKey() {
        return "item.modifiers.mainhand";
    }

    @Override
    public EquipmentSlotGroup getVanillaGroup() {return EquipmentSlotGroup.MAINHAND;}

    @Override
    public @Nullable EquipmentSlot getVanillaSlot() {return EquipmentSlot.HAND;}

    @Override
    public void getAllSlots(@NotNull Entity entity, @NotNull Consumer<@NotNull Integer> consumer) {
        consumer.accept(getSlot(entity));
    }

    @Override
    public @NotNull Integer getSlot(@NotNull Entity entity) {
        if (entity instanceof Player player) return player.getInventory().getHeldItemSlot();
        else return 98;
    }
}
