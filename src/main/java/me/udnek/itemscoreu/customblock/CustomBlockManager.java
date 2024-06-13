package me.udnek.itemscoreu.customblock;

import me.udnek.itemscoreu.customevent.AllEventListener;
import me.udnek.itemscoreu.customevent.CustomEventManager;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public class CustomBlockManager {

    private static final HashMap<String, CustomBlock> customBlocks = new HashMap<>();


    public static CustomBlock register(JavaPlugin plugin, CustomBlock customBlock){
        customBlock.initialize(plugin);
        put(customBlock);
        if (customBlock instanceof AllEventListener allEventListener) CustomEventManager.addListener(allEventListener);
        return customBlock;
    }

    private static void put(CustomBlock customBlock){
        if (!customBlocks.containsKey(customBlock.getId())){
            customBlocks.put(customBlock.getId(), customBlock);
            LogUtils.pluginLog(customBlock.getId() + " (Block)");
        }
    }

    protected static CustomBlock get(String id){
        return customBlocks.get(id);
    }
    protected static Set<String> getAllIds(){
        return customBlocks.keySet();
    }

}
