package me.udnek.itemscoreu.customattribute.equipmentslot;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customattribute.equipmentslot.instance.*;
import me.udnek.itemscoreu.utils.CustomThingManager;

import java.util.ArrayList;
import java.util.List;

// TODO: 7/18/2024 MAKE JAVA PLUGIN DEPENDENCE
public class CustomEquipmentSlots extends CustomThingManager<CustomEquipmentSlot> {
    private static final List<CustomEquipmentSlot> registeredSlots = new ArrayList<>();
    private static CustomEquipmentSlots instance;

    public static final CustomEquipmentSlot MAIN_HAND = register(new MainHandSlot());
    public static final CustomEquipmentSlot OFF_HAND = register(new OffHandSlot());
    public static final CustomEquipmentSlot HAND = register(new HandSlot());

    public static final CustomEquipmentSlot HEAD = register(new HeadSlot());
    public static final CustomEquipmentSlot CHEST = register(new ChestSlot());
    public static final CustomEquipmentSlot LEGS = register(new LegsSlot());
    public static final CustomEquipmentSlot FEET = register(new FeetSlot());

    public static final CustomEquipmentSlot ARMOR = register(new ArmorSlot());

    public static CustomEquipmentSlot register(CustomEquipmentSlot equipmentSlot){
        return getInstance().register(ItemsCoreU.getInstance(), equipmentSlot);
    }


    private CustomEquipmentSlots(){}
    public static CustomEquipmentSlots getInstance() {
        if (instance == null) instance = new CustomEquipmentSlots();
        return instance;
    }

    public static List<CustomEquipmentSlot> getAllSlots(){
        return new ArrayList<>(registeredSlots);
    }

    @Override
    public String getCategory() {
        return "Equipment Slot";
    }

    @Override
    protected String getIdToLog(CustomEquipmentSlot custom) {
        return custom.getClass().getName();
    }

    @Override
    protected void put(CustomEquipmentSlot custom) {
        registeredSlots.add(custom);
    }
}
