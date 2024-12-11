package me.udnek.itemscoreu.customitem;

import com.google.common.base.Preconditions;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.item.Repairable;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.RegistrySet;
import me.udnek.itemscoreu.customattribute.AttributeUtils;
import me.udnek.itemscoreu.customcomponent.OptimizedComponentHolder;
import me.udnek.itemscoreu.customevent.CustomItemGeneratedEvent;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.nms.ConsumableComponent;
import me.udnek.itemscoreu.nms.Nms;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static io.papermc.paper.datacomponent.DataComponentTypes.*;


public abstract class ConstructableCustomItem extends OptimizedComponentHolder<CustomItem> implements CustomItemProperties, ComponentUpdatingCustomItem {
    private String id;
    protected ItemStack itemStack;
    protected List<Recipe> recipes = null;

    ///////////////////////////////////////////////////////////////////////////
    // INITIAL
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public final @NotNull String getId(){return this.id;}

    @Override
    public void initialize(@NotNull Plugin plugin){
        Preconditions.checkArgument(id == null, "Item already initialized!");
        id = new NamespacedKey(plugin, getRawId()).asString();
    }

    public void initializeComponents(){}

    @Override
    public void afterInitialization() {
        ComponentUpdatingCustomItem.super.afterInitialization();
        initializeComponents();
        generateRecipes(this::registerRecipe);
    }

    @Override
    public @Nullable NamespacedKey getItemModel() {return NamespacedKey.fromString(getId());}
    public @Nullable String getRawItemName(){return "item."+NamespacedKey.fromString(getId()).getNamespace()+"."+getRawId();}
    @Override
    public @Nullable Component getItemName() {
        if (getRawItemName() == null) return null;
        return Component.translatable(getRawItemName());
    }
    @Override
    public @Nullable UseCooldownComponent getUseCooldown() {
        UseCooldownComponent useCooldown = new ItemStack(Material.WHEAT).getItemMeta().getUseCooldown();
        useCooldown.setCooldownGroup(NamespacedKey.fromString(getId()));
        useCooldown.setCooldownSeconds(0.0000001f);
        return useCooldown;
    }

    public void initializeAttributes(@NotNull ItemMeta itemMeta){}

    @Override
    public void setCooldown(@NotNull Player player, int ticks) {
        player.setCooldown(getItemNoClone(), ticks);
    }
    @Override
    public int getCooldown(@NotNull Player player) {return player.getCooldown(getItemNoClone());}

    @Override
    public boolean isTagged(@NotNull Tag<Material> tag) {
        return tag.isTagged(getMaterial());
    }

    public void getRepairMaterials(@NotNull Consumer<Material> consumer){}

    @Override
    public @Nullable Repairable getRepairable() {
        List<ItemType> materials = new ArrayList<>();
        getRepairMaterials(material -> materials.add(material.asItemType()));
        return Repairable.repairable(RegistrySet.keySetFromValues(RegistryKey.ITEM, materials));
    }

    ///////////////////////////////////////////////////////////////////////////
    // CREATING
    ///////////////////////////////////////////////////////////////////////////
    protected void setPersistentData(@NotNull ItemMeta itemMeta){
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING, id);
    }

    protected @NotNull ItemMeta getMainItemMeta(){
        ItemMeta itemMeta = new ItemStack(getMaterial()).getItemMeta();
        setPersistentData(itemMeta);

        itemMeta.itemName(this.getItemName());

        ArrayList<Component> lore = new ArrayList<>();
        getLore(lore::add);
        if (lore.isEmpty()) itemMeta.lore(null);
        else itemMeta.lore(lore);

        itemMeta.setRarity(getRarity());
        itemMeta.setHideTooltip(getHideTooltip());
        itemMeta.setFood(getFood());
        itemMeta.setTool(getTool());
        itemMeta.displayName(getDisplayName());
        itemMeta.setUnbreakable(getUnbreakable());
        itemMeta.setEnchantmentGlintOverride(getEnchantmentGlintOverride());
        itemMeta.setCustomModelData(getCustomModelData());
        if (getDamageResistant() != null) itemMeta.setDamageResistant(getDamageResistant());
        itemMeta.setMaxStackSize(getMaxStackSize());
        if (itemMeta instanceof Damageable damageable){
            if (getMaxDamage() != null){
                damageable.setMaxStackSize(1);
                damageable.setMaxDamage(getMaxDamage());
            }
        }
        if (itemMeta instanceof ArmorMeta armorMeta) armorMeta.setTrim(getTrim());
        if (itemMeta instanceof MusicInstrumentMeta instrumentMeta) instrumentMeta.setInstrument(getMusicInstrument());
        if (getAddDefaultAttributes()) AttributeUtils.addDefaultAttributes(itemMeta, getMaterial());
        if (getBlockData() != null && itemMeta instanceof BlockDataMeta blockDataMeta){
            blockDataMeta.setBlockData(getBlockData());
        }
        ItemFlag[] tooltipHides = getTooltipHides();
        if (getItemModel() != null) itemMeta.setItemModel(getItemModel());
        if (getUseRemainder() != null) itemMeta.setUseRemainder(getUseRemainder());
        if (getUseRemainderCustom() != null) itemMeta.setUseRemainder(getUseRemainderCustom().getItem());
        if (getEquippable() != null) itemMeta.setEquippable(getEquippable());
        if (getGlider() != null) itemMeta.setGlider(getGlider());
        if (getUseCooldown() != null) itemMeta.setUseCooldown(getUseCooldown());

        // attributes
        getAttributes(itemMeta::addAttributeModifier);
        initializeAttributes(itemMeta);
        if (tooltipHides != null) itemMeta.addItemFlags(tooltipHides);

        return itemMeta;
    }

    protected void modifyFinalItemMeta(@NotNull ItemMeta itemMeta){}

    protected @NotNull ItemStack getMainItemStack(){
        ItemMeta itemMeta = getMainItemMeta();
        modifyFinalItemMeta(itemMeta);
        ItemStack itemStack = new ItemStack(getMaterial());
        itemStack.setItemMeta(itemMeta);

        if (getRepairable() != null) itemStack.setData(REPAIRABLE, getRepairable());

        ConsumableComponent consumable = getConsumable();
        if (consumable != null){
            itemStack = Nms.get().setConsumableComponent(itemStack, consumable);
        }

        return itemStack;
    }

    protected void modifyFinalItemStack(@NotNull ItemStack itemStack){}

    protected @NotNull ItemStack getItemNoClone(){
        if (itemStack == null){
            ItemStack newItem = this.getMainItemStack();
            modifyFinalItemStack(newItem);
            CustomItemGeneratedEvent event = new CustomItemGeneratedEvent(this, newItem, getLoreBuilder());
            event.callEvent();
            event.getLoreBuilder().buildAndApply(event.getItemStack());
            itemStack = event.getItemStack();
        }
        return itemStack;
    }

    @Override
    public @NotNull ItemStack getItem(){
        return getItemNoClone().clone();
    }
    @Override
    public @NotNull NamespacedKey getNewRecipeKey() {
        return NamespacedKey.fromString(getId() + "_" + (recipes != null ? Integer.toString(recipes.size()) : "0"));
    }

    @Override
    public void getRecipes(@NotNull Consumer<@NotNull Recipe> consumer) {
        if (recipes == null) return;
        recipes.forEach(consumer);
    }

    @Override
    public void registerRecipe(@NotNull Recipe recipe) {
        if (recipes == null) recipes = new ArrayList<>();
        recipes.add(recipe);
        RecipeManager.getInstance().register(recipe);
    }

    protected void generateRecipes(@NotNull Consumer<@NotNull Recipe> consumer){}

    public interface ComponentConsumer{
        <T> void accept(@NotNull DataComponentType.Valued<T> type);
        void accept(@NotNull DataComponentType.NonValued type);
    }
}
