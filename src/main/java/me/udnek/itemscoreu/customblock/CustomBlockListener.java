package me.udnek.itemscoreu.customblock;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.udnek.itemscoreu.customblock.type.CustomBlockType;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CustomBlockListener extends SelfRegisteringListener {
    public CustomBlockListener(@NotNull JavaPlugin plugin) {super(plugin);}

    @EventHandler
    public void onDestroy(BlockDestroyEvent event){
        CustomBlockType customBlockType = CustomBlockType.get(event.getBlock());
        if (customBlockType != null) customBlockType.onDestroy(event);
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        CustomBlockType customBlockType = CustomBlockType.get(event.getBlock());
        if (customBlockType != null) customBlockType.onDestroy(event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (event.getClickedBlock() == null) return;
        CustomBlockType customBlockType = CustomBlockType.get(event.getClickedBlock());
        if (customBlockType == null) return;
        customBlockType.getComponents().getOrDefault(CustomComponentType.RIGHT_CLICKABLE_BLOCK).onRightClick(customBlockType, event);
    }
}
