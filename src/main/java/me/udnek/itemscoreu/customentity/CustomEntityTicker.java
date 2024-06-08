package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.utils.TickingTask;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class CustomEntityTicker extends TickingTask {

    private final List<CustomDumbEntityHolder> loadedEntities = new ArrayList<>();

    public void addEntity(Entity entity, CustomDumbTickingEntity customDumbTickingEntity){
        for (CustomDumbEntityHolder loadedEntity : loadedEntities) {
            if (loadedEntity.entity == entity) return;
        }
        loadedEntities.add(new CustomDumbEntityHolder(entity, customDumbTickingEntity));
        customDumbTickingEntity.onLoad(entity);
    }

    public void removeEntity(Entity entity){
        for (int i = 0; i < loadedEntities.size(); i++) {
            CustomDumbEntityHolder dumbEntityHolder = loadedEntities.get(i);
            if (dumbEntityHolder.entity == entity){
                loadedEntities.remove(i);
                dumbEntityHolder.customDumbTickingEntity.onUnload(dumbEntityHolder.entity);
                return;
            }
        }
    }
    @Override
    public void run() {

        List<Entity> toRemoveEntities = new ArrayList<>();

        for (CustomDumbEntityHolder dumbEntityHolder : loadedEntities) {
            if (!dumbEntityHolder.entity.isValid()) {
                toRemoveEntities.add(dumbEntityHolder.entity);
                continue;
            }
            dumbEntityHolder.customDumbTickingEntity.tick(dumbEntityHolder.entity);
        }

        for (Entity entity : toRemoveEntities) {
            removeEntity(entity);
        }

    }


    @Override
    public int getDelay() {
        return 10;
    }

}
