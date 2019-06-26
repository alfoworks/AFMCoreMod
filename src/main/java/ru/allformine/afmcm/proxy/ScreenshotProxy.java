package ru.allformine.afmcm.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.References;
import ru.allformine.afmcm.screenshot.ScreenshotMaker;

import java.awt.image.BufferedImage;

public class ScreenshotProxy {

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        ByteBuf _buf = event.getPacket().payload();
        int mode;

        switch (ByteBufUtils.readUTF8String(_buf)) {
            case "highres":
                mode = 1;
                break;
            case "grayscale":
                mode = BufferedImage.TYPE_USHORT_GRAY;
                break;
            case "lowres":
            default:
                mode = BufferedImage.TYPE_USHORT_555_RGB;
                break;
        }

        mc.addScheduledTask(() -> {
            References.lastImage = ScreenshotMaker.getScreenshotByteArray(mc.displayWidth, mc.displayHeight, mc.getFramebuffer(), mode);
        });

        try {
            Thread.sleep(1500); // Очень больщой костыль из-за проблем с потоками. Возможно, подойдет
        } catch (InterruptedException e) { //synchronized
            e.printStackTrace();
        }

        if (References.lastImage != null) {
            byte[][] chunkedImage = Util.splitArray(References.lastImage, 10240);

            if (chunkedImage != null) {
                for (byte[] chunk : chunkedImage) {
                    ByteBuf buf = Unpooled.buffer(0);
                    buf.writeBytes(chunk);

                    PacketBuffer packetBuffer = new PacketBuffer(buf);
                    CPacketCustomPayload packet = new CPacketCustomPayload("AN3234234A", packetBuffer);
                    mc.player.connection.sendPacket(packet);
                }

                // Отправляем пустой массив, как конец скриншота.
                ByteBuf lastBuf = Unpooled.buffer();
                lastBuf.writeBytes(new byte[]{});
                mc.player.connection.sendPacket(new CPacketCustomPayload("AN3234234A", new PacketBuffer(lastBuf)));
            }
        }
    }
}
