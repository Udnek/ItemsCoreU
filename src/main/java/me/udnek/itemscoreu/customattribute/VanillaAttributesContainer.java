package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VanillaAttributesContainer {

    private final HashMap<Attribute, List<CustomKeyedAttributeModifier>> attributes = new HashMap<>();

    private VanillaAttributesContainer(){}
    public static VanillaAttributesContainer empty(){return new VanillaAttributesContainer();}
    public List<CustomAttributeModifier> get(Attribute attribute){return new ArrayList<>(attributes.get(attribute));}
    public Map<Attribute, List<CustomKeyedAttributeModifier>> getAll(){return new HashMap<>(attributes);}
    public VanillaAttributesContainer get(CustomEquipmentSlot slot){
        VanillaAttributesContainer newContainer = new VanillaAttributesContainer();
        for (Map.Entry<Attribute, List<CustomKeyedAttributeModifier>> entry : attributes.entrySet()) {
            Attribute attribute = entry.getKey();
            for (CustomKeyedAttributeModifier modifier : entry.getValue()) {
                if (modifier.getEquipmentSlot() != slot) continue;
                newContainer.add(attribute, modifier);
            }

        }
        return newContainer;
    }
    public boolean isEmpty(){
        return attributes.isEmpty();
    }
    public boolean contains(Attribute attribute){
        return attributes.containsKey(attribute);
    }
    private void add(Attribute attribute, CustomKeyedAttributeModifier modifier){
        List<CustomKeyedAttributeModifier> modifiers = attributes.get(attribute);
        if (modifiers == null){
            modifiers = new ArrayList<>();
            modifiers.add(modifier);
            attributes.put(attribute, modifiers);
            return;
        }
        modifiers.add(modifier);
    }

    public static class Builder{

        private final VanillaAttributesContainer container;
        public Builder(){
            container = new VanillaAttributesContainer();
        }
        public VanillaAttributesContainer.Builder add(Attribute attribute, NamespacedKey key, double amount, AttributeModifier.Operation operation, CustomEquipmentSlot slot){
            CustomKeyedAttributeModifier attributeModifier = new CustomKeyedAttributeModifier(key, amount, operation, slot);
            return add(attribute, attributeModifier);
        }

        public VanillaAttributesContainer.Builder add(Attribute attribute, CustomKeyedAttributeModifier attributeModifier){
            container.add(attribute, attributeModifier);
            return this;
        }

        public VanillaAttributesContainer build(){
            return container;
        }

    }
}
