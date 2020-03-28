package ru.allformine.afmcm.messaging;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class WindowedMessageGuiScreen extends GuiScreen {
    @Override
    public void initGui() {
        super.initGui();

        ScaledResolution resolution = new ScaledResolution(mc);
        int windowWidth = 140;
        int windowHeight = 100;

        int posX = center(windowWidth, resolution.getScaledWidth());
        int posY = center(windowHeight, resolution.getScaledHeight());

        int buttonWidth = 40;
        int buttonHeight = 20;

        int buttonPosX = posX - buttonWidth - 5;
        int buttonPosY = posY - buttonHeight - 5;

        this.buttonList.add(new GuiButton(buttonPosX, buttonPosY, buttonWidth, "Close"));
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        this.drawDefaultBackground();

        mc.renderEngine.bindTexture(new ResourceLocation("afmcm", "textures/windowed_message.png"));

        ScaledResolution resolution = new ScaledResolution(mc);
        int windowWidth = 140;
        int windowHeight = 100;

        int posX = center(windowWidth, resolution.getScaledWidth());
        int posY = center(windowHeight, resolution.getScaledHeight());

        mc.ingameGUI.drawTexturedModalRect(posX, posY, 0, 0, windowWidth, windowHeight);

        drawString(mc.fontRenderer, I18n.format("afmcm.text.message"), posX + 7, posY + 3, 0xFFFFFF);

        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    private int center(int a, int b) {
        return (b - a) / 2;
    }
}
