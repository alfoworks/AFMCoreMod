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
        final int titleAlpha = 160 + (int) (85.0D * Math.sin(cycle * 2 * Math.PI));

        int titleX = scaledResolution.getScaledWidth() - mc.fontRenderer.getStringWidth("Notification") / 2;
        mc.fontRenderer.drawString("Notification", titleX, 31, Utils.colorARGBtoInt(titleAlpha, 255, 0, 0), false);

        //Очень костыльно, чо поделаешь (
        int textX = scaledResolution.getScaledWidth() - mc.fontRenderer.getStringWidth("AAAAAAAAAAAAAAAAAAAA") / 2;

        for (int i = 0; i < text.length; i++) {
            int textY = 40 + i * mc.fontRenderer.FONT_HEIGHT;
            mc.fontRenderer.drawString(text[i], textX, textY, 0xFFFFFF);
        }
    }
}
