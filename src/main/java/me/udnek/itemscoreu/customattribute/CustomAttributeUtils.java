package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.CustomAttributableItem;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.utils.CustomItemUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class CustomAttributeUtils {

    // TODO: 2/25/2024 USAGE OF EQUIPMENT SLOT

    public static final NamespacedKey namespacedKey = new NamespacedKey(ItemsCoreU.getInstance(), "attributes");

    ///////////////////////////////////////////////////////////////////////////
    // DATA CONTAINER
    ///////////////////////////////////////////////////////////////////////////

/*    private static void setContainer(CustomAttributeContainer customAttributeContainer, PersistentDataContainer persistentDataContainer){
        persistentDataContainer.set(namespacedKey, new CustomAttributePersistentDataType(), customAttributeContainer);
    }*/

    private static CustomAttributesContainer getContainer(PersistentDataContainer persistentDataContainer){
        if (persistentDataContainer.has(namespacedKey, new CustomAttributePersistentDataType())){
            return persistentDataContainer.get(namespacedKey, new CustomAttributePersistentDataType());
        }
        return new CustomAttributesContainer();
    }

    public static void add(CustomAttributeType type, CustomAttributeModifier modifier, PersistentDataContainer persistentDataContainer){
        CustomAttributesContainer customAttributesContainer = getContainer(persistentDataContainer);
        customAttributesContainer.add(type, modifier);
    }

    public static CustomAttributeModifiersContainer get(CustomAttributeType customAttributeType, PersistentDataContainer persistentDataContainer){
        CustomAttributesContainer customAttributesContainer = getContainer(persistentDataContainer);
        CustomAttributeModifiersContainer customAttributeModifiersContainer = customAttributesContainer.get(customAttributeType);
        if (customAttributeModifiersContainer == null){
            return new CustomAttributeModifiersContainer();
        }
        return customAttributeModifiersContainer;
    }

    public static double calculateFinalValue(double input, CustomAttributeModifiersContainer container){
        double addScalar = 1;
        for (CustomAttributeModifier customAttributeModifier : container.getAll()) {
            double amount = customAttributeModifier.getAmount();
            switch (customAttributeModifier.getOperation()){
                case ADD_NUMBER:
                    input += amount;
                case ADD_SCALAR:
                    addScalar += amount;
                case MULTIPLY_SCALAR_1:
                    input *= amount;
            }
        }
        input *= addScalar;

        return input;
    }


    ///////////////////////////////////////////////////////////////////////////
    // ITEM
    ////////////////////////////////////////////////////////////////////////////*

    // TODO: 3/9/2024 DO

        public static void add(CustomAttributeType customAttribute, CustomAttributeModifier customAttributeModifier, ItemMeta itemMeta){
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            add(customAttribute, customAttributeModifier, dataContainer);
        }
        public static void add(CustomAttributeType customAttribute, CustomAttributeModifier customAttributeModifier, ItemStack itemStack){
            ItemMeta itemMeta = itemStack.getItemMeta();
            add(customAttribute, customAttributeModifier, itemMeta);
            itemStack.setItemMeta(itemMeta);
        }

        public static CustomAttributeModifiersContainer get(CustomAttributeType customAttributeType, ItemMeta itemMeta){
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            return get(customAttributeType, dataContainer);
        }

        public static CustomAttributeModifiersContainer get(CustomAttributeType customAttributeType, ItemStack itemStack){
            if (!itemStack.hasItemMeta()) return new CustomAttributeModifiersContainer();

            ItemMeta itemMeta = itemStack.getItemMeta();

            CustomAttributeModifiersContainer customAttributeModifiersContainer = get(customAttributeType, itemMeta);
            if (!customAttributeModifiersContainer.isEmpty()) return customAttributeModifiersContainer;
            CustomItem customItem = CustomItemUtils.getFromItemStack(itemStack);
            if (customItem instanceof CustomAttributableItem){
                return ((CustomAttributableItem) customItem).getDefaultCustomAttributes().get(customAttributeType);
            }
            return new CustomAttributeModifiersContainer();

        }

        ///////////////////////////////////////////////////////////////////////////
        // ENTITY
        ///////////////////////////////////////////////////////////////////////////

/*        public static void set(CustomAttribute customAttribute, Entity entity){
            PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
            set(customAttribute, dataContainer);
        }

        public static double get(CustomAttributeType customAttributeType, Entity entity){
            PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
            return get(customAttributeType, dataContainer).getAmount();
        }*/


}
