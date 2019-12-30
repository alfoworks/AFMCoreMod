package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class FactionsGui extends Gui {
    private String text;
    private Minecraft mc;
    private RenderGameOverlayEvent event;

    public FactionsGui(String text, Minecraft mc, RenderGameOverlayEvent event) {
        this.text = "[" + text + TextFormatting.RESET+"]";
        this.mc = mc;
        this.event = event;
        drawScreen();
    }

    private void drawScreen() {
        ScaledResolution scaledResolution = event.getResolution();
        float width = scaledResolution.getScaledWidth();
        float height = scaledResolution.getScaledHeight();

        mc.fontRenderer.drawStringWithShadow(text, width / 2 + 93, height - this.mc.fontRenderer.FONT_HEIGHT - 2, 0xFFFFFF);
    }
}
