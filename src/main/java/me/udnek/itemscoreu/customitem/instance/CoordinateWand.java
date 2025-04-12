package me.udnek.itemscoreu.customitem.instance;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customcomponent.instance.LeftClickableItem;
import me.udnek.itemscoreu.customcomponent.instance.RightClickableItem;
import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CoordinateWand extends ConstructableCustomItem {

    public static final NamespacedKey ORIGIN_KEY = new NamespacedKey(ItemsCoreU.getInstance(), "origin");

    @Override
    public @NotNull String getRawId() {
        return "coordinate_wand";
    }

    @Override
    public @NotNull ItemStack update(@NotNull ItemStack itemStack) {
        return itemStack;
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();

        getComponents().set((RightClickableItem) (item, event) -> {
            event.setCancelled(true);
            Player player = event.getPlayer();
            Location location = player.getLocation();

            List<Double> origin = Objects.requireNonNull(event.getItem())
                    .getPersistentDataContainer().get(ORIGIN_KEY, PersistentDataType.LIST.doubles());
            if (origin == null || origin.isEmpty()) {origin = List.of(0d, 0d, 0d);}

            String x = rounding(location.getX() - origin.getFirst());
            String y = rounding(location.getY() - origin.get(1));
            String z = rounding(location.getZ() - origin.get(2));

            Component component = Component.text("Location: ").color(NamedTextColor.GOLD);
            TextComponent copy = Component.text("[X: " + x + ", Y: " + y + ", Z: " + z + ", Yaw: " + rounding(location.getYaw()) + ", Pitch: "
                            + rounding(location.getPitch()) + "]")
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, x + " " + y + " " + z + " " + rounding(location.getYaw()) + " "
                            + rounding(location.getPitch())));

            event.getPlayer().sendMessage(component.append(copy
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to copy")))
                    .color(NamedTextColor.GREEN)));
        });
        getComponents().set((LeftClickableItem) (item, event) -> {
            event.setCancelled(true);
            Block block = event.getClickedBlock();
            if (block == null) return;
            Location location = block.getLocation();

            List<Double> origin =  Objects.requireNonNull(event.getItem())
                    .getPersistentDataContainer().get(ORIGIN_KEY, PersistentDataType.LIST.doubles());
            if (origin == null || origin.isEmpty()) {origin = List.of(0d, 0d, 0d);}

            String x = rounding(location.getX() - origin.getFirst());
            String y = rounding(location.getY() - origin.get(1));
            String z = rounding(location.getZ() - origin.get(2));

            Component component = Component.text("Block Location: ").color(NamedTextColor.GOLD);
            TextComponent copy = Component.text("[X: " + x + ", Y: " + y + ", Z: " + z + "]")
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, x + " " + y + " " + z));

            event.getPlayer().sendMessage(component.append(copy
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to copy")))
                    .color(NamedTextColor.GREEN)));
        });
    }

    public String rounding(double number) {
        return String.format("%.2f", number);
    }
}
