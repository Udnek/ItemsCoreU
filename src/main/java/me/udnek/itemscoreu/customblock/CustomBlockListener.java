package me.udnek.itemscoreu.customblock;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomBlockListener extends SelfRegisteringListener {
    public CustomBlockListener(JavaPlugin plugin) {super(plugin);}

    // TODO: 6/13/2024 CATCH MORE EVENTS??
    @EventHandler
    public void onDestroy(BlockDestroyEvent event){
        CustomBlock customBlock = CustomBlock.get(event.getBlock());
        if (customBlock != null) customBlock.onDestroy(event);
    }
    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event){
        CustomBlock customBlock = CustomBlock.get(event.getBlock());
        if (customBlock != null) customBlock.onDestroy(event);
    }

/*    @EventHandler
    public void onPick(PlayerPickItemEvent event){
        Block block = event.getPlayer().getTargetBlockExact(6);

        LogUtils.log(String.valueOf(block));
        if (block != null) LogUtils.log(String.valueOf(block.getType()));

        CustomBlock customBlock = CustomBlock.get(block);

        LogUtils.log(String.valueOf(customBlock));

        if (customBlock == null) return;
        int targetSlot = event.getTargetSlot();
        CustomItem customItem = customBlock.getItem();
        event.getPlayer().getInventory().setItem(targetSlot, customItem.getItem());
    }*/
}
