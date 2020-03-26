package ru.allformine.afmcm.handlers.event;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.messaging.MessageDispatcher;
import ru.allformine.afmcm.messaging.MessageType;

public class MessagingPacketHandler {
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        ByteBuf buf = event.getPacket().payload();

        int messageTypeInt = buf.getInt(0);
        String messageText = ByteBufUtils.readUTF8String(buf);

        MessageType messageType;

        switch (messageTypeInt) {
            case 0:
                messageType = MessageType.NOTIFY_MESSAGE;
                break;
            case 1:
                messageType = MessageType.WINDOWED_MESSAGE;
                break;
            default:
                return;
        }

        MessageDispatcher.displayMessage(messageType, messageText);
    }
}
