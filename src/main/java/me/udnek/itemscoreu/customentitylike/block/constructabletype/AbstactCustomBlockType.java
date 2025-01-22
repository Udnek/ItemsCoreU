package me.udnek.itemscoreu.customentitylike.block.constructabletype;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import me.udnek.itemscoreu.customcomponent.CustomComponentMap;
import me.udnek.itemscoreu.customentitylike.block.CustomBlockManager;
import me.udnek.itemscoreu.customentitylike.block.CustomBlockType;
import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class AbstactCustomBlockType extends AbstractRegistrable implements CustomBlockType {

    private final CustomComponentMap<CustomBlockType> components = new CustomComponentMap<>();

    @Override
    public @NotNull CustomComponentMap<CustomBlockType> getComponents() {
        return components;
    }

    public abstract @NotNull Material getMaterial();

    @Override
    public void onPlayerLoads(@NotNull PlayerChunkLoadEvent event, @NotNull TileState tileState) {
        BlockState copy = tileState.copy();
        copy.setType(Material.DIAMOND_BLOCK);
        event.getPlayer().sendBlockChange(tileState.getLocation(), copy.getBlockData());
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void place(@NotNull Location location){
        TileState blockState = (TileState) getMaterial().createBlockData().createBlockState().copy(location);
        blockState.getPersistentDataContainer().set(PDC_KEY, PersistentDataType.STRING, getId());
        blockState.update(true, false);
        CustomBlockManager.getInstance().loadAny(this, blockState);
    }

    @Override
    public void afterInitialization() {
        CustomBlockType.super.afterInitialization();
        initializeComponents();
    }

    @OverridingMethodsMustInvokeSuper
    public void initializeComponents(){}

    @Override
    public void destroy(@NotNull Location location) {
        location.getWorld().getBlockAt(location).setType(Material.AIR);
        onDestroy(location.getBlock());
    }

    @Override
    public void onDestroy(@NotNull EntityExplodeEvent event, @NotNull Block block) {
        onDestroy(block);
    }

    @Override
    public void onDestroy(@NotNull BlockExplodeEvent event) {
        onDestroy(event.getExplodedBlockState().getBlock());
    }
    @Override
    public void onDestroy(@NotNull BlockDestroyEvent event){
        event.setExpToDrop(0);
        onDestroy(event.getBlock());
    }
    @Override
    public void onDestroy(@NotNull BlockBreakEvent event){
        event.setExpToDrop(0);
        onDestroy(event.getBlock());
    }
    @OverridingMethodsMustInvokeSuper
    public void onDestroy(@NotNull Block block){
        CustomBlockManager.getInstance().unloadAny(this, (TileState) block.getState());
    }
}
