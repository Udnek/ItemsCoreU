package me.udnek.itemscoreu.customregistry;

import me.udnek.itemscoreu.customevent.InitializationEvent;
import me.udnek.itemscoreu.util.LogUtils;
import me.udnek.itemscoreu.customitem.VanillaItemManager;

public class InitializationProcess {

    public static void start(){
        new InitializationEvent(Step.BEFORE_REGISTRIES_INITIALIZATION).callEvent();
        LogUtils.pluginLog("Registries After Initialization started");
        for (CustomRegistry<?> registry : CustomRegistries.REGISTRY.getAll()) {
            for (Registrable registrable : registry.getAll()) {
                registrable.afterInitialization();
            }
        }
        new InitializationEvent(Step.AFTER_REGISTRIES_INITIALIZATION).callEvent();

        new InitializationEvent(Step.BEFORE_VANILLA_MANAGER).callEvent();
        LogUtils.pluginLog("VanillaManager started");
        VanillaItemManager.getInstance().start();

        new InitializationEvent(Step.AFTER_VANILLA_MANGER).callEvent();
        new InitializationEvent(Step.END).callEvent();
    }



    public enum Step{
        BEFORE_REGISTRIES_INITIALIZATION,
        AFTER_REGISTRIES_INITIALIZATION,
        BEFORE_VANILLA_MANAGER,
        AFTER_VANILLA_MANGER,
        END
    }
}
