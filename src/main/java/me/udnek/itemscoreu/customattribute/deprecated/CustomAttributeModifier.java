package me.udnek.itemscoreu.customattribute.deprecated;

import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.io.Serializable;

@Deprecated
public class CustomAttributeModifier implements Serializable{

    private double amount;
    private EquipmentSlot equipmentSlot;
    private AttributeModifier.Operation operation;

    public CustomAttributeModifier(double amount, EquipmentSlot equipmentSlot, AttributeModifier.Operation operation){
        this.amount = amount;
        this.equipmentSlot = equipmentSlot;
        this.operation = operation;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public void setEquipmentSlot(EquipmentSlot equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
    }

    public AttributeModifier.Operation getOperation() {
        return operation;
    }

    public void setOperation(AttributeModifier.Operation operation) {
        this.operation = operation;
    }
}
