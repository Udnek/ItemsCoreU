package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customblock.CustomBlock;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface RightClickableBlock extends CustomComponent<CustomBlock> {

    RightClickableBlock EMPTY = new RightClickableBlock() {
        @Override
        public void onRightClick(@NotNull CustomBlock block, @NotNull PlayerInteractEvent event) {}
    };

    void onRightClick(@NotNull CustomBlock block, @NotNull PlayerInteractEvent event);

    @Override
    default @NotNull CustomComponentType<CustomBlock, ?> getType() {
        return CustomComponentType.RIGHT_CLICKABLE_BLOCK;
    }
}
