package me.udnek.itemscoreu.customadvancement;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.NotNull;

public class CustomAdvancementBuilder {

    private String id;

    public CustomAdvancementBuilder(@NotNull String id){
        this.id = id;
    }

    public @NotNull CustomAdvancementBuilder display(ItemStack itemStack, Component title, AdvancementDisplay.Frame frame, boolean showToast, boolean announceInChat, Component ...description){
        return this;
    }

}
