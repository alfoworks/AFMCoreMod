package ru.allformine.afmcm.messaging;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotifyMessageRenderer extends Gui {
    private static String message = null;
    private static long renderTime;
    private double cycle = ((double) (System.nanoTime() / 1000) % 0x200000) / (double) 0x200000;

    public static void setMessage(String messageToSet) {
        message = messageToSet;
        renderTime = System.currentTimeMillis() + 5000;
    }

    private static int clamp(final int min, final int max, final int value) {
        return Math.min(max, Math.max(value, min));
    }

    static int colorARGBtoInt(final int alpha, final int red, final int green, final int blue) {
        return (clamp(0, 255, alpha) << 24)
                + (clamp(0, 255, red) << 16)
                + (clamp(0, 255, green) << 8)
                + clamp(0, 255, blue);
    }

    static ArrayList<String> splitString(String str) {
        ArrayList<String> res = new ArrayList<>();

        int maxLength = 20;
        Pattern p = Pattern.compile("\\G\\s*(.{1," + maxLength + "})(?=\\s|$)", Pattern.DOTALL);
        Matcher m = p.matcher(str);
        while (m.find()) {
            res.add(m.group(1));
        }

        return res;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (message == null) {
            return;
        }

        ArrayList<String> messageList = splitString(message);

        ScaledResolution scaledResolution = event.getResolution();
        int width = scaledResolution.getScaledWidth();
        int titleAlpha = 160 + (int) (85.0D * Math.sin(cycle * 2 * Math.PI));
        Minecraft mc = Minecraft.getMinecraft();

        drawCenteredString(mc.fontRenderer, "Notification", width / 2, 41, colorARGBtoInt(titleAlpha, 255, 0, 0));

        for (int i = 0; i < messageList.size(); i++) {
            int textY = 50 + i * mc.fontRenderer.FONT_HEIGHT;
            drawCenteredString(mc.fontRenderer, messageList.get(i), width / 2, textY, 0xFFFFFF);
        }

        if (System.currentTimeMillis() > renderTime) {
            message = null;
        }
    }
}
