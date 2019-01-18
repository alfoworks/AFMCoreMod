package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import ru.allformine.afmcm.References;

public class NotifierProxy {

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        ByteBuf buf = event.packet.payload();
        ByteBufUtils.readUTF8String(buf);
        String text = ByteBufUtils.readUTF8String(buf);

        if(text.length() > 0) {
            mc.thePlayer.playSound("afmcm:nstart", mc.gameSettings.getSoundLevel(SoundCategory.MASTER), 1.0F);
            References.notifyText = text;
            References.notifyDrawing = true;
        }
    }
}
