package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class NotifyGui extends Gui {
    private String text;
    private Minecraft mc;

    public NotifyGui(String text, Minecraft mc) {
        this.text = text;
        this.mc = mc;
        drawScreen();
    }

    private void drawScreen() {


        String s = "[" + this.text + EnumChatFormatting.WHITE + "]";
        this.mc.fontRenderer.drawStringWithShadow(s,
                10,
                10,
                16777215);
    }
}
