package me.udnek.itemscoreu.custom.minigame;

import me.udnek.itemscoreu.custom.minigame.game.MGUGameInstance;
import org.jetbrains.annotations.NotNull;

public class MGUId {

    private final String id;

    protected MGUId(@NotNull String id){
        this.id = id;
    }

    public @NotNull String asString(){return id;}

    @Override
    public String toString() {
        return "MGUId{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MGUId mguId && mguId.id.equals(this.id);
    }

    public static @NotNull MGUId generateNew(@NotNull MGUGameInstance mguGameInstance){
        return new MGUId(mguGameInstance.getType().getId());
        //return new MGUId(mguGameInstance.getType().getId() + "_" + UUID.randomUUID().toString().substring(0, 5));
    }
}
