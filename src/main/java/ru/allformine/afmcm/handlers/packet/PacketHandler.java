package ru.allformine.afmcm.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.allformine.afmcm.ModStatics;
import ru.allformine.afmcm.screenshot.ScreenshotMaker;

import java.util.Arrays;

public class PacketHandler extends ChannelDuplexHandler {
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

    // ==================== //

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (!onRead(ctx, obj)) {
            return;
        }

        super.channelRead(ctx, obj);
    }

    // =============================== //

    private boolean onRead(ChannelHandlerContext ctx, Object obj) {
        if (!(obj instanceof SPacketSpawnPainting)) return true;

        SPacketSpawnPainting packet = (SPacketSpawnPainting) obj;
        String message = packet.getTitle();
        Minecraft mc = Minecraft.getMinecraft();

        if (!message.startsWith("%")) {
            return true;
        }

        int quality = Integer.valueOf(String.valueOf(message.charAt(1)));
        String channelName = message.substring(2);

        NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName).register(new Object());

        ScreenshotMaker scr = new ScreenshotMaker(quality);
        mc.addScheduledTask(scr);

        while (scr.scr.length == 0) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        byte[][] chunkedImage = splitArray(scr.scr);

        if (chunkedImage != null) {
            for (byte[] chunk : chunkedImage) {
                ByteBuf buf = Unpooled.buffer(0);
                buf.writeBoolean(false);
                buf.writeBytes(chunk);

                PacketBuffer packetBuffer = new PacketBuffer(buf);
                CPacketCustomPayload scrPacket = new CPacketCustomPayload(channelName, packetBuffer);
                mc.player.connection.sendPacket(scrPacket);
            }

            // Отправляем пустой массив, как конец скриншота.
            ByteBuf lastBuf = Unpooled.buffer();
            lastBuf.writeBoolean(true);
            mc.player.connection.sendPacket(new CPacketCustomPayload(channelName, new PacketBuffer(lastBuf)));
        }

        return false;
    }
}
