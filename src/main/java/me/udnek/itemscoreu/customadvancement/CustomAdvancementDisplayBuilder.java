package me.udnek.itemscoreu.customadvancement;

import io.papermc.paper.advancement.AdvancementDisplay;
import me.udnek.itemscoreu.nms.NmsUtils;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CustomAdvancementDisplayBuilder {
    @NotNull ItemStack icon;
    @Nullable String background;
    @Nullable Component title;
    @Nullable Component description;
    @NotNull AdvancementDisplay.Frame frame = AdvancementDisplay.Frame.TASK;
    boolean showToast = true;
    boolean announceToChat = true;
    boolean hidden = false;
    @Nullable Float x;
    @Nullable Float y;

    public CustomAdvancementDisplayBuilder(@NotNull ItemStack icon) {
        this.icon = icon;
    }

    public @NotNull CustomAdvancementDisplayBuilder defaultSettings(){
        return hidden(false).showToast(true).announceToChat(true).background(null);
    }
    public @NotNull CustomAdvancementDisplayBuilder rootSettings(@NotNull String background){
        return hidden(false).showToast(false).announceToChat(false).background(background);
    }

    public @NotNull CustomAdvancementDisplayBuilder icon(@NotNull ItemStack itemStack){
        this.icon = itemStack; return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder x(@Nullable Float x){
        this.x = x; return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder y(@Nullable Float y){
        this.y = y; return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder title(@Nullable Component component){
        title = component; return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder title(@Nullable net.kyori.adventure.text.Component component){
        if (component == null) title = null;
        else title = CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(component));
        return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder title(@Nullable String string){
        if (string == null) return title((net.kyori.adventure.text.Component) null);
        return title(Component.translatable(string));
    }
    public @NotNull CustomAdvancementDisplayBuilder description(@Nullable Component component){
        description = component; return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder description(@Nullable net.kyori.adventure.text.Component component){
        if (component == null) description = null;
        else description = CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(component));
        return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder description(@Nullable String string){
        if (string == null) return description((net.kyori.adventure.text.Component) null);
        return description(Component.translatable(string));
    }
    public @NotNull CustomAdvancementDisplayBuilder background(@Nullable String background){
        this.background = background; return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder frame(@NotNull AdvancementDisplay.Frame frame){
        this.frame = frame; return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder hidden(boolean hidden){
        this.hidden = hidden; return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder announceToChat(boolean announceToChat){
        this.announceToChat = announceToChat; return this;
    }
    public @NotNull CustomAdvancementDisplayBuilder showToast(boolean showToast){
        this.showToast = showToast; return this;
    }

    public @NotNull DisplayInfo build(){
        AdvancementType type = switch (frame){
            case GOAL -> AdvancementType.GOAL;
            case CHALLENGE -> AdvancementType.CHALLENGE;
            default -> AdvancementType.TASK;
        };
        return new DisplayInfo(
                NmsUtils.toNmsItemStack(icon),
                title == null ? Component.empty() : title,
                description == null ? Component.empty() : description,
                Optional.ofNullable(background == null ? null : ResourceLocation.parse(background)),
                type,
                showToast,
                announceToChat,
                hidden
        );
    }
}
