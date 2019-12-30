package ru.allformine.afmcm.keyboard;

import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;
import java.util.List;

public class KeyBinder {
    public static List<KeyBind> keyBinds = new ArrayList<>();

    public static void register(KeyBind keyBind){
        keyBinds.add(keyBind);
        ClientRegistry.registerKeyBinding(keyBind.getBinding());
    }
}