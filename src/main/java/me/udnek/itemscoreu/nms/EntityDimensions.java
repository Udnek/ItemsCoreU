package me.udnek.itemscoreu.nms;

import net.minecraft.world.entity.EntityAttachments;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class EntityDimensions {

    public float width;
    public float height;
    public float eyeHeight;
    protected EntityAttachments attachments;
    public boolean fixed;

    public EntityDimensions(float width, float height, float eyeHeight, @NotNull EntityAttachments attachments, boolean fixed){
        this.width = width;
        this.height = height;
        this.eyeHeight = eyeHeight;
        this.attachments = attachments;
        this.fixed = fixed;
    }

    public EntityDimensions(@NotNull net.minecraft.world.entity.EntityDimensions nms){
        this(nms.width(), nms.height(), nms.eyeHeight(), nms.attachments(), nms.fixed());
    }

    public @NotNull net.minecraft.world.entity.EntityDimensions toNms(){
        return new net.minecraft.world.entity.EntityDimensions(width, height, eyeHeight, attachments, fixed);
    }
}
