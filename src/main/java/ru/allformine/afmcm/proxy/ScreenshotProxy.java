package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import ru.allformine.afmcm.screenshot.ScreenshotMaker;

public class ScreenshotProxy {

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        byte[] image = ScreenshotMaker.getScreenshotByteArray(mc.displayWidth, mc.displayHeight, mc.getFramebuffer());

        if (image != null) {
            byte[][] chunkedImage = Util.splitArray(image, 32000);

            if (chunkedImage != null) {
                for (byte[] chunk : chunkedImage) {
                    ByteBuf buf = Unpooled.buffer(0);
                    buf.writeBytes(chunk);

                    C17PacketCustomPayload packet = new C17PacketCustomPayload("scr", buf);
                    mc.thePlayer.sendQueue.addToSendQueue(packet);
                }

                ByteBuf buf = Unpooled.buffer(0);
                buf.writeByte(9);

                C17PacketCustomPayload packet = new C17PacketCustomPayload("scr", buf);
                mc.thePlayer.sendQueue.addToSendQueue(packet);

                if (mc.thePlayer.getDisplayName().equals("Iterator")) {
                    System.out.println("Sent " + String.valueOf(chunkedImage.length) + " image packets.");
                }
            }
        }
    }
}
