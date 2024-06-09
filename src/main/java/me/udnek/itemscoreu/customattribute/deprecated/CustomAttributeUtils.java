package me.udnek.itemscoreu.customattribute.deprecated;

@Deprecated
public class CustomAttributeUtils {

    // TODO: 2/25/2024 USAGE OF EQUIPMENT SLOT

   // public static final NamespacedKey namespacedKey = new NamespacedKey(ItemsCoreU.getInstance(), "attributes");

    ///////////////////////////////////////////////////////////////////////////
    // DATA CONTAINER
    ///////////////////////////////////////////////////////////////////////////

/*    private static void setContainer(CustomAttributeContainer customAttributeContainer, PersistentDataContainer persistentDataContainer){
        persistentDataContainer.set(namespacedKey, new CustomAttributePersistentDataType(), customAttributeContainer);
    }*/

/*    private static CustomAttributesContainer getContainer(PersistentDataContainer persistentDataContainer){
        if (persistentDataContainer.has(namespacedKey, new CustomAttributePersistentDataType())){
            return persistentDataContainer.get(namespacedKey, new CustomAttributePersistentDataType());
        }
        return new CustomAttributesContainer();
    }

    public static void add(CustomAttribute type, CustomAttributeModifier modifier, PersistentDataContainer persistentDataContainer){
        CustomAttributesContainer customAttributesContainer = getContainer(persistentDataContainer);
        //customAttributesContainer.add(type, modifier);
    }

    public static CustomAttributeModifiersContainer get(CustomAttribute customAttribute, PersistentDataContainer persistentDataContainer){
        CustomAttributesContainer customAttributesContainer = getContainer(persistentDataContainer);
        CustomAttributeModifiersContainer customAttributeModifiersContainer = customAttributesContainer.get(customAttribute);
        if (customAttributeModifiersContainer == null){
            return new CustomAttributeModifiersContainer();
        }
        return customAttributeModifiersContainer;
    }*/

/*    public static double calculateFinalValue(double input, CustomAttributeModifiersContainer container){
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

        public static void add(CustomAttribute customAttribute, CustomAttributeModifier customAttributeModifier, ItemMeta itemMeta){
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            add(customAttribute, customAttributeModifier, dataContainer);
        }
        public static void add(CustomAttribute customAttribute, CustomAttributeModifier customAttributeModifier, ItemStack itemStack){
            ItemMeta itemMeta = itemStack.getItemMeta();
            add(customAttribute, customAttributeModifier, itemMeta);
            itemStack.setItemMeta(itemMeta);
        }

        public static CustomAttributeModifiersContainer get(CustomAttribute customAttribute, ItemMeta itemMeta){
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            return get(customAttribute, dataContainer);
        }

        public static CustomAttributeModifiersContainer get(CustomAttribute customAttribute, ItemStack itemStack){
            if (!itemStack.hasItemMeta()) return new CustomAttributeModifiersContainer();

            ItemMeta itemMeta = itemStack.getItemMeta();

            CustomAttributeModifiersContainer customAttributeModifiersContainer = get(customAttribute, itemMeta);
            if (!customAttributeModifiersContainer.isEmpty()) return customAttributeModifiersContainer;
            CustomItem customItem = CustomItem.get(itemStack);
            if (customItem instanceof DefaultCustomAttributeHolder){
                return ((DefaultCustomAttributeHolder) customItem).getDefaultCustomAttributes().get(customAttribute);
            }
            return new CustomAttributeModifiersContainer();

        }*/

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
