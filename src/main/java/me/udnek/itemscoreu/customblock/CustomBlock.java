package me.udnek.itemscoreu.customblock;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.utils.PluginInitializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface CustomBlock extends PluginInitializable {

    static final NamespacedKey PERSISTENT_DATA_CONTAINER_NAMESPACE = new NamespacedKey(ItemsCoreU.getInstance(), "block");

    void onLoad(BlockState blockState);
    void onUnload(BlockState blockState);
    void place(Location location);
    void destroy(Location location);
    void onDestroy(BlockDestroyEvent event);
    void onDestroy(BlockBreakEvent event);
    @NotNull String getId();
    @NotNull String getRawId();

    ///////////////////////////////////////////////////////////////////////////
    // STATIC
    ///////////////////////////////////////////////////////////////////////////

    static String getId(Block block){
        if (block == null) return null;
        return getId(block.getState());
    }

    static String getId(BlockState blockState){
        if (!(blockState instanceof TileState tileState)) return null;
        return tileState.getPersistentDataContainer().get(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING);
    }

    static String getId(Location location){
        if (location == null) return null;
        return getId(location.getBlock());
    }

    static CustomBlock get(Block block){
        return CustomBlockManager.get(getId(block));
    }
    static CustomBlock get(BlockState blockState){
        return CustomBlockManager.get(getId(blockState));
    }
    static CustomBlock get(Location location){
        return CustomBlockManager.get(getId(location));
    }
    static CustomBlock get(String id){
        return CustomBlockManager.get(id);
    }

    static Set<String> getAllIds(){return CustomBlockManager.getAllIds();}
}
