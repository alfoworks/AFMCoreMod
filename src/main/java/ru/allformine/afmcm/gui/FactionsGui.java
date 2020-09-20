package ru.allformine.afmcm.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.allformine.afmcm.ModUtils;

public class FactionsGui extends Gui {

    private static long animTime;

    private static String[] texts = {null, null};

    public static void addText(String text) {
        text = String.format("[%s%s]", text, ChatFormatting.RESET);

        texts[1] = texts[0];
        texts[0] = text;

        animTime = System.currentTimeMillis();
    }

    /*
    private void drawScreen() {
        ScaledResolution scaledResolution = event.getResolution();
        float width = scaledResolution.getScaledWidth();
        float height = scaledResolution.getScaledHeight();

        drawString(text, width / 2 + 93, height - this.mc.fontRenderer.FONT_HEIGHT - 2, 0xFFFFFF);
    }
     */

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        ScaledResolution scaledResolution = event.getResolution();
        Minecraft mc = Minecraft.getMinecraft();
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();

        int a;

        if (animTime != 0) {
            a = Math.min(255, (int) (System.currentTimeMillis() - animTime));

            if (texts[1] != null) {
                drawString(mc.fontRenderer, texts[1], width / 2 + 93, height - mc.fontRenderer.FONT_HEIGHT - 2, ModUtils.colorARGBtoInt(255 - a, 255, 255, 255));
            }

            if (a == 255) {
                animTime = 0;
            }
        } else {
            a = 255;
        }

        drawString(mc.fontRenderer, texts[0], width / 2 + 93, height - mc.fontRenderer.FONT_HEIGHT - 2, ModUtils.colorARGBtoInt(a, 255, 255, 255));
    }
}
