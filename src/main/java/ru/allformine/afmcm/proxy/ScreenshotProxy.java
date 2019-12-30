package ru.allformine.afmcm.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.ModStatics;
import ru.allformine.afmcm.screenshot.ScreenshotMaker;

import java.awt.image.BufferedImage;
import java.util.Arrays;

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
            ModStatics.lastImage = ScreenshotMaker.getScreenshotByteArray(mc.displayWidth, mc.displayHeight, mc.getFramebuffer(), mode);
        });

        long time = System.currentTimeMillis();
        while (ModStatics.lastImage == null) {
            if (time > (System.currentTimeMillis() + 5000)) {
                return;
            }
        }
        
        byte[][] chunkedImage = splitArray(ModStatics.lastImage);

        if (chunkedImage != null) {
            for (byte[] chunk : chunkedImage) {
                ByteBuf buf = Unpooled.buffer(0);
                buf.writeBoolean(false);
                buf.writeBytes(chunk);

                PacketBuffer packetBuffer = new PacketBuffer(buf);
                CPacketCustomPayload packet = new CPacketCustomPayload("AN3234234A", packetBuffer);
                mc.player.connection.sendPacket(packet);
            }

            // Отправляем пустой массив, как конец скриншота.
            ByteBuf lastBuf = Unpooled.buffer();
            lastBuf.writeBoolean(true);
            mc.player.connection.sendPacket(new CPacketCustomPayload("AN3234234A", new PacketBuffer(lastBuf)));
            
            ModStatics.lastImage = null;
        }
    }
    
    // =============================== //

    private static byte[][] splitArray(byte[] arrayToSplit) {
        if (ModStatics.screenshotChunkSize <= 0) {
            return null;
        }

        int rest = arrayToSplit.length % ModStatics.screenshotChunkSize;
        int chunks = arrayToSplit.length / ModStatics.screenshotChunkSize + (rest > 0 ? 1 : 0);

        byte[][] arrays = new byte[chunks][];

        for (int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++) {
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * ModStatics.screenshotChunkSize, i * ModStatics.screenshotChunkSize + ModStatics.screenshotChunkSize);
        }

        if (rest > 0) {
            arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * ModStatics.screenshotChunkSize, (chunks - 1) * ModStatics.screenshotChunkSize + rest);
        }

        return arrays;
    }
}
