package me.udnek.itemscoreu.customblock.type;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customcomponent.ComponentHolder;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.customregistry.Registrable;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomBlockType extends Registrable, ComponentHolder<CustomBlockType> {

    NamespacedKey PDC_NAMESPACE_TYPE = new NamespacedKey(ItemsCoreU.getInstance(), "custom_block_type");
    NamespacedKey PDC_NAMESPACE_ENTITY_TYPE = new NamespacedKey(ItemsCoreU.getInstance(), "custom_block_type");

    static @Nullable String getId(@NotNull Block block){
        BlockState state = block.getState();
        if (state instanceof TileState tileState) return getId(tileState);
        return null;
    }

    static @Nullable String getId(@NotNull TileState blockState){
        String normal = blockState.getPersistentDataContainer().get(PDC_NAMESPACE_TYPE, PersistentDataType.STRING);
        String entity = blockState.getPersistentDataContainer().get(PDC_NAMESPACE_ENTITY_TYPE, PersistentDataType.STRING);
        return normal == null ? entity : normal;
    }

    static @Nullable String getId(@NotNull Location location){
        return getId(location.getBlock());
    }

    static @Nullable CustomBlockType get(@NotNull Block block){
        return CustomRegistries.BLOCK_TYPE.get(getId(block));
    }
    static @Nullable CustomBlockType get(@NotNull TileState blockState){
        return CustomRegistries.BLOCK_TYPE.get(getId(blockState));
    }
    static @Nullable CustomBlockType get(@NotNull String id){
        return CustomRegistries.BLOCK_TYPE.get(id);
    }

    void place(@NotNull Location location);
    void destroy(@NotNull Location location);
    void onDestroy(@NotNull BlockDestroyEvent event);
    void onDestroy(@NotNull BlockBreakEvent event);
}
