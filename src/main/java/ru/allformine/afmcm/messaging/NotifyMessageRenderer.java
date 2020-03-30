package ru.allformine.afmcm.messaging;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.allformine.afmcm.AFMCoreMod;

public class NotifyMessageRenderer extends Gui {
    private static String[] message = null;
    private static long renderTime;

    // Анимация
    private static int initialHeight = 1;

    public static void setMessage(String messageToSet) {
        message = MessagingUtils.splitByMaxChars(messageToSet, 20);
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

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (message == null) {
            return;
        }

        ScaledResolution scaledResolution = event.getResolution();
        Minecraft mc = Minecraft.getMinecraft();

        checkAnim();

        int width = scaledResolution.getScaledWidth();

        double cycle = ((double) (System.nanoTime() / 1000) % 0x200000) / (double) 0x200000;
        int titleAlpha = 160 + (int) (85.0D * Math.sin(cycle * 2 * Math.PI));

        drawCenteredString(mc.fontRenderer, I18n.format("afmcm.text.notification"), width / 2, initialHeight, colorARGBtoInt(titleAlpha, 255, 0, 0));

        for (int i = 0; i < message.length; i++) {
            int textY = initialHeight + mc.fontRenderer.FONT_HEIGHT + i * mc.fontRenderer.FONT_HEIGHT;
            drawCenteredString(mc.fontRenderer, message[i], width / 2, textY, 0xFFFFFF);
        }

        if (System.currentTimeMillis() > renderTime) {
            message = null;
            initialHeight = 1;
        }
    }

    private void checkAnim() {
        if (initialHeight < 41) {
            initialHeight++;
        }
    }
}
