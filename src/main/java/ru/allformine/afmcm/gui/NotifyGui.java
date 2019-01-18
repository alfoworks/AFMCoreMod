package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class NotifyGui extends Gui {
    private final double cycle = ((System.nanoTime() / 1000) % 0x200000) / (double) 0x200000;

    private String[] text;
    private Minecraft mc;
    private RenderGameOverlayEvent event;

    public NotifyGui(String text, Minecraft mc, RenderGameOverlayEvent event) {
        this.text = text.split("(?<=\\G.{20})");
        this.mc = mc;
        this.event = event;
        drawScreen();
    }

    private void drawScreen() {
        ScaledResolution scaledResolution = event.resolution;
        int width = scaledResolution.getScaledWidth();
        int titleAlpha = 160 + (int) (85.0D * Math.sin(cycle * 2 * Math.PI));

        drawCenteredString(mc.fontRenderer, "Notification", width / 2, 41, Utils.colorARGBtoInt(titleAlpha, 255, 0, 0));

        for (int i = 0; i < text.length; i++) {
            int textY = 50 + i * mc.fontRenderer.FONT_HEIGHT;
            drawCenteredString(mc.fontRenderer, text[i], width / 2, textY, 0xFFFFFF);
        }
    }
}
