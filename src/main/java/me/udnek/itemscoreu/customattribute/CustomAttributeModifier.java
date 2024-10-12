package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class CustomAttributeModifier {

    protected final double amount;
    protected final AttributeModifier.Operation operation;
    protected final CustomEquipmentSlot equipmentSlot;

    public CustomAttributeModifier(double amount, @NotNull AttributeModifier.Operation operation, @NotNull CustomEquipmentSlot equipmentSlot){
        this.amount = amount;
        this.operation = operation;
        this.equipmentSlot = equipmentSlot;
    }
    public double getAmount() {return amount;}
    public @NotNull AttributeModifier.Operation getOperation() {return operation;}
    public @NotNull CustomEquipmentSlot getEquipmentSlot() {return equipmentSlot;}
}
