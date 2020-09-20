package ru.allformine.afmcm.keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import ru.allformine.afmcm.ModStatics;
import ru.allformine.afmcm.gui.DebugGui;
import ru.allformine.afmcm.gui.FactionsGui;
import ru.allformine.afmcm.messaging.MessageDispatcher;
import ru.allformine.afmcm.messaging.MessageType;

import java.util.Random;

public class  DebugKey implements KeyBind {
    private static KeyBinding keyBinding = new KeyBinding(I18n.format("afmcm.key.debugmode"), Keyboard.KEY_DECIMAL, ModStatics.category);

    @Override
    public void onPress(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            MessageDispatcher.displayMessage(MessageType.NOTIFY_MESSAGE, "Nothing to look at here. Stop.");

            return;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
            MessageDispatcher.displayMessage(MessageType.WINDOWED_MESSAGE, "Anal sex or anal intercourse is generally the insertion and thrusting of the erect penis into a person's anus, or anus and rectum, for sexual pleasure.[1][2][3] Other forms of anal sex include fingering, the use of sex toys for anal penetration, oral sex performed on the anus (anilingus), and pegging.");

            return;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
            FactionsGui.addText(ChatFormatting.values()[new Random().nextInt(ChatFormatting.values().length)] + "Test");

            return;
        }

        Minecraft.getMinecraft().displayGuiScreen(new DebugGui());
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }
}
