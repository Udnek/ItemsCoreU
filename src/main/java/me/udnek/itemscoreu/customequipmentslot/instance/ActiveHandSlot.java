package me.udnek.itemscoreu.customequipmentslot.instance;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customequipmentslot.AbstractCustomEquipmentSlot;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customequipmentslot.SingleSlot;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActiveHandSlot extends AbstractCustomEquipmentSlot implements SingleSlot {
    public ActiveHandSlot(@NotNull String id) {super(id);}

    @Override
    public @Nullable Integer getSlot(@NotNull LivingEntity entity) {
        if (!entity.isHandRaised()) return null;
        return ((SingleSlot) CustomEquipmentSlot.getFromVanilla(entity.getActiveItemHand())).getSlot(entity);
    }

    @Override
    public @Nullable EquipmentSlotGroup getVanillaGroup() {return null;}

    @Override
    public @Nullable EquipmentSlot getVanillaSlot() {return null;}

    @Override
    public @NotNull String translationKey() {
        return "slot." + new NamespacedKey(ItemsCoreU.getInstance(),"text").getNamespace() + ".active_hand";
    }
}
