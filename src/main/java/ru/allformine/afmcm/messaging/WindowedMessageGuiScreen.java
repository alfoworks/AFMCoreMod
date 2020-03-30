package ru.allformine.afmcm.messaging;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class WindowedMessageGuiScreen extends GuiScreen {
    private String[] message;
    private int windowWidth = 256;
    private int windowHeight = 120;

    public WindowedMessageGuiScreen(String message) {
        this.message = MessagingUtils.splitByMaxChars(message, 41);
    }

    @Override
    public void initGui() {
        super.initGui();

        ScaledResolution resolution = new ScaledResolution(mc);

        int posX = center(windowWidth, resolution.getScaledWidth());
        int posY = center(windowHeight, resolution.getScaledHeight());

        int buttonWidth = 40;
        int buttonHeight = 20;

        int buttonPosX = posX + windowWidth - buttonWidth - 5;
        int buttonPosY = posY + windowHeight - buttonHeight - 2;

        this.buttonList.add(new GuiButton(1, buttonPosX, buttonPosY, buttonWidth, buttonHeight, "OK"));
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        this.drawDefaultBackground();

        ScaledResolution resolution = new ScaledResolution(mc);

        int posX = center(windowWidth, resolution.getScaledWidth());
        int posY = center(windowHeight, resolution.getScaledHeight());

        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(new ResourceLocation("afmcm", "textures/windowed_message.png"));
        drawTexturedModalRect(posX, posY, 0, 0, windowWidth, windowHeight);
        GL11.glPopMatrix();

        this.fontRenderer.drawString(I18n.format("afmcm.text.message"), posX + 7, posY + 5, 4210752);

        for (int i = 0; i < message.length; i++) {
            this.fontRenderer.drawString(message[i], posX + 4, posY + 17 + (i * this.fontRenderer.FONT_HEIGHT), 4210752);
        }

        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    private int center(int a, int b) {
        return (b - a) / 2;
    }
}
