package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

public class DebugGui extends GuiScreen {
    GuiTextField pitchInput;
    GuiTextField yawInput;

    @Override
    public void initGui() {
        super.initGui();

        ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
        Minecraft mc = Minecraft.getMinecraft();

        int screenWidth = scaled.getScaledWidth();
        int screenHeight = scaled.getScaledHeight();

        pitchInput = new GuiTextField(1, fontRenderer, center(137, screenWidth), center(20, screenHeight) - 25, 137, 20);
        yawInput = new GuiTextField(2, fontRenderer, center(137, screenWidth), center(20, screenHeight) - 50, 137, 20);

        pitchInput.setText(String.valueOf(mc.player.rotationPitch));
        yawInput.setText(String.valueOf(mc.player.rotationYaw));

        this.buttonList.add(new GuiButton(3, center(60, screenWidth), center(20, screenHeight), 60, 20, "Set rotation"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        pitchInput.drawTextBox();
        yawInput.drawTextBox();

        ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());

        int screenWidth = scaled.getScaledWidth();
        int screenHeight = scaled.getScaledHeight();

        String stringPitch = "Pitch: ";
        String stringYaw = "Yaw: ";

        fontRenderer.drawStringWithShadow(stringPitch, center(fontRenderer.getStringWidth(stringPitch), screenWidth) - 80, center(20, screenHeight) - 25, 0xFFFFFF);
        fontRenderer.drawStringWithShadow(stringYaw, center(fontRenderer.getStringWidth(stringYaw), screenWidth) - 80, center(20, screenHeight) - 50, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 3) {
            Minecraft mc = Minecraft.getMinecraft();

            mc.player.rotationPitch = Float.parseFloat(pitchInput.getText());
            mc.player.rotationYaw = Float.parseFloat(yawInput.getText());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        pitchInput.updateCursorCounter();
        yawInput.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
        super.keyTyped(par1, par2);

        GuiTextField field;

        if (pitchInput.isFocused()) {
            field = pitchInput;
        } else {
            field = yawInput;
        }

        field.textboxKeyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        pitchInput.mouseClicked(mouseX, mouseY, mouseButton);
        yawInput.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private int center(int a, int b) {
        return (b - a) / 2;
    }
}
