package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.utils.CustomRegistry;

// TODO: 7/18/2024 STORE IN MAP????
public class CustomAttributeRegistry extends CustomRegistry<CustomAttribute> {


    private static CustomAttributeRegistry instance;
    private CustomAttributeRegistry(){}
    public static CustomAttributeRegistry getInstance() {
        if (instance == null) instance = new CustomAttributeRegistry();
        return instance;

    }

    @Override
    public String getCategory() {
        return "Attribute";
    }

    @Override
    protected String getIdToLog(CustomAttribute custom) {
        return custom.getId();
    }

    @Override
    protected void put(CustomAttribute custom) {
    }

}
