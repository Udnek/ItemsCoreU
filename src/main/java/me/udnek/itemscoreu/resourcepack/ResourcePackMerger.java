package me.udnek.itemscoreu.resourcepack;

import me.udnek.itemscoreu.resourcepack.filetype.FileType;
import me.udnek.itemscoreu.resourcepack.path.RPPath;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ResourcePackMerger {

    private static final String[] MERGE_DIRECTORIES = new String[]{
            "assets/minecraft/models/item"
    };

    public static final String EXTRACT_DIRECTORY = "C:/Users/glebd/Downloads/rpTest";

    public ResourcePackMerger(){}

    public void startGlobalMerging(){
        LogUtils.pluginLog("ResourcePack merging started");
        List<VirtualResourcePack> resourcePacks = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (!(plugin instanceof ResourcePackablePlugin resourcePackable)) continue;
            LogUtils.pluginLog("Found resourcePackable plugin: " + plugin.getName());
            resourcePacks.add(resourcePackable.getResourcePack());
        }

        for (VirtualResourcePack resourcePack : resourcePacks) {
            resourcePack.initialize();
        }

        for (VirtualResourcePack resourcePack : resourcePacks) {
            for (RPPath rpFile : resourcePack.getAllFoundFiles()) {


                String folderPath = FileManager.joinPaths(EXTRACT_DIRECTORY, FileManager.oneLayerUp(rpFile.getPath()));
                String filePath = FileManager.joinPaths(EXTRACT_DIRECTORY, rpFile.getPath());

                LogUtils.pluginLog(folderPath);
                LogUtils.pluginLog(filePath);

                new File(folderPath).mkdirs();

                File file = new File(filePath);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (rpFile.getFileType() == FileType.PNG){
                    try {
                        BufferedImage image = ImageIO.read(resourcePack.getFile(rpFile));
                        ImageIO.write(image, "png", file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    try {
                        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(filePath));
                        InputStream stream = resourcePack.getFile(rpFile);
                        BufferedReader packFile = new BufferedReader(new InputStreamReader(stream));

                        List<String> lines = packFile.lines().toList();

                        final long count = lines.size();

                        final int[] i = {0};
                        final boolean suppress = count > 80;
                        lines.forEach(new Consumer<String>() {

                            @Override
                            public void accept(String line) {

                                debug(line);
                                try {
                                    bufferedWriter.write(line + "\n");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                i[0]++;
                            }

                            public void debug(String line){
                                if (suppress){
                                    if(i[0] > 5 && i[0] < count-5) return;
                                    LogUtils.pluginLog(line);
                                    if (i[0] == 5){
                                        LogUtils.pluginLog("................");
                                        LogUtils.pluginLog("..*suppressed*..");
                                        LogUtils.pluginLog("................");
                                    }
                                }
                                else {
                                    LogUtils.pluginLog(line);
                                }

                            }
                        });

                        bufferedWriter.close();
                        packFile.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }



    }

}
