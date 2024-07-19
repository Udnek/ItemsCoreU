package me.udnek.itemscoreu.customblock;

import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlots;
import me.udnek.itemscoreu.customevent.AllEventListener;
import me.udnek.itemscoreu.customevent.CustomEventManager;
import me.udnek.itemscoreu.utils.CustomThingManager;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public class CustomBlockManager extends CustomThingManager<CustomBlock> {

    private static final HashMap<String, CustomBlock> customBlocks = new HashMap<>();

    private static CustomBlockManager instance;
    private CustomBlockManager(){}
    public static CustomBlockManager getInstance() {
        if (instance == null) instance = new CustomBlockManager();
        return instance;

    }


    @Override
    public String getCategory() {
        return "Block";
    }

    @Override
    protected String getIdToLog(CustomBlock custom) {
        return custom.getId();
    }

    protected void put(CustomBlock customBlock){
        customBlocks.put(customBlock.getId(), customBlock);
    }

    protected static CustomBlock get(String id){
        return customBlocks.get(id);
    }
    protected static Set<String> getAllIds(){
        return customBlocks.keySet();
    }

}
