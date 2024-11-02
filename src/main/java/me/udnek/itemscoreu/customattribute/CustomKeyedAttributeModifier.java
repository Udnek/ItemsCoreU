package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class CustomKeyedAttributeModifier extends CustomAttributeModifier implements Keyed {
    protected final NamespacedKey key;
    public CustomKeyedAttributeModifier(@NotNull NamespacedKey key, double amount, @NotNull AttributeModifier.Operation operation, @NotNull CustomEquipmentSlot equipmentSlot) {
        super(amount, operation, equipmentSlot);
        this.key = key;
    }

    public @NotNull AttributeModifier toVanilla(){
        return toVanilla(key);
    }

    public @NotNull AttributeModifier toVanillaWitAdjustedKey(@NotNull String string){
        return toVanilla(new NamespacedKey(key.getNamespace(), key + string));
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }
}
