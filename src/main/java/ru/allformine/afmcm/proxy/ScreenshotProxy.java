package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ScreenshotProxy {

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        for (byte _byte : bytes) {
            System.out.println(_byte);
        }

        ByteBuf buf = Unpooled.buffer(0);
        buf.writeBytes(bytes);

        C17PacketCustomPayload packet = new C17PacketCustomPayload("C234Fb", buf);
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
