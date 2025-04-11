package me.udnek.itemscoreu.customitem.instance;

import me.udnek.itemscoreu.customcomponent.instance.LeftClickableItem;
import me.udnek.itemscoreu.customcomponent.instance.RightClickableItem;
import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoordinateWand extends ConstructableCustomItem {
    @Override
    public @NotNull String getRawId() {
        return "coordinate_wand";
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();

        getComponents().set((RightClickableItem) (item, event) -> {
            event.setCancelled(true);
            Player player = event.getPlayer();
            Location location = player.getLocation();


            player.sendMessage(Component.text("Location: ").color(NamedTextColor.GOLD).append(
                    Component.text("[X: " + rounding(location.getX()) + ", Y: " + rounding(location.getY()) + ", Z: " + rounding(location.getZ()) + ", Yaw"
                                    + rounding(location.getYaw()) + ", Pitch:" + rounding(location.getPitch()) + "]")
                        .color(NamedTextColor.GREEN)
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, rounding(location.getX()) + " " + rounding(location.getY()) + " "
                                + rounding(location.getZ()) + " " + rounding(location.getYaw()) + " " + rounding(location.getPitch())))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to copy")))));
        });
        getComponents().set((LeftClickableItem) (item, event) -> {
            event.setCancelled(true);
            Block block = event.getClickedBlock();
            if (block == null) return;
            Location location = block.getLocation();

            event.getPlayer().sendMessage(Component.text("Block Location: ").color(NamedTextColor.GOLD).append(
                    Component.text("[X: " + rounding(location.getBlockX()) + ", Y: " + rounding(location.getBlockY()) + ", Z: " + rounding(location.getBlockZ()) + "]")
                            .color(NamedTextColor.GREEN)
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, location.getBlockX() + " " + location.getBlockY() + " "
                                    + location.getBlockZ()))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to copy")))));
        });
    }

    public String rounding(double number) {
        return String.format("%.2f", number);
    }
}
