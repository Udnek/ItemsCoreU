package me.udnek.itemscoreu.customadvancement;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import me.udnek.itemscoreu.util.ItemUtils;
import me.udnek.itemscoreu.util.NMS.Reflex;
import net.kyori.adventure.key.Key;
import net.minecraft.advancements.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomAdvancementUtils {

    private static final Map<AdvancementHolder, Pair<Float, Float>> positions = new HashMap<>();

    private static @NotNull Pair<Float, Float> getFromPositions(@NotNull AdvancementHolder advancementHolder){
        for (Map.Entry<AdvancementHolder, Pair<Float, Float>> entry : positions.entrySet()) {
            if (entry.getKey().equals(advancementHolder)) return entry.getValue();
        }
        return new ObjectObjectMutablePair<>(null, null);
    }
    private static void putToPositions(@NotNull CustomAdvancementContainer container){
        if (container.getDisplay() == null) return;
        if (container.getDisplay().x == null && container.getDisplay().y == null) return;
        positions.put(container.get(), new ObjectObjectMutablePair<>(container.getDisplay().x, container.getDisplay().y));
    }
    
    public static @NotNull ConstructableCustomAdvancement itemAdvancement(@NotNull Key key, @NotNull ItemStack itemStack){
        ConstructableCustomAdvancement advancement = new ConstructableCustomAdvancement(key);
        advancement.addCriterion(ItemUtils.getId(itemStack), AdvancementCriterion.INVENTORY_CHANGE.create(itemStack));
        CustomAdvancementDisplayBuilder display = new CustomAdvancementDisplayBuilder(itemStack);
        display.title(ItemUtils.getDisplayName(itemStack));
        advancement.display(display);
        return advancement;
    }

    public static void register(@NotNull CustomAdvancementContainer advancementContainer){
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerAdvancementManager manager = server.getAdvancements();

        AdvancementHolder advancementHolder = advancementContainer.get();

        ImmutableMap.Builder<ResourceLocation, AdvancementHolder> mapBuilder = ImmutableMap.builder();
        Reflex.invokeMethod(
                manager,
                Reflex.getMethod(
                        ServerAdvancementManager.class,
                        "validate",
                        ResourceLocation.class, Advancement.class
                ),
                advancementHolder.id(),
                advancementHolder.value()
        );
        mapBuilder.put(advancementHolder.id(), advancementHolder);

        Map<ResourceLocation, AdvancementHolder> advancements = new HashMap<>(mapBuilder.buildOrThrow());
        advancements.putAll((Map<ResourceLocation, AdvancementHolder>) Reflex.getFieldValue(manager, "advancements"));
        AdvancementTree tree = new AdvancementTree();
        tree.addAll(advancements.values());
        for (AdvancementNode advancementnode : tree.roots()) {
            if (advancementnode.holder().value().display().isPresent()) {
                TreeNodePosition.run(advancementnode);
            }
        }
        putToPositions(advancementContainer);

        for (AdvancementHolder advancement : advancements.values()) {
            DisplayInfo displayInfo = (DisplayInfo) Reflex.getFieldValue(advancement.value(), "display", Optional.class).orElse(null);
            if (displayInfo == null) continue;
            Pair<Float, Float> pos = getFromPositions(advancement);
            displayInfo.setLocation(
                    pos.left() == null ? displayInfo.getX() : pos.left(),
                    pos.right() == null ? displayInfo.getY() : pos.right()
            );
        }

        Reflex.setFieldValue(manager, "advancements", advancements);
        Reflex.setFieldValue(manager, "tree", tree);

        DisplayInfo displayInfo = (DisplayInfo) Reflex.getFieldValue(advancementHolder.value(), "display", Optional.class).orElse(null);
        if (displayInfo == null) return;

        for (@NotNull CustomAdvancementContainer fake : advancementContainer.getFakes()) {
            CustomAdvancementDisplayBuilder fakeDisplay = fake.getDisplay();
            if (fakeDisplay != null){
                System.out.println("settingFake: " + displayInfo.getX() + " "  + displayInfo.getY());
                fakeDisplay.x(displayInfo.getX());
                fakeDisplay.y(displayInfo.getY());
            }
            register(fake);
        }
        

    }
}
