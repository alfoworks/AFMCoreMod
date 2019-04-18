package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class DreamHudGui {
    public void drawScreen() {
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("afmcm", "textures/dream_hud.png"));
        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 10, 10, 480, 126);
    }
}
