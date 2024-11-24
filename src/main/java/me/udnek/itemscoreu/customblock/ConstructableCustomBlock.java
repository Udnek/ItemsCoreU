package me.udnek.itemscoreu.customblock;

import com.destroystokyo.paper.ParticleBuilder;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customcomponent.OptimizedComponentHolder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructableCustomBlock extends OptimizedComponentHolder<CustomBlock> implements CustomBlock{
    private String id;
    protected EntitySnapshot visualRepresentation;

    @Override
    public void initialize(@NotNull Plugin plugin) {
        Preconditions.checkArgument(id == null, "Registrable already initialized!");
        id = new NamespacedKey(plugin, getRawId()).asString();
    }

    public abstract @NotNull String getRawId();

    @Override
    public @NotNull String getId() {return id;}

    @Override
    public void place(Location location){
        CreatureSpawner blockState = (CreatureSpawner) Material.SPAWNER.createBlockData().createBlockState().copy(location);
        blockState.getLocation(location);
        blockState.setRequiredPlayerRange(0);
        blockState.setSpawnedEntity(getVisualRepresentation());
        blockState.getPersistentDataContainer().set(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING, getId());
        blockState.update(true, false);

    }

    @Override
    public void onLoad(BlockState blockState) {}
    @Override
    public void onUnload(BlockState blockState) {}
    @Override
    public void destroy(Location location) {
        location.getWorld().getBlockAt(location).setType(Material.AIR);
        onDestroy(location.getBlock());
    }
    @Override
    public void onDestroy(BlockDestroyEvent event){
        event.setExpToDrop(0);
        onDestroy(event.getBlock());
    }
    @Override
    public void onDestroy(BlockBreakEvent event){
        event.setExpToDrop(0);
        onDestroy(event.getBlock());
    }
    public void onDestroy(Block block){
        Location centerLocation = block.getLocation().toCenterLocation();
/*        centerLocation.getWorld().spawnParticle(
                Particle.ITEM,
                centerLocation,
                10,
                0.2, 0.2, 0.2,
                0,
                getVisualItem());*/
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
    public abstract ItemStack getVisualItem();

    ///////////////////////////////////////////////////////////////////////////
    // CREATING
    ///////////////////////////////////////////////////////////////////////////
    public EntitySnapshot getVisualRepresentation(){
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
