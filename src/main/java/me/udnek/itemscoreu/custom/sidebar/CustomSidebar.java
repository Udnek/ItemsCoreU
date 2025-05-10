package me.udnek.itemscoreu.custom.sidebar;

import me.udnek.itemscoreu.nms.NmsUtils;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class CustomSidebar {
    private final String id;
    private Component title;
    private Objective obj;
    private Map<Integer, Component> lines = new HashMap<>();
    private final List<Player> players = new ArrayList<>();

    public CustomSidebar(@NotNull String id, @NotNull Component title) {
        this.id = id;
        this.title = title;
    }

    public void hide(@NotNull Player player){
        players.remove(player);
        NmsUtils.sendPacket(player, new ClientboundSetObjectivePacket(obj, 1));
        NmsUtils.sendPacket(player, new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, null));
    }

    public void show(@NotNull Player player){
        players.add(player);
        obj = new Scoreboard().addObjective(id, ObjectiveCriteria.DUMMY, NmsUtils.toNmsComponent(title) ,
                ObjectiveCriteria.RenderType.INTEGER, false, BlankFormat.INSTANCE);
        NmsUtils.sendPacket(player, new ClientboundSetObjectivePacket(obj, 0));
        NmsUtils.sendPacket(player, new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, obj));

        for (Map.Entry<Integer, Component> entry : lines.entrySet()) {
            NmsUtils.sendPacket(player,
                    new ClientboundSetScorePacket(entry.getKey().toString(),
                            id,
                            entry.getKey(),
                            Optional.of(NmsUtils.toNmsComponent(entry.getValue())),
                            Optional.of(BlankFormat.INSTANCE)));
        }
    }

    public void updateForAll() {
        players.forEach(this::update);
    }

    public void update(@NotNull Player player) {
        NmsUtils.sendPacket(player, new ClientboundSetObjectivePacket(obj, 2));
        for (Map.Entry<Integer, Component> entry : lines.entrySet()) {
            NmsUtils.sendPacket(player,
                    new ClientboundSetScorePacket(entry.getKey().toString(),
                            id,
                            entry.getKey(),
                            Optional.of(NmsUtils.toNmsComponent(entry.getValue())),
                            Optional.of(BlankFormat.INSTANCE)));
        }
    }

    public void setTitle(@NotNull Component title) {
        this.title = title;
    }

    public @NotNull Map<Integer, Component> getLines() {
        return lines;
    }

    public void setLines(@NotNull Map<Integer, Component> lines) {
        this.lines = new HashMap<>(lines);
    }

    public void setLines(@NotNull List<Component> lines) {
        for (Component component: lines)addLine(component);
    }

    public void setLine(int position, @Nullable Component component) {
        if (component == null) {
            if (lines.isEmpty() || lines.get(position) == null) return;
            this.lines.remove(position);
        } else this.lines.put(position, component);
    }

    public void addLine(@NotNull Component component){
        if (lines.isEmpty()) this.lines.put(1, component);
        else this.lines.put(Collections.min(lines.keySet()) - 1, component);
    }
}
