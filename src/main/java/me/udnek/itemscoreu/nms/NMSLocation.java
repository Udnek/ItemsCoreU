package me.udnek.itemscoreu.nms;

import net.minecraft.core.BlockPos;
import org.bukkit.Location;

public class NMSLocation {

    public BlockPos toNMS(Location location){
        return new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
