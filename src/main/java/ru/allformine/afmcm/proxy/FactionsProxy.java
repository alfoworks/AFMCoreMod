package ru.allformine.afmcm.proxy;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.ModStatics;

public class FactionsProxy {
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        ByteBuf buf = event.getPacket().payload();

        ModStatics.factionText = ByteBufUtils.readUTF8String(buf);
    }
}
