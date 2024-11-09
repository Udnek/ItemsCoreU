package me.udnek.itemscoreu.nms;

import net.minecraft.world.item.ItemUseAnimation;
import org.jetbrains.annotations.NotNull;

public enum ConsumableAnimation {

    NONE(ItemUseAnimation.NONE),
    EAT(ItemUseAnimation.EAT),
    DRINK(ItemUseAnimation.DRINK),
    BLOCK(ItemUseAnimation.BLOCK),
    BOW(ItemUseAnimation.BOW),
    SPEAR(ItemUseAnimation.SPEAR),
    CROSSBOW(ItemUseAnimation.CROSSBOW),
    SPYGLASS(ItemUseAnimation.SPYGLASS),
    TOOT_HORN(ItemUseAnimation.TOOT_HORN),
    BRUSH(ItemUseAnimation.BRUSH);

    public final ItemUseAnimation nms;
    ConsumableAnimation(@NotNull ItemUseAnimation animation){
        this.nms = animation;
    }

}
