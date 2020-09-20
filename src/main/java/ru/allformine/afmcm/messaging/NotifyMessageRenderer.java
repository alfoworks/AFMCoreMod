package ru.allformine.afmcm.messaging;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import ru.allformine.afmcm.AFMCoreMod;
import ru.allformine.afmcm.ModUtils;

public class NotifyMessageRenderer extends Gui {
    private static String[] message = null;
    private static long renderTime;

    private static long animStartTime;

    public static void setMessage(String messageToSet) {
        message = MessagingUtils.splitByMaxChars(messageToSet, 20);
        renderTime = System.currentTimeMillis() + 5000;
        animStartTime = System.currentTimeMillis();

        Minecraft mc = Minecraft.getMinecraft();
        mc.player.playSound(AFMCoreMod.NOTIFY_SOUND_EVENT, mc.gameSettings.getSoundLevel(SoundCategory.VOICE), 1.0F);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (message == null) {
            return;
        }

        ScaledResolution scaledResolution = event.getResolution();
        Minecraft mc = Minecraft.getMinecraft();

        int width = scaledResolution.getScaledWidth();

        double cycle = ((double) (System.nanoTime() / 1000) % 0x200000) / (double) 0x200000;
        int titleAlpha = 160 + (int) (85.0D * Math.sin(cycle * 2 * Math.PI));

        drawCenteredString(mc.fontRenderer, I18n.format("afmcm.text.notification"), width / 2, 41, ModUtils.colorARGBtoInt(titleAlpha, 255, 0, 0));

        for (int i = 0; i < message.length; i++) {
            int textY = 41 + mc.fontRenderer.FONT_HEIGHT + i * mc.fontRenderer.FONT_HEIGHT;
            drawCenteredString(mc.fontRenderer, message[i], width / 2, textY, ModUtils.colorARGBtoInt((int) (System.currentTimeMillis() - animStartTime), 255, 255, 255));
        }

        if (System.currentTimeMillis() > renderTime) {
            message = null;
        }
    }
}
