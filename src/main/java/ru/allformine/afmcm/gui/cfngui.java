package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class cfngui
  extends Gui
{
  private String text;
  private Minecraft mc;
  
  public cfngui(String text, Minecraft mc)
  {
    this.text = text;
    this.mc = mc;
    drawScreen();
  }
  
  private void drawScreen()
  {
    ScaledResolution scaled = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    
    int width = scaled.getScaledWidth();
    int height = scaled.getScaledHeight();
    
    String s = "[" + this.text + EnumChatFormatting.WHITE + "]";
    this.mc.fontRenderer.drawStringWithShadow(s,
            10,
            10,
            16777215);
  }
}
