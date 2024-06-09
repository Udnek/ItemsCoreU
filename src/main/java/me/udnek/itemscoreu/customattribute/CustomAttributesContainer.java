package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlot;
import org.bukkit.attribute.AttributeModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAttributesContainer{

    private final HashMap<CustomAttribute, List<CustomAttributeModifier>> attributes = new HashMap<>();

    private CustomAttributesContainer(){}

    public static CustomAttributesContainer empty(){return new CustomAttributesContainer();}
    public List<CustomAttributeModifier> get(CustomAttribute customAttribute){return new ArrayList<>(attributes.get(customAttribute));}
    public Map<CustomAttribute, List<CustomAttributeModifier>> getAll(){return new HashMap<>(attributes);}
    public CustomAttributesContainer get(CustomEquipmentSlot slot){
        CustomAttributesContainer newContainer = new CustomAttributesContainer();
        for (Map.Entry<CustomAttribute, List<CustomAttributeModifier>> entry : attributes.entrySet()) {
            CustomAttribute attribute = entry.getKey();
            for (CustomAttributeModifier modifier : entry.getValue()) {
                if (modifier.getEquipmentSlot() != slot) continue;
                newContainer.add(attribute, modifier);
            }

        }
        return newContainer;
    }
    public boolean contains(CustomAttribute customAttribute){
        return attributes.containsKey(customAttribute);
    }
    private void add(CustomAttribute attribute, CustomAttributeModifier modifier){
        List<CustomAttributeModifier> modifiers = attributes.get(attribute);
        if (modifiers == null){
            modifiers = new ArrayList<>();
            modifiers.add(modifier);
            attributes.put(attribute, modifiers);
            return;
        }
        modifiers.add(modifier);
    }

    public static class Builder{

        private CustomAttributesContainer container;
        public Builder(){
            container = new CustomAttributesContainer();
        }
        public Builder add(CustomAttribute customAttribute, double amount, AttributeModifier.Operation operation, CustomEquipmentSlot slot){
            CustomAttributeModifier attributeModifier = new CustomAttributeModifier(amount, operation, slot);
            return add(customAttribute, attributeModifier);
        }

        public Builder add(CustomAttribute customAttribute, CustomAttributeModifier attributeModifier){
            container.add(customAttribute, attributeModifier);
            return this;
        }

        public CustomAttributesContainer build(){
            return container;
        }

    }

}
