package me.udnek.itemscoreu.customadvancement;

import com.google.common.collect.ImmutableMap;
import jline.internal.Log;
import me.udnek.itemscoreu.utils.ItemUtils;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.advancements.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomAdvancementUtils {

    public static @NotNull ConstructableCustomAdvancement itemAdvancement(@NotNull Key key, @NotNull ItemStack itemStack){
        ConstructableCustomAdvancement advancement = new ConstructableCustomAdvancement(key);
        advancement.addCriterion(AdvancementCriterion.INVENTORY_CHANGE.create(itemStack));
        CustomAdvancementDisplayBuilder display = new CustomAdvancementDisplayBuilder(itemStack);
        //LogUtils.log(itemStack.displayName());
        //LogUtils.log(JSONComponentSerializer.json().serialize(ItemUtils.getDisplayName(itemStack)));
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

        Reflex.setFieldValue(manager, "advancements", advancements);
        Reflex.setFieldValue(manager, "tree", tree);

        if (advancementContainer.getDisplay() == null) return;
        DisplayInfo displayInfo = (DisplayInfo) Reflex.getFieldValue(advancementHolder.value(), "display", Optional.class).orElse(null);
        if (displayInfo == null) return;

        displayInfo.setLocation(
                advancementContainer.getDisplay().x == null ? displayInfo.getX() : advancementContainer.getDisplay().x,
                advancementContainer.getDisplay().y == null ? displayInfo.getY() : advancementContainer.getDisplay().y
        );
    }
}
