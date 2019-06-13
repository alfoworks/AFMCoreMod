package ru.allformine.afmcm.keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public interface KeyBind {
    void onPress(InputEvent.KeyInputEvent event);
    KeyBinding getBinding();
}
