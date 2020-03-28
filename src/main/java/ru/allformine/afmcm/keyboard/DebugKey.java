package ru.allformine.afmcm.keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import ru.allformine.afmcm.ModStatics;
import ru.allformine.afmcm.gui.DebugGui;
import ru.allformine.afmcm.messaging.MessageDispatcher;
import ru.allformine.afmcm.messaging.MessageType;

public class DebugKey implements KeyBind {
    private static KeyBinding keyBinding = new KeyBinding(I18n.format("afmcm.key.debugmode"), Keyboard.KEY_DECIMAL, ModStatics.category);

    @Override
    public void onPress(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            MessageDispatcher.displayMessage(MessageType.NOTIFY_MESSAGE, "Test test test test test test test test 123");

            return;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
            MessageDispatcher.displayMessage(MessageType.WINDOWED_MESSAGE, "Windowed message test!");

            return;
        }

        Minecraft.getMinecraft().displayGuiScreen(new DebugGui());
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }
}
