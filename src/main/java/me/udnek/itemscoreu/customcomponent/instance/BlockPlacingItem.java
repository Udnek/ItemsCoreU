package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customentitylike.block.CustomBlockType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class BlockPlacingItem implements CustomComponent<CustomItem> {

    public static final BlockPlacingItem EMPTY = new BlockPlacingItem(){
        @Override
        public void onPlace(@NotNull BlockPlaceEvent event) {}
    };

    protected CustomBlockType block;

    private BlockPlacingItem(){}

    public BlockPlacingItem(@NotNull CustomBlockType blockType){
        this.block = blockType;
    }

    public void onPlace(@NotNull BlockPlaceEvent event){
        block.place(event.getBlock().getLocation());
    }

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return CustomComponentType.BLOCK_PLACING_ITEM;
    }
}
