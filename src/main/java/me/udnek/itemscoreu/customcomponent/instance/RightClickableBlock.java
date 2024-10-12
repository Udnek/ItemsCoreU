package me.udnek.itemscoreu.customcomponent.instance;

import me.udnek.itemscoreu.customblock.CustomBlock;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public abstract class RightClickableBlock implements CustomComponent<CustomBlock> {

    public static final RightClickableBlock EMPTY = new RightClickableBlock() {
        @Override
        public void onRightClick(@NotNull CustomBlock block, @NotNull PlayerInteractEvent event) {}
    };

    public abstract void onRightClick(@NotNull CustomBlock block, @NotNull PlayerInteractEvent event);

    @Override
    public @NotNull CustomComponentType<CustomBlock, ?> getType() {
        return CustomComponentType.RIGHT_CLICKABLE_BLOCK;
    }
}
