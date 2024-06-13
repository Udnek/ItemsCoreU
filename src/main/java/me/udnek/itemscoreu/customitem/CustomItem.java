package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customattribute.AttributeUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class CustomItem {
    private JavaPlugin plugin;
    private List<Recipe> recipes;
    private String id;
    protected ItemStack itemStack;

    public static final NamespacedKey PERSISTENT_DATA_CONTAINER_NAMESPACE = new NamespacedKey(ItemsCoreU.getInstance(), "item");
    protected CustomItem(){}

    ///////////////////////////////////////////////////////////////////////////
    // STATIC
    ///////////////////////////////////////////////////////////////////////////

    public static String getId(ItemStack itemStack){
        if (itemStack == null) return null;
        if (!itemStack.hasItemMeta()) return null;
        return itemStack.getItemMeta().getPersistentDataContainer().get(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING);
    }
    public static CustomItem get(ItemStack itemStack){return get(getId(itemStack));}
    public static CustomItem get(String id){return CustomItemManager.get(id);}
    public static boolean isIdExists(String id){return CustomItemManager.get(id) != null;}
    public static boolean isCustom(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().has(PERSISTENT_DATA_CONTAINER_NAMESPACE);
    }
    public static Set<String> getAllIds(){
        return CustomItemManager.getAllIds();
    }

    public static boolean isSameIds(ItemStack itemStack1, ItemStack itemStack2){
        CustomItem customItem1 = get(itemStack1);
        CustomItem customItem2 = get(itemStack2);
        if (customItem1 == null || customItem2 == null) return false;
        return customItem1 == customItem2;
    }

    public boolean isThisItem(ItemStack itemStack){
        return CustomItem.get(itemStack) == this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // UTILS
    ///////////////////////////////////////////////////////////////////////////
    public boolean isSameId(CustomItem customItem){return customItem.getId().equals(id);}
    public boolean isSameId(ItemStack itemStack){
        String id = CustomItem.getId(itemStack);
        if (id == null) return false;
        return id.equals(getId());
    }

    ///////////////////////////////////////////////////////////////////////////
    // INITIAL
    ///////////////////////////////////////////////////////////////////////////

    public final String getId(){return this.id;}

    protected void initialize(JavaPlugin javaPlugin){
        this.plugin = javaPlugin;
        this.id = new NamespacedKey(javaPlugin, getRawId()).asString();
        this.recipes = new ArrayList<>();
    }

    ///////////////////////////////////////////////////////////////////////////
    // PROPERTIES
    ///////////////////////////////////////////////////////////////////////////
    public abstract String getRawId();
    public abstract Material getMaterial();
    public abstract String getRawItemName();

    // OPTIONAL
    public Component getItemName(){return Component.translatable(this.getRawItemName());}
    public Component getCustomDisplayName(){return null;}
    public List<Component> getLore(){return null;}
    public ItemRarity getItemRarity(){return null;}
    public Integer getCustomModelData(){return null;}
    public boolean getHideTooltip(){return false;}
    public FoodComponent getFoodComponent(){return null;}
    protected ItemFlag[] getTooltipHides(){return new ItemFlag[0];}
    public Integer getMaxStackSize(){return null;}
    public Integer getMaxDamage(){return null;}
    public boolean getUnbreakable(){return false;}
    public Boolean getFireResistant(){return null;}
    public Boolean getEnchantmentGlintOverride(){return null;}
    public ArmorTrim getArmorTrim(){return null;}
    public MusicInstrument getMusicInstrument(){return null;}
    public boolean getAddDefaultAttributes(){return false;}
    public Map<Attribute, List<AttributeModifier>> getAttributes(){return null;}
    public BlockData getBlockData(){return null;}

    ///////////////////////////////////////////////////////////////////////////
    // CREATING
    ///////////////////////////////////////////////////////////////////////////

    protected void setPersistentData(ItemMeta itemMeta){
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING, id);
    }

    protected ItemMeta getMainItemMeta(){
        ItemMeta itemMeta = new ItemStack(this.getMaterial()).getItemMeta();
        this.setPersistentData(itemMeta);

        itemMeta.itemName(this.getItemName());
        itemMeta.lore(this.getLore());
        itemMeta.setRarity(getItemRarity());
        itemMeta.setHideTooltip(getHideTooltip());
        itemMeta.setFood(getFoodComponent());
        itemMeta.displayName(getCustomDisplayName());
        itemMeta.setUnbreakable(getUnbreakable());
        itemMeta.setEnchantmentGlintOverride(getEnchantmentGlintOverride());
        itemMeta.setCustomModelData(getCustomModelData());
        if (getFireResistant() != null) itemMeta.setFireResistant(getFireResistant());
        itemMeta.setMaxStackSize(getMaxStackSize());
        if (itemMeta instanceof Damageable damageable){
            if (getMaxDamage() != null){
                damageable.setMaxStackSize(1);
                damageable.setMaxDamage(getMaxDamage());
            }
        }
        if (itemMeta instanceof ArmorMeta armorMeta) armorMeta.setTrim(getArmorTrim());
        if (itemMeta instanceof MusicInstrumentMeta instrumentMeta) instrumentMeta.setInstrument(getMusicInstrument());
        if (getAddDefaultAttributes()) AttributeUtils.addDefaultAttributes(itemMeta, getMaterial());
        if (getAttributes() != null){
            for (Map.Entry<Attribute, List<AttributeModifier>> entry : getAttributes().entrySet()) {
                Attribute attribute = entry.getKey();
                for (AttributeModifier modifier : entry.getValue()) {
                    itemMeta.addAttributeModifier(attribute, modifier);
                }
            }
        }
        if (getBlockData() != null && itemMeta instanceof BlockDataMeta blockDataMeta){
            blockDataMeta.setBlockData(getBlockData());
        }
        itemMeta.addItemFlags(getTooltipHides());

        return itemMeta;
    }

    protected void modifyFinalItemMeta(ItemMeta itemMeta){}

    protected ItemStack getMainItemStack(){
        ItemMeta itemMeta = this.getMainItemMeta();
        this.modifyFinalItemMeta(itemMeta);
        ItemStack itemStack = new ItemStack(this.getMaterial());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    protected void modifyFinalItemStack(ItemStack itemStack){
    }


    public ItemStack getItem(){
        if (this.itemStack == null){
            ItemStack itemStack = this.getMainItemStack();
            this.modifyFinalItemStack(itemStack);
            this.itemStack = itemStack;
        }
        return this.itemStack.clone();
    }

    ///////////////////////////////////////////////////////////////////////////
    // RECIPES
    ///////////////////////////////////////////////////////////////////////////

    protected List<Recipe> generateRecipes(){return new ArrayList<>();}
    protected final void registerRecipes(){
        List<Recipe> recipes = generateRecipes();
        for (Recipe recipe : recipes) {
            Bukkit.addRecipe(recipe);
        }
        this.recipes = recipes;
    }

    // TODO: 6/2/2024 REALISE
    protected final void registerAdditionalRecipes(Recipe ...recipes){
        for (Recipe recipe : recipes) {
            Bukkit.addRecipe(recipe);
            this.recipes.add(recipe);
        }
    }
    public final List<Recipe> getRecipes(){
        return this.recipes;
    }

    protected NamespacedKey getRecipeNamespace(int recipeNumber){
        return new NamespacedKey(this.plugin, this.getRawId()+ "_" + recipeNumber);
    }

    ///////////////////////////////////////////////////////////////////////////
    // EVENTS
    ///////////////////////////////////////////////////////////////////////////
    public ItemStack onPrepareCraft(PrepareItemCraftEvent event){
        return this.getItemFromCraftingMatrix(event.getRecipe().getResult(), event.getInventory().getMatrix(), event.getRecipe());
    }
    protected ItemStack getItemFromCraftingMatrix(ItemStack result, ItemStack[] matrix, Recipe recipe){return result;}

}
