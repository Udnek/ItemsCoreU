package me.udnek.itemscoreu.customitem.utils;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.InteractableItem;
import me.udnek.itemscoreu.utils.CustomItemUtils;
import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItemListener extends SelfRegisteringListener {


    public CustomItemListener(JavaPlugin plugin) {
        super(plugin);
    }

    // TODO: 2/15/2024 FIX SO TWO ITEMS WONT FIRE AT ONE TICK
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getItem() == null) return;
        if (!event.getAction().isRightClick()) return;
        //event.getPlayer().sendMessage(event.getItem().getType().toString());

        ItemStack itemStack = event.getItem();
        if (!CustomItemUtils.isCustomItem(itemStack)) return;
        CustomItem customItem = CustomItemUtils.getFromItemStack(itemStack);
        if (customItem instanceof InteractableItem){
            ((InteractableItem) customItem).onRightClicks(event);
        }

        //event.getPlayer().sendMessage(event.getItem().getType() + " " + event.getHand());
//
        //if (!event.getAction().isRightClick()) return;
//
        //Interactable itemForRightClick = this.getItemForRightClick(event);
        //if (itemForRightClick == null) return;
        //itemForRightClick.onRightClicks(event);
    }

    // TODO: 2/15/2024 FIX SO TWO ITEMS WONT FIRE AT ONE TICK
    public InteractableItem getItemForRightClick(PlayerInteractEvent event){
        ItemStack itemStack;

        if (event.getItem() != null) {
            itemStack = event.getItem();
            if (CustomItemUtils.isCustomItem(itemStack)){
                CustomItem customItem = CustomItemUtils.getFromItemStack(itemStack);
                if (customItem instanceof InteractableItem){
                    return (InteractableItem) customItem;
                }
            }
        }
        itemStack = event.getPlayer().getEquipment().getItemInOffHand();
        if (CustomItemUtils.isCustomItem(itemStack)){
            CustomItem customItem = CustomItemUtils.getFromItemStack(itemStack);
            if (customItem instanceof InteractableItem){
                return (InteractableItem) customItem;
            }
        }
        return null;
    }



    @EventHandler
    public void onPlayerConsumes(PlayerItemConsumeEvent event) {
        ItemStack itemStack = event.getItem();

        if (CustomItemUtils.isCustomItem(itemStack)) {
            CustomItem customItem = CustomItemUtils.getFromItemStack(itemStack);
            customItem.onConsumes(event, itemStack);
        }
    }

    @EventHandler
    public void onProjectileHits(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!(projectile instanceof ThrowableProjectile)) return;

        ItemStack itemStack = ((ThrowableProjectile) projectile).getItem();
        if (CustomItemUtils.isCustomItem(itemStack)) {
            CustomItem customItem = CustomItemUtils.getFromItemStack(itemStack);
            customItem.onThrowableProjectileHits(event, itemStack);
        }
    }

    @EventHandler
    public void onEntityShootsBow(EntityShootBowEvent event){
        if (event.getBow() != null){
            ItemStack bow = event.getBow();
            if (!CustomItemUtils.isCustomItem(bow)) return;
            CustomItem customItemBow = CustomItemUtils.getFromItemStack(bow);
            customItemBow.onShoots(event, bow);
        }
        if (event.getConsumable() != null){
            ItemStack arrow = event.getConsumable();
            if (!CustomItemUtils.isCustomItem(arrow)) return;
            CustomItem customItemBow = CustomItemUtils.getFromItemStack(arrow);
            customItemBow.onShoots(event, arrow);
        }
    }
}














