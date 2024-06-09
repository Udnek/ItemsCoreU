package me.udnek.itemscoreu.customenchantment;

public class CustomEnchantmentManager {

/*    public static Enchantment register(JavaPlugin javaPlugin, CustomEnchantment customEnchantment){

        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.getClass().getName().equals(customEnchantment.getClass().getName())){
                return enchantment;
            }
        }

        customEnchantment.setNamespacedKey(javaPlugin);
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Enchantment.registerEnchantment(customEnchantment);
        LogUtils.pluginLog(customEnchantment.getKey().asString() + " (Enchantment)");
        return customEnchantment;
    }*/

}
