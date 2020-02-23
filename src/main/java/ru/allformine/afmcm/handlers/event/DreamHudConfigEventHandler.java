package ru.allformine.afmcm.handlers.event;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.allformine.afmcm.AFMCoreMod;
import ru.allformine.afmcm.ModConfig;

public class DreamHudConfigEventHandler {
    @SubscribeEvent
    public void onGuiScreenInit(GuiScreenEvent.InitGuiEvent event) {
        if (!(event.getGui() instanceof GuiOptions)) return;

        event.getButtonList().add(new GuiButton(1488, 0, 0, 130, 20, getButtonText()));
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
