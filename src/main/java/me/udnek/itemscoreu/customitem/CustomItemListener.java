package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.custominventory.CustomInventory;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class CustomItemListener extends SelfRegisteringListener {
    public CustomItemListener(@NotNull Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                Nms.get().iterateTroughCooldowns(player, (key, start, end) -> Nms.get().sendCooldown(player,  key, end-start));
            }
        }.runTaskLater(ItemsCoreU.getInstance(), 20);

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        CustomItem customItem = CustomItem.get(event.getItem());
        if (customItem == null) return;
        customItem.getComponents().getOrDefault(CustomComponentType.RIGHT_CLICKABLE_ITEM).onRightClick(customItem, event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        CustomItem.consumeIfCustom(event.getItemInHand(), customItem -> {
            customItem.getComponents().getOrDefault(CustomComponentType.BLOCK_PLACING_ITEM).onPlace(event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerConsume(InventoryClickEvent event){
        CustomItem currentItem = CustomItem.get(event.getCurrentItem());
        CustomItem cursorItem = CustomItem.get(event.getCursor());
        if (currentItem != null) currentItem.getComponents().getOrDefault(CustomComponentType.INVENTORY_INTERACTABLE_ITEM).onBeingClicked(currentItem, event);
        if (cursorItem != null) cursorItem.getComponents().getOrDefault(CustomComponentType.INVENTORY_INTERACTABLE_ITEM).onClickWith(cursorItem, event);
    }

    @EventHandler
    public void updateItemOnTake(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;
        if (clickedInventory.getType() == InventoryType.PLAYER) return;
        CustomInventory customInventory = CustomInventory.get(clickedInventory);
        if (customInventory != null && !customInventory.shouldAutoUpdateItems()) return;
        CustomItem.consumeIfCustom(event.getCurrentItem(), customItem -> event.setCurrentItem(customItem.update(event.getCurrentItem())));
    }

    @EventHandler
    public void updateItemOnJoin(PlayerJoinEvent event){
        PlayerInventory inventory = event.getPlayer().getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            int finalI = i;
            CustomItem.consumeIfCustom(item, customItem -> inventory.setItem(finalI, customItem.update(item)));
        }
    }
}














