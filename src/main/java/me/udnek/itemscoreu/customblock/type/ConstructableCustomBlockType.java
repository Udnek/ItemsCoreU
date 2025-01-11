package me.udnek.itemscoreu.customblock.type;

import com.destroystokyo.paper.ParticleBuilder;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customblock.CustomBlockManager;
import me.udnek.itemscoreu.customcomponent.OptimizedComponentHolder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class ConstructableCustomBlockType extends OptimizedComponentHolder<CustomBlockType> implements CustomBlockType {
    private String id;
    protected EntitySnapshot visualRepresentation;

    @Override
    public void initialize(@NotNull Plugin plugin) {
        Preconditions.checkArgument(id == null, "Registrable already initialized!");
        id = new NamespacedKey(plugin, getRawId()).asString();
    }


    @Override
    public void afterInitialization() {
        CustomBlockType.super.afterInitialization();
        initializeComponents();
    }

    @OverridingMethodsMustInvokeSuper
    public void initializeComponents(){}

    public abstract @NotNull String getRawId();

    @Override
    public @NotNull String getId() {return id;}

    @Override
    @OverridingMethodsMustInvokeSuper
    public void place(@NotNull Location location){
        CreatureSpawner blockState = (CreatureSpawner) Material.SPAWNER.createBlockData().createBlockState().copy(location);
        blockState.getLocation(location);
        blockState.setRequiredPlayerRange(0);
        blockState.setSpawnedEntity(getVisualRepresentation());
        blockState.getPersistentDataContainer().set(PDC_NAMESPACE_TYPE, PersistentDataType.STRING, getId());
        blockState.update(true, false);
        if (this instanceof CustomBlockEntityType<?> blockEntityType){
            CustomBlockManager.getInstance().load(blockEntityType, blockState);
        }
    }

    @Override
    public void destroy(@NotNull Location location) {
        location.getWorld().getBlockAt(location).setType(Material.AIR);
        onDestroy(location.getBlock());
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
    public void onDestroy(@NotNull Block block){
        if (this instanceof CustomBlockEntityType<?>){
            CustomBlockManager.getInstance().unload((TileState) block.getState());
        }

        Location centerLocation = block.getLocation().toCenterLocation();
        // TODO: 7/22/2024 FIX PARTICLES
        ParticleBuilder builder = new ParticleBuilder(Particle.ITEM);
        builder.location(centerLocation);
        builder.data(getVisualItem());
        builder.count(10);
        builder.offset(0.5, 0.5, 0.5);
        builder.extra(0);
        builder.spawn();
    }

    ///////////////////////////////////////////////////////////////////////////
    // PROPERTIES
    ///////////////////////////////////////////////////////////////////////////

    public abstract @NotNull ItemStack getVisualItem();

    ///////////////////////////////////////////////////////////////////////////
    // CREATING
    ///////////////////////////////////////////////////////////////////////////

    public @NotNull EntitySnapshot getVisualRepresentation(){
        if (visualRepresentation == null){
            Location location = new Location(Bukkit.getWorld("world"), 0, 0, 0);
            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setHeadPose(new EulerAngle(Math.PI/6, 0, 0));
            armorStand.getEquipment().setHelmet(getVisualItem());
            armorStand.setVisible(false);
            visualRepresentation = armorStand.createSnapshot();
            armorStand.remove();
        }
        return visualRepresentation;
    }

}
