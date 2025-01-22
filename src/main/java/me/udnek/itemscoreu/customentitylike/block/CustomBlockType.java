package me.udnek.itemscoreu.customentitylike.block;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customcomponent.ComponentHolder;
import me.udnek.itemscoreu.customentitylike.EntityLikeType;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomBlockType extends ComponentHolder<CustomBlockType>, EntityLikeType<TileState> {

    NamespacedKey PDC_KEY = new NamespacedKey(ItemsCoreU.getInstance(), "custom_block_type");

    static @Nullable String getId(@NotNull Block block){
        BlockState state = block.getState();
        if (state instanceof TileState tileState) return getId(tileState);
        return null;
    }

    static @Nullable String getId(@NotNull TileState blockState){
        return blockState.getPersistentDataContainer().get(PDC_KEY, PersistentDataType.STRING);
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
    void onPlayerLoads(@NotNull PlayerChunkLoadEvent event, @NotNull TileState tileState);
    void onDestroy(@NotNull EntityExplodeEvent event, @NotNull Block block);
    void onDestroy(@NotNull BlockDestroyEvent event);
    void onDestroy(@NotNull BlockBreakEvent event);
    void onDestroy(@NotNull BlockExplodeEvent event);

}
