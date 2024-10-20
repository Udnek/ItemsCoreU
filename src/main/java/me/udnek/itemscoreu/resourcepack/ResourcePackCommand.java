package me.udnek.itemscoreu.resourcepack;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.resourcepack.merger.ResourcePackMerger;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.itemscoreu.util.LogUtils;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResourcePackCommand implements TabExecutor, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof ConsoleCommandSender)){
            commandSender.sendMessage("Command can be executed in console only!");
            return true;
        }

        if (args.length > 1) return false;

        String directory;

        if (args.length == 1) {
            directory = args[0];
        }
        else {
            ResourcepackSettings settings = new ResourcepackSettings(null);
            SerializableDataManager.read(settings, ItemsCoreU.getInstance());
            if (settings.getExtractDirectory() == null){
                LogUtils.pluginLog("Saved directory is null, specify it using argument!");
                return true;
            }
            directory = settings.getExtractDirectory();
            LogUtils.pluginLog("Loaded saved directory: " + directory);
        }

        ResourcePackMerger merger = new ResourcePackMerger();
        String error = merger.checkExtractDirectory(directory);
        if (error != null){
            LogUtils.pluginLog(error);
            return true;
        }

        SerializableDataManager.write(new ResourcepackSettings(directory), ItemsCoreU.getInstance());

        merger.startGlobalMerging(directory);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return null;
    }
}
