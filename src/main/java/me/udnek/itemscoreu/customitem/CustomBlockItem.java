package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customblock.CustomBlock;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public interface CustomBlockItem extends CustomItem {
    @NotNull CustomBlock getBlock();
    default void onBlockPlace(BlockPlaceEvent event){
        getBlock().place(event.getBlockPlaced().getLocation());
    }
}
