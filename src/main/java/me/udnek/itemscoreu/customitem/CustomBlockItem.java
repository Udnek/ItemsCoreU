package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customblock.CustomBlock;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;

public abstract class CustomBlockItem extends CustomItem {
    @Override
    public Material getMaterial() {return Material.SPAWNER;}

    @Override
    public ItemFlag[] getTooltipHides() {
        return new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP};
    }

    public abstract CustomBlock getBlock();
    public void onBlockPlace(BlockPlaceEvent event){
        getBlock().place(event.getBlockPlaced().getLocation());
    }
}
