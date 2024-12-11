package me.udnek.itemscoreu.customcomponent.instance;

import com.google.errorprone.annotations.Immutable;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class RepairableWithCustomItem implements CustomComponent<CustomItem> {

    public static final RepairableWithCustomItem DEFAULT = new RepairableWithCustomItem(Set.of());

    protected Set<CustomItem> repairs;

    public RepairableWithCustomItem(@NotNull Set<@NotNull CustomItem> customItems){
        repairs = customItems;
    }

    public @NotNull Set<@NotNull CustomItem> getRepairs() {
        return repairs;
    }

    public boolean canBeRepairedWith(@NotNull CustomItem customItem){
        return repairs.contains(customItem);
    }

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return CustomComponentType.REPAIRABLE_WITH_CUSTOM_ITEM;
    }
}
