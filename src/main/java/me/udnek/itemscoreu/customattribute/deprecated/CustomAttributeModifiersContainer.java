package me.udnek.itemscoreu.customattribute.deprecated;

import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class CustomAttributeModifiersContainer {
    private List<CustomAttributeModifier> attributeModifiers = new ArrayList<>();


    public CustomAttributeModifiersContainer(){}

    public CustomAttributeModifiersContainer(CustomAttributeModifier customAttributeModifier){
        this.add(customAttributeModifier);
    }

    public CustomAttributeModifiersContainer get(EquipmentSlot equipmentSlot){
        CustomAttributeModifiersContainer customAttributeModifiersContainer = new CustomAttributeModifiersContainer();
        for (CustomAttributeModifier customAttributeModifier : attributeModifiers) {
            if (customAttributeModifier.getEquipmentSlot() == equipmentSlot){
                customAttributeModifiersContainer.add(customAttributeModifier);
            }
        }
        return customAttributeModifiersContainer;
    }

    public void add(CustomAttributeModifier customAttributeModifier){
        attributeModifiers.add(customAttributeModifier);
    }

    public void add(CustomAttributeModifiersContainer container){
        for (CustomAttributeModifier customAttributeModifier : container.getAll()) {
            add(customAttributeModifier);
        }
    }

    public List<CustomAttributeModifier> getAll(){
        return attributeModifiers;
    }

    public boolean isEmpty(){
        return attributeModifiers.isEmpty();
    }
}
