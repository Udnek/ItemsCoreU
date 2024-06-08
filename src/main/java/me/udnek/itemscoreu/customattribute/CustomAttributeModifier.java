package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlot;
import org.bukkit.attribute.AttributeModifier;

public class CustomAttributeModifier {

    private final double amount;
    private final AttributeModifier.Operation operation;
    private final CustomEquipmentSlot equipmentSlot;

    public CustomAttributeModifier(double amount, AttributeModifier.Operation operation, CustomEquipmentSlot equipmentSlot){
        this.amount = amount;
        this.operation = operation;
        this.equipmentSlot = equipmentSlot;
    }
    public double getAmount() {return amount;}
    public AttributeModifier.Operation getOperation() {return operation;}
    public CustomEquipmentSlot getEquipmentSlot() {return equipmentSlot;}
}
