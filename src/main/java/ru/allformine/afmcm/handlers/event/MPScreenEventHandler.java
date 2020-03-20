package ru.allformine.afmcm.handlers.event;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import ru.allformine.afmcm.ModUtils;

import java.util.ArrayList;

public class MPScreenEventHandler {
    @SubscribeEvent
    public void onGuiScreenInit(GuiScreenEvent.InitGuiEvent event) {
        if (!(event.getGui() instanceof GuiMultiplayer)) return;

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) return;

        new ArrayList<>(event.getButtonList()).forEach(guiButton -> {
            event.getButtonList().remove(guiButton);

            if (guiButton.id == 1) {
                guiButton.width = 293;

                event.getButtonList().add(guiButton);
            } else if (guiButton.id == 8) {
                GuiButton button = ModUtils.getButtonById(event.getButtonList(), 1);

                guiButton.x = button.x;
                guiButton.width = button.width / 2;

                event.getButtonList().add(guiButton);
            } else if (guiButton.id == 0) {
                GuiButton button = ModUtils.getButtonById(event.getButtonList(), 1);

                guiButton.width = button.width / 2;
                guiButton.x = (button.x + button.width) - guiButton.width;

                event.getButtonList().add(guiButton);
            }
        });
    }
}
