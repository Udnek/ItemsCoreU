package me.udnek.coreu.resourcepack;

import me.udnek.coreu.CoreU;
import me.udnek.coreu.resourcepack.host.ResourcePackHost;
import me.udnek.coreu.resourcepack.host.ResourcePackUtils;
import me.udnek.coreu.resourcepack.merger.ResourcePackMerger;
import me.udnek.coreu.serializabledata.SerializableData;
import me.udnek.coreu.serializabledata.SerializableDataManager;
import me.udnek.coreu.util.LogUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ResourcePackCommand implements TabExecutor, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof ConsoleCommandSender)){
            commandSender.sendMessage("Command can be executed in console only!");
            return true;
        }

        if (args.length > 1) return false;


        ResourcepackInfo info = SerializableDataManager.read(new ResourcepackInfo(), CoreU.getInstance());

        if (args.length == 1) {
            info.extractDirectory = args[0];
        }
        else {
            if (info.extractDirectory == null){
                LogUtils.pluginLog("Saved directory is null, specify it using argument!");
                return true;
            }
            LogUtils.pluginLog("Loaded saved directory: " + info.extractDirectory);
        }

        ResourcePackMerger merger = new ResourcePackMerger();
        String error = merger.checkExtractDirectoryAndError(info.extractDirectory);
        if (error != null){
            LogUtils.pluginLog(error);
            return true;
        }

        merger.startMergeInto(info.extractDirectory);

        try {
            ResourcePackMerger mergerHost = new ResourcePackMerger();
            Path path = ResourcePackHost.getPath();
            FileUtils.cleanDirectory(path.toFile());
            mergerHost.startMergeInto(path.toString());
            String checksum = ResourcePackUtils.calculateSHA(path.toFile());
            if (!checksum.equals(info.checksum)){
                ResourcePackUtils.zipFolder(path, path.getParent().toString());
            }
            info.checksum = checksum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SerializableDataManager.write(info, CoreU.getInstance());

        LogUtils.pluginWarning("If your sound does not play, remove '<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>' in plugin's pom!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return null;
    }
}
