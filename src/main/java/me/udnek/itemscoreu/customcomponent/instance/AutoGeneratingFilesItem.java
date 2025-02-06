package me.udnek.itemscoreu.customcomponent.instance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.papermc.paper.datacomponent.DataComponentTypes;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.resourcepack.path.VirtualRpJsonFile;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface AutoGeneratingFilesItem extends CustomComponent<CustomItem> {

    AutoGeneratingFilesItem GENERATED = new Generated();
    AutoGeneratingFilesItem HANDHELD = new HandHeld();
    AutoGeneratingFilesItem CUSTOM_MODEL_DATA_COLORABLE = new CustomModelDataColorable();

    @NotNull List<VirtualRpJsonFile> getFiles(@NotNull CustomItem customItem);

    @Override
    default @NotNull CustomComponentType<CustomItem, ?> getType() {return CustomComponentType.AUTO_GENERATING_FILES_ITEM;}

    interface Base extends AutoGeneratingFilesItem{
        @Override
        @NotNull
        default List<VirtualRpJsonFile> getFiles(@NotNull CustomItem customItem){
            Key itemModel = customItem.getItem().getData(DataComponentTypes.ITEM_MODEL);
            if (itemModel == null || itemModel.namespace().equals(Key.MINECRAFT_NAMESPACE)) return List.of();
            List<VirtualRpJsonFile> files = new ArrayList<>();
            files.add(new VirtualRpJsonFile(getModel(itemModel), getModelPath(itemModel)));
            files.add(new VirtualRpJsonFile(getDefinition(itemModel), getDefinitionPath(itemModel)));
            return files;
        }

        default @NotNull String getModelPath(@NotNull Key itemModel){
            return "assets/" + itemModel.namespace() + "/models/item/" + itemModel.value() + ".json";
        }
        default @NotNull String getDefinitionPath(@NotNull Key itemModel){
            return "assets/" + itemModel.namespace() + "/items/" + itemModel.value() + ".json";
        }

        @NotNull JsonObject getModel(@NotNull Key modelKey);
        @NotNull JsonObject getDefinition(@NotNull Key modelKey);
    }
    class Generated implements Base{
        @Override
        public @NotNull JsonObject getModel(@NotNull Key modelKey) {
            return (JsonObject) JsonParser.parseString("""
                        {
                            "parent": "minecraft:item/generated",
                            "textures": {
                                "layer0": "%namespace%:item/%key%"
                            }
                        }
                        """.replace("%namespace%", modelKey.namespace()).replace("%key%", modelKey.value()));
        }
        @Override
        public @NotNull JsonObject getDefinition(@NotNull Key modelKey) {
            return (JsonObject) JsonParser.parseString( """
                        {
                            "model": {
                                "type": "minecraft:model",
                                "model": "%namespace%:item/%key%"
                            }
                        }
                        """.replace("%namespace%", modelKey.namespace()).replace("%key%", modelKey.value()));
        }
    }
    class HandHeld extends Generated{

        @Override
        public @NotNull JsonObject getModel(@NotNull Key modelKey) {
            @Nullable JsonObject model = super.getModel(modelKey);
            model.addProperty("parent", "minecraft:item/handheld");
            return model;
        }
    }

    class CustomModelDataColorable extends Generated{
        @Override
        public @NotNull JsonObject getDefinition(@NotNull Key modelKey) {
            JsonObject definition = super.getDefinition(modelKey);
            JsonElement tints = JsonParser.parseString(
                    """
                            {
                            "tints": [
                                  {
                                    "type": "minecraft:custom_model_data",
                                    "default": 0
                                  }
                            ]
                            }
                            """).getAsJsonObject().get("tints");
            definition.get("model").getAsJsonObject().add("tints", tints);
            return definition;
        }
    }
}
