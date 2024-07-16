package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customblock.CustomBlock;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;

public interface CustomBlockItem extends CustomItem {
    CustomBlock getBlock();
    default void onBlockPlace(BlockPlaceEvent event){
        getBlock().place(event.getBlockPlaced().getLocation());
    }
}
