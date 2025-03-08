package me.udnek.itemscoreu.customcomponent;

import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ConstructableComponentType<HolderType, Component extends CustomComponent<HolderType>> extends AbstractRegistrable implements CustomComponentType<HolderType, Component>{

    protected Component defaultComponent;
    protected String rawId;
    protected Supplier<Component> componentCreator;

    public ConstructableComponentType(@NotNull String rawId, @NotNull Component defaultComponent, @NotNull Supplier<Component> componentCreator){
        this.rawId = rawId;
        this.defaultComponent = defaultComponent;
        this.componentCreator = componentCreator;
    }

    public ConstructableComponentType(@NotNull String rawId, @NotNull Component defComponent){
        this(rawId, defComponent, () -> defComponent);
    }

    @NotNull
    @Override
    public Component getDefault() {
        return defaultComponent;
    }

    @Override
    public @NotNull Component createNewDefault() {
        return componentCreator.get();
    }

    @Override
    public @NotNull String getRawId() {
        return rawId;
    }
}
