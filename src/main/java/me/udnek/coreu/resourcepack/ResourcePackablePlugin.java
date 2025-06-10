package me.udnek.coreu.resourcepack;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface ResourcePackablePlugin extends Plugin {
    default @NotNull VirtualResourcePack getResourcePack(){
        return new VirtualResourcePack(this);

    }

    @NotNull Priority getPriority();

    enum Priority {

        MAIN(2),
        BASE(1),
        TECHNICAL(0);

        public final int value;

        Priority(int value){
            this.value = value;
        }

    }
}
