package me.udnek.itemscoreu.multiblockstructure.blockchoice;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public interface BlockChoice {

    boolean isAppropriate(Block block);
    @NotNull Material getExample();
}
