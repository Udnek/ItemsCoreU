package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlot;
import org.bukkit.attribute.AttributeModifier;

public class CustomAttributeModifier {

    protected final double amount;
    protected final AttributeModifier.Operation operation;
    protected final CustomEquipmentSlot equipmentSlot;

    public CustomAttributeModifier(double amount, AttributeModifier.Operation operation, CustomEquipmentSlot equipmentSlot){
        this.amount = amount;
        this.operation = operation;
        this.equipmentSlot = equipmentSlot;
    }
    public double getAmount() {return amount;}
    public AttributeModifier.Operation getOperation() {return operation;}
    public CustomEquipmentSlot getEquipmentSlot() {return equipmentSlot;}
}
