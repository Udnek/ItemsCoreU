package me.udnek.itemscoreu.multiblockstructure.blockchoice;

import org.bukkit.Material;
import org.bukkit.block.Block;

public interface BlockChoice {

    boolean isAppropriate(Block block);
    Material getExample();
}
