package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class TerritoryShowGui extends Gui {
    private String text;
    private Minecraft mc;
    private RenderGameOverlayEvent event;

    public TerritoryShowGui(String text, Minecraft mc, RenderGameOverlayEvent event) {
        this.text = "[" + text + EnumChatFormatting.RESET+"]";
        this.mc = mc;
        this.event = event;
        drawScreen();
    }

    private void drawScreen() {
        ScaledResolution scaledResolution = event.resolution;
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();

        mc.fontRenderer.drawStringWithShadow(text, width / 2 + 93, height - this.mc.fontRenderer.FONT_HEIGHT - 2, 0xFFFFFF);
    }
}
