package ru.allformine.afmcm.proxy;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.References;

public class FactionsProxy {
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        ByteBuf buf = event.getPacket().payload();
        ByteBufUtils.readUTF8String(buf);
        String text = ByteBufUtils.readUTF8String(buf);

        if(text.length() > 0) {
            References.factionText = text;
        }
    }
}
