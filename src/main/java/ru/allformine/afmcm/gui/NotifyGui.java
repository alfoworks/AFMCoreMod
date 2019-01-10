package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

public class NotifyGui extends Gui {
    final double cycle = ((System.nanoTime() / 1000) % 0x200000) / (double) 0x200000;

    private String text;
    private Minecraft mc;
    private RenderGameOverlayEvent event;

    public NotifyGui(String text, Minecraft mc, RenderGameOverlayEvent event) {
        this.text = "[" + text + EnumChatFormatting.WHITE + "]";
        this.mc = mc;
        this.event = event;
        drawScreen();
    }

    private void drawScreen() {
        GL11.glPushMatrix();
        GL11.glScalef(1.2F, 1.2F, 0.0F);

        final int alpha = 160 + (int) (85.0D * Math.sin(cycle * 2 * Math.PI));

        ScaledResolution scaledResolution = event.resolution;
        double x = (scaledResolution.getScaledWidth()*0.5 / 2) - mc.fontRenderer.getStringWidth(text);

        mc.fontRenderer.drawString(text, x, 40, Utils.colorARGBtoInt(alpha, 255, 255, 255), false);

        GL11.glPopMatrix();
    }
}
