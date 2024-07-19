package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customblock.CustomBlock;
import org.bukkit.event.block.BlockPlaceEvent;

public interface CustomBlockItem extends CustomItem {
    CustomBlock getBlock();
    default void onBlockPlace(BlockPlaceEvent event){
        getBlock().place(event.getBlockPlaced().getLocation());
    }
}
