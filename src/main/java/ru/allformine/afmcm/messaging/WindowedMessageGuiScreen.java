package ru.allformine.afmcm.messaging;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WindowedMessageGuiScreen extends GuiScreen {
    private String message;

    public WindowedMessageGuiScreen(String message) {
        this.message = message;
    }

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

        int buttonPosX = posX + windowWidth - buttonWidth - 5;
        int buttonPosY = posY + windowHeight - buttonHeight - 5;

        this.buttonList.add(new GuiButton(1, buttonPosX, buttonPosY, buttonWidth, buttonHeight, "OK"));
    }

    static ArrayList<String> splitString(String str) {
        ArrayList<String> res = new ArrayList<>();

        int maxLength = 22;
        Pattern p = Pattern.compile("\\G\\s*(.{1," + maxLength + "})(?=\\s|$)", Pattern.DOTALL);
        Matcher m = p.matcher(str);
        while (m.find()) {
            res.add(m.group(1));
        }

        return res;
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        this.drawDefaultBackground();

        ScaledResolution resolution = new ScaledResolution(mc);
        int windowWidth = 140;
        int windowHeight = 100;

        int posX = center(windowWidth, resolution.getScaledWidth());
        int posY = center(windowHeight, resolution.getScaledHeight());

        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(new ResourceLocation("afmcm", "textures/windowed_message.png"));
        drawTexturedModalRect(posX, posY, 0, 0, windowWidth, windowHeight);
        GL11.glPopMatrix();

        this.fontRenderer.drawString(I18n.format("afmcm.text.message"), posX + 7, posY + 5, 4210752);

        ArrayList<String> strings = splitString(message);

        for (int i = 0; i < strings.size(); i++) {
            this.fontRenderer.drawString(strings.get(i), posX + 5, posY + 17 + (i * this.fontRenderer.FONT_HEIGHT), 4210752);
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

    private int center(int a, int b) {
        return (b - a) / 2;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
