package me.udnek.itemscoreu.customattribute.equipmentslot;

import me.udnek.itemscoreu.customattribute.equipmentslot.instance.*;

import java.util.ArrayList;
import java.util.List;

public class CustomEquipmentSlots {
    private static final List<CustomEquipmentSlot> registeredSlots = new ArrayList<>();

    public static final CustomEquipmentSlot MAIN_HAND = register(new MainHandSlot());
    public static final CustomEquipmentSlot OFF_HAND = register(new OffHandSlot());

    public static final CustomEquipmentSlot HEAD = register(new HeadSlot());
    public static final CustomEquipmentSlot CHEST = register(new ChestSlot());
    public static final CustomEquipmentSlot LEGS = register(new LegsSlot());
    public static final CustomEquipmentSlot FEET = register(new FeetSlot());

    public static CustomEquipmentSlot register(CustomEquipmentSlot equipmentSlot){
        registeredSlots.add(equipmentSlot);
        return equipmentSlot;
    }

    public static List<CustomEquipmentSlot> getAllSlots(){
        return new ArrayList<>(registeredSlots);
    }
}
