package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customblock.CustomBlockType;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public interface CustomBlockItem extends CustomItem {
    @NotNull CustomBlockType getBlock();
    default void onBlockPlace(BlockPlaceEvent event){
        getBlock().place(event.getBlockPlaced().getLocation());
    }
}
