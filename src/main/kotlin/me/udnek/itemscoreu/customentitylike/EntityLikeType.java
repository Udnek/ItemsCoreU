package me.udnek.itemscoreu.customentitylike;

import me.udnek.itemscoreu.customregistry.Registrable;
import org.jetbrains.annotations.NotNull;

public interface EntityLikeType<Real> extends Registrable {
    void load(@NotNull Real real);
    void unload(@NotNull Real real);
}
