package ru.allformine.afmcm.handlers.event;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.allformine.afmcm.AFMCoreMod;
import ru.allformine.afmcm.ModConfig;
import ru.allformine.afmcm.ModUtils;

public class DreamHudConfigEventHandler {
    @SubscribeEvent
    public void onGuiScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.getGui() instanceof GuiOptions)) return;

        GuiButton bindedButton = ModUtils.getButtonById(event.getButtonList(), 110);

        int buttonWidth = 150;
        int buttonHeight = 20;
        int buttonX = bindedButton.x;
        int buttonY = bindedButton.y - buttonHeight - 7;

        event.getButtonList().add(new GuiButton(1488, buttonX, buttonY, buttonWidth, buttonHeight, getButtonText()));
    }

    @SubscribeEvent
    public void onGuiScreenAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getButton().id != 1488) return;

        ModConfig.dreamHudEnabled = !ModConfig.dreamHudEnabled;
        AFMCoreMod.config.save();

        event.getButton().displayString = getButtonText();
    }

    // ============================= //

    private String getButtonText() {
        return String.format("DreamHud BETA [%s%s%s]", ModConfig.dreamHudEnabled ? TextFormatting.DARK_GREEN : TextFormatting.DARK_RED, ModConfig.dreamHudEnabled ? "ON" : "OFF", TextFormatting.RESET);
    }
}
