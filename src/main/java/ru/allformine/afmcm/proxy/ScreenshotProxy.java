package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import ru.allformine.afmcm.screenshot.ScreenshotMaker;

import java.util.Arrays;

public class ScreenshotProxy {

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        byte[] image = ScreenshotMaker.getScreenshotByteArray(mc.displayWidth, mc.displayHeight, mc.getFramebuffer());

        if (image != null) {
            byte[][] dividedImages = new byte[2][];

            dividedImages[0] = Arrays.copyOfRange(image, 0, image.length / 2);
            dividedImages[1] = Arrays.copyOfRange(image, image.length / 2, image.length);

            for (byte[] dividedImage : dividedImages) {
                ByteBuf buf = Unpooled.buffer(0);

                buf.writeBytes(dividedImage);

                C17PacketCustomPayload packet = new C17PacketCustomPayload("scr", buf);
                mc.thePlayer.sendQueue.addToSendQueue(packet);
            }
        }
    }
}
