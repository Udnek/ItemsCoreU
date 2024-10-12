package me.udnek.itemscoreu.customcomponent;

import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import org.jetbrains.annotations.NotNull;

public class ConstructableComponentType<HolderType, Component extends CustomComponent<HolderType>> extends AbstractRegistrable implements CustomComponentType<HolderType, Component>{

    protected Component defaultComponent;
    protected String rawId;

    public ConstructableComponentType(@NotNull String rawId, @NotNull Component defaultComponent){
        this.rawId = rawId;
        this.defaultComponent = defaultComponent;
    }

    @NotNull
    @Override
    public Component getDefault() {
        return defaultComponent;
    }

    @Override
    public @NotNull String getRawId() {
        return rawId;
    }
}
