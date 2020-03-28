package ru.allformine.afmcm.messaging;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.allformine.afmcm.AFMCoreMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotifyMessageRenderer extends Gui {
    private static String message = null;
    private static long renderTime;
    private static int initialHeight = 1;

    public static void setMessage(String messageToSet) {
        message = messageToSet;
        renderTime = System.currentTimeMillis() + 5000;
        initialHeight = 1;

        Minecraft mc = Minecraft.getMinecraft();
        mc.player.playSound(AFMCoreMod.NOTIFY_SOUND_EVENT, mc.gameSettings.getSoundLevel(SoundCategory.VOICE), 1.0F);
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
        double cycle = ((double) (System.nanoTime() / 1000) % 0x200000) / (double) 0x200000;
        int titleAlpha = 160 + (int) (85.0D * Math.sin(cycle * 2 * Math.PI));
        Minecraft mc = Minecraft.getMinecraft();
        drawCenteredString(mc.fontRenderer, I18n.format("afmcm.text.notification"), width / 2, initialHeight, colorARGBtoInt(titleAlpha, 255, 0, 0));

        mc.player.sendMessage(new TextComponentString(message));
        mc.player.sendMessage(new TextComponentString(Arrays.toString(messageList.toArray())));

        for (int i = 0; i < messageList.size(); i++) {
            int textY = initialHeight + mc.fontRenderer.FONT_HEIGHT + i * mc.fontRenderer.FONT_HEIGHT;
            drawCenteredString(mc.fontRenderer, messageList.get(i), width / 2, textY, 0xFFFFFF);
        }

        if (System.currentTimeMillis() > renderTime) {
            message = null;
            initialHeight = 1;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (message != null && initialHeight < 41) {
            initialHeight++;
        }
    }
}
