package me.udnek.itemscoreu.multiblockstructure.blockchoice;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class AnyBlockChoice implements BlockChoice{

    public AnyBlockChoice(){}

    @Override
    public boolean isAppropriate(Block block) {
        return true;
    }

    @Override
    public @NotNull Material getExample() {
        return Material.AIR;
    }
}

