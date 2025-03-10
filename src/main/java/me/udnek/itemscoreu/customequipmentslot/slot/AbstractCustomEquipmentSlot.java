package me.udnek.itemscoreu.customequipmentslot.slot;

import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCustomEquipmentSlot extends AbstractRegistrable implements CustomEquipmentSlot {
    protected final String rawId;

    public AbstractCustomEquipmentSlot(@NotNull String id){
        this.rawId = id;
    }
    @NotNull
    @Override
    public String getRawId() {
        return rawId;
    }
}
