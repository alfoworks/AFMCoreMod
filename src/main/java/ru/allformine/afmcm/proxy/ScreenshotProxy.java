package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import ru.allformine.afmcm.screenshot.ScreenshotHelper;

public class ScreenshotProxy {

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        byte[] image = ScreenshotHelper.getScreenshot(mc.displayWidth, mc.displayHeight, mc.getFramebuffer());

        ByteBuf buf = Unpooled.buffer(0);
        buf.writeByte(1);
        buf.writeBoolean(true);
        buf.writeBytes(image);

        C17PacketCustomPayload packet = new C17PacketCustomPayload("scr", buf);
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
