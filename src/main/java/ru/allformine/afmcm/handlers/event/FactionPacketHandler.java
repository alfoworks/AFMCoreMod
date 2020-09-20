package ru.allformine.afmcm.handlers.event;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.ModStatics;
import ru.allformine.afmcm.gui.FactionsGui;

public class FactionPacketHandler {
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        ByteBuf buf = event.getPacket().payload();

        FactionsGui.addText(ByteBufUtils.readUTF8String(buf));
    }
}
