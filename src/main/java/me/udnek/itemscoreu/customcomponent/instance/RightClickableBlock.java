package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customentitylike.block.CustomBlockType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface RightClickableBlock extends CustomComponent<CustomBlockType> {

    RightClickableBlock EMPTY = (block, event) -> {};

    void onRightClick(@NotNull CustomBlockType block, @NotNull PlayerInteractEvent event);

    @Override
    default @NotNull CustomComponentType<CustomBlockType, ?> getType(){
        return CustomComponentType.RIGHT_CLICKABLE_BLOCK;
    }
}
