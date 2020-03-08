package ru.allformine.afmcm.handlers.event;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class MPScreenEventHandler {
    List<Integer> buttonIds = Lists.newArrayList(1, 8, 0);

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
                GuiButton button = getButtonById(event.getButtonList(), 1);

                guiButton.x = button.x;
                guiButton.width = button.width / 2;

                event.getButtonList().add(guiButton);
            } else if (guiButton.id == 0) {
                GuiButton button = getButtonById(event.getButtonList(), 1);

                guiButton.width = button.width / 2;
                guiButton.x = (button.x + button.width) - guiButton.width;

                event.getButtonList().add(guiButton);
            }
        });
    }

    private GuiButton getButtonById(List<GuiButton> buttons, int id) {
        for (GuiButton button : buttons) {
            if (button.id == id) return button;
        }

        return buttons.get(0);
    }
}
