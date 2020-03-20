package ru.allformine.afmcm.keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import ru.allformine.afmcm.ModStatics;
import ru.allformine.afmcm.gui.DebugGui;

public class DebugKey implements KeyBind {
    private static KeyBinding keyBinding = new KeyBinding(I18n.format("afmcm.key.debugmode"), Keyboard.KEY_DECIMAL, ModStatics.category);

    @Override
    public void onPress(InputEvent.KeyInputEvent event) {
        System.out.println("Anus");
        Minecraft.getMinecraft().displayGuiScreen(new DebugGui());
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }
}
