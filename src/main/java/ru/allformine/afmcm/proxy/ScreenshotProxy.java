package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import ru.allformine.afmcm.screenshot.ScreenshotMaker;

import java.awt.image.BufferedImage;

public class ScreenshotProxy {

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        ByteBuf _buf = event.packet.payload();
        ByteBufUtils.readUTF8String(_buf);
        int mode;

        switch (ByteBufUtils.readUTF8String(_buf)) {
            case "highres":
                mode = 1;
                break;
            case "extralowres":
                mode = BufferedImage.TYPE_4BYTE_ABGR_PRE;
                break;
            case "grayscale":
                mode = BufferedImage.TYPE_USHORT_GRAY;
                break;
            case "lowres":
            default:
                mode = BufferedImage.TYPE_USHORT_555_RGB;
                break;
        }

        byte[] image = ScreenshotMaker.getScreenshotByteArray(mc.displayWidth, mc.displayHeight, mc.getFramebuffer(), mode);

        if (image != null) {
            byte[][] chunkedImage = Util.splitArray(image, 10240);

            if (chunkedImage != null) {

                for (byte[] chunk : chunkedImage) {

                    ByteBuf buf = Unpooled.buffer(0);
                    buf.writeBytes(chunk);

                    buf.writeByte(290);

                    C17PacketCustomPayload packet = new C17PacketCustomPayload("C234Fb", buf);
                    mc.thePlayer.sendQueue.addToSendQueue(packet);
                }
            }
        }
    }
}
